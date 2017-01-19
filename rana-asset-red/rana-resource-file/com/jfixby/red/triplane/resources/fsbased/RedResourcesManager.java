
package com.jfixby.red.triplane.resources.fsbased;

import java.io.IOException;

import com.jfixby.rana.api.cfg.AssetsFolder;
import com.jfixby.rana.api.cfg.ResourcesConfigFile;
import com.jfixby.rana.api.pkg.DeployRemoteBanksTask;
import com.jfixby.rana.api.pkg.PackageFormat;
import com.jfixby.rana.api.pkg.PackageReader;
import com.jfixby.rana.api.pkg.PackageSearchParameters;
import com.jfixby.rana.api.pkg.PackageSearchResult;
import com.jfixby.rana.api.pkg.Resource;
import com.jfixby.rana.api.pkg.ResourceRebuildIndexListener;
import com.jfixby.rana.api.pkg.ResourceSpecs;
import com.jfixby.rana.api.pkg.ResourcesGroup;
import com.jfixby.rana.api.pkg.ResourcesManagerComponent;
import com.jfixby.rana.api.pkg.bank.BankHeaderInfo;
import com.jfixby.scarabei.api.assets.ID;
import com.jfixby.scarabei.api.assets.Names;
import com.jfixby.scarabei.api.collections.Collection;
import com.jfixby.scarabei.api.collections.Collections;
import com.jfixby.scarabei.api.collections.List;
import com.jfixby.scarabei.api.collections.Map;
import com.jfixby.scarabei.api.debug.Debug;
import com.jfixby.scarabei.api.err.Err;
import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.file.FilesList;
import com.jfixby.scarabei.api.json.Json;
import com.jfixby.scarabei.api.json.JsonString;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.api.net.http.Http;
import com.jfixby.scarabei.api.net.http.HttpFileSystem;
import com.jfixby.scarabei.api.net.http.HttpFileSystemSpecs;
import com.jfixby.scarabei.api.net.http.HttpURL;

public class RedResourcesManager implements ResourcesManagerComponent {

	private final File assets_folder;
	private final File assets_cache_folder;
	private final List<RemoteBankSettings> remoteBanksToDepoloy = Collections.newList();
	Map<ID, ResourcesGroup> resources = Collections.newMap();
	boolean deployed = false;

	public RedResourcesManager (final RedResourcesManagerSpecs specs) {
		this.deployed = false;
		this.assets_folder = specs.getAssetFolder();
		this.assets_cache_folder = specs.getAssetCacheFolder();
		final ResourceRebuildIndexListener listener = specs.getListener();
		try {
			final Collection<ResourcesGroup> locals = this.loadAssetsFolder(this.assets_folder, listener);

		} catch (final IOException e) {
			e.printStackTrace();
		}

		final Collection<RemoteBankSpecs> remotebanks = specs.listRemoteBanks();
		for (final RemoteBankSpecs rbank : remotebanks) {
			final RemoteBankSettings element = new RemoteBankSettings();
			element.bankURL = Debug.checkNull("remote bank url", rbank.getUrl());
			element.tanks.addAll(rbank.listTanks());
			this.remoteBanksToDepoloy.add(element);
		}
	}

	Collection<ResourcesGroup> loadAssetsFolder (final File assets_folder, final ResourceRebuildIndexListener listener)
		throws IOException {
		if (assets_folder.exists() && assets_folder.isFolder()) {
			final Collection<ResourcesGroup> locals = this.findAndInstallResources(assets_folder);
			locals.print("locals");
			for (final ResourcesGroup local : locals) {
				local.rebuildAllIndexes(listener);
			}
			return locals;
		}
		return null;
	}

	void loadRemoteBank (final HttpURL bankURL, final Iterable<String> tanks, final File assets_cache_folder,
		final ResourceRebuildIndexListener listener) throws IOException {
		final ResourcesGroup bank = this.installRemoteBank(bankURL, assets_cache_folder, tanks);
		bank.rebuildAllIndexes(listener);
	}

	private static final boolean COLLECT_TANKS = true;

	@Override
	public PackageSearchParameters newSearchParameters () {
		return new RedPackageSearchParameters();
	}

	@Override
	public PackageSearchResult findPackages (final PackageSearchParameters search_params) {
		final RedPackageSearchResult result = new RedPackageSearchResult(search_params);
		Debug.checkNull("search_params", search_params);
		for (int i = 0; i < this.resources.size(); i++) {
			final PackageSearchResult result_i = this.resources.getValueAt(i).findPackages(search_params);
			result.add(result_i);
		}

		return result;
	}

	private void installBank (final ResourcesGroup group) {
		Debug.checkNull("resource_to_install", group);
		final ID name = group.getName();

		if (this.resources.containsKey(name)) {
			Err.reportError("Resource with this name <" + name + "> is already installed: " + this.resources.get(name));
		}

		this.resources.put(name, group);
	}

// public void removeResource (final Resource resource) {
// this.resources.remove(resource);
// }

	@Override
	public void printAllPackages () {
		this.resources.print("All installed resources");
		final PackageSearchParameters search_params = this.newSearchParameters();
		search_params.setGetAllAssetsFlag(true);
		final PackageSearchResult packages = this.findPackages(search_params);
		packages.list().print("All available packages");
		;
	}

	@Override
	public Collection<PackageReader> findPackageReaders (final PackageFormat format) {
		final List<PackageReader> list = this.loaders_by_format.get(format);
		if (list == null) {
			// L.d("format", format);
			// loaders_by_format.print("acceptable formats");
			return Collections.newList();
		}
		return list;
	}

	@Override
	public void registerPackageReader (final PackageReader loader) {
		if (this.loaders.contains(loader)) {
			L.d("Loader is already registred:" + loader);
			return;
		}

		final Collection<PackageFormat> can_read = loader.listAcceptablePackageFormats();
		Debug.checkNull("PackageReader.listAcceptablePackageFormats()", can_read);
		this.loaders.add(loader);
		for (int i = 0; i < can_read.size(); i++) {
			final PackageFormat format = can_read.getElementAt(i);
			List<PackageReader> loaders_list = this.loaders_by_format.get(format);
			if (loaders_list == null) {
				loaders_list = Collections.newList();
				this.loaders_by_format.put(format, loaders_list);
			}
			loaders_list.add(loader);
		}

	}

	final Map<PackageFormat, List<PackageReader>> loaders_by_format = Collections.newMap();
	final List<PackageReader> loaders = Collections.newList();

	@Override
	public Collection<PackageFormat> listAcceptablePackageFormats () {
		return this.loaders_by_format.keys();
	}

	@Override
	public void printInstalledPackageReaders () {
		this.loaders_by_format.print("Installed package readers");
	}

	@Override
	public PackageFormat newPackageFormat (final String format_name) {
		return new PackageFormatImpl(format_name);
	}

	@Override
	public Collection<ResourcesGroup> findAndInstallResources (final File assets_folder) throws IOException {
		final List<ResourcesGroup> resources = this.findBanks(assets_folder);
		this.installBanks(resources);
		return resources;
	}

	@Override
	public void installBanks (final Collection<ResourcesGroup> resources) {
		for (final ResourcesGroup r : resources) {
			this.installBank(r);
		}
	}

	@Override
	public List<ResourcesGroup> findBanks (final File assets_folder) throws IOException {
		final List<ResourcesGroup> result = Collections.newList();

		{
			final RedBank bank = this.findBank(assets_folder, COLLECT_TANKS);
			if (bank != null) {
				result.add(bank);
				return result;
			}
		}

		for (final File file : assets_folder.listDirectChildren()) {
			if (file.isFile()) {
				continue;
			}
			final RedBank bank = this.findBank(file, COLLECT_TANKS);
			if (bank != null) {
				result.add(bank);
			}
		}

		return result;
	}

	private RedBank findBank (final File bankFolder, final boolean collect_tanks) throws IOException {
		if (!bankFolder.exists()) {
			L.e("bank not found", bankFolder);
			return null;
		}

		final BankHeader bankHeader = this.findAndLoadBankHeader(bankFolder);

		if (bankHeader == null) {
			return null;
		}

		L.d("found bank", bankHeader);

		final ID bank_name = Names.newID(bankHeader.getName());
		final RedBank bank = new RedBank(bank_name);
		if (!collect_tanks) {
			return bank;
		}

		final File bank_root = bankHeader.getRoot();
		final FilesList tanks = bank_root.listSubFolders();
		for (final File tank : tanks) {
			final ResourceSpecs resSpec = this.newResourceSpecs();
			resSpec.setFolder(tank);
			resSpec.setCachingRequired(false);
			final String tankName = tank.getName();
			resSpec.setName(tankName);
			final Resource resource = this.newResource(resSpec);
			bank.addResource(resource);
		}
		return bank;
	}

	@Override
	public Resource newResource (final ResourceSpecs resSpec) throws IOException {
		return new RedResource(resSpec);
	}

	@Override
	public ResourceSpecs newResourceSpecs () {
		return new RedResourceSpecs();
	}

	private BankHeader findAndLoadBankHeader (final File bank_folder) throws IOException {
		if (!(bank_folder.exists() && bank_folder.isFolder())) {
			return null;
		}

		final File header_file = bank_folder.child(BankHeaderInfo.FILE_NAME);
		if (!header_file.exists()) {
			return null;
		}

		String data;
		try {
			data = header_file.readToString();
		} catch (final IOException e) {
			e.printStackTrace();
			return null;
		}

		final JsonString json_data = Json.newJsonString(data);

		final BankHeaderInfo headerInfo = Json.deserializeFromString(BankHeaderInfo.class, json_data);

		final BankHeader header = new BankHeader(headerInfo, bank_folder);
		return header;

	}

	public ResourcesConfigFile loadConfigFile (final File applicationHome) {

		ResourcesConfigFile config = null;
		try {
			final File resources_config_file = applicationHome.child(ResourcesConfigFile.FILE_NAME);
			L.d("reading", resources_config_file);

			if (!resources_config_file.exists()) {
				return null;
			}

			final String configString = resources_config_file.readToString();

			config = Json.deserializeFromString(ResourcesConfigFile.class, configString);
			return config;
		} catch (final IOException e) {
			e.printStackTrace();
			return null;
		}

	}

// public void tryToLoadConfigFile (final File applicationHome) throws IOException {
// ResourcesConfigFile cfg = this.loadConfigFile(applicationHome);
// if (cfg == null) {
// cfg = this.tryToMakeConfigFile(applicationHome);
// }
//
// if (cfg == null) {
// return;
// }
// for (final AssetsFolder assets : cfg.local_assets) {
// final String java_path = assets.java_path;
// final File assets_folder = LocalFileSystem.newFile(java_path);
// this.findAndInstallResources(assets_folder);
// }
//
// }

	// File dev_assets_home =
	// LocalFileSystem.newFile(TintoAssetsConfig.PACKED_ASSETS_HOME);
// final File dev_assets_home = LocalFileSystem.newFile(TintoAssetsConfig.PACKED_ASSETS_HOME);

	private ResourcesConfigFile tryToMakeConfigFile (final File applicationHome) {
		final ResourcesConfigFile config = new ResourcesConfigFile();

		final AssetsFolder assets_folder = new AssetsFolder();
		assets_folder.java_path = "path?";
		config.local_assets.add(assets_folder);

		final File resources_config_file = applicationHome.child(ResourcesConfigFile.FILE_NAME);
		try {
			if (resources_config_file.exists()) {
				L.e("file exists", resources_config_file);
				final String data = resources_config_file.readToString();
				L.e(data);
				return null;
			}
			final String stringData = Json.serializeToString(config).toString();

			L.d("writing", resources_config_file);
			resources_config_file.writeString(stringData);
			L.d(stringData);

		} catch (final IOException e) {
			e.printStackTrace();
		}
		return config;

	}
// }
//

// @Override
// public void updateAll (ResourceRebuildIndexListener listener) {
// if (listener == null) {
// listener = ResourceRebuildIndexListener.DEFAULT;
// }
// for (final Resource res : this.resources.values()) {
// res.rebuildIndex(listener);
// }
// }

	@Override
	public ResourcesGroup installRemoteBank (final HttpURL bankUrl, final File assets_cache_folder, final Iterable<String> tanks)
		throws IOException {
		Debug.checkNull("bankUrl", bankUrl);
		Debug.checkNull("tanks", tanks);

		final HttpFileSystemSpecs specs = Http.newHttpFileSystemSpecs();

		final HttpURL url = bankUrl;
		specs.setRootUrl(url);
		specs.setCacheSize(200);

		final HttpFileSystem fs = Http.newHttpFileSystem(specs);
		final File httpRemote = fs.ROOT();
		final RedBank bank = this.findBank(httpRemote, !COLLECT_TANKS);
		if (bank == null) {
			Err.reportError("Bank not found at " + httpRemote);
		}
		final File bank_cache_folder = assets_cache_folder.child(bank.getName() + "");
		for (final String tank : tanks) {
			final ResourceSpecs resSpec = this.newResourceSpecs();
			final File tankFolder = httpRemote.child(tank);
			resSpec.setFolder(tankFolder);
			resSpec.setCachingRequired(true);

			final File tankCache = bank_cache_folder.child(tank);
			tankCache.makeFolder();
			resSpec.setCacheFolder(tankCache);
			resSpec.setName(tank);

			final Resource resource = this.newResource(resSpec);

			bank.addResource(resource);

		}
		this.installBank(bank);
		return bank;
	}

// @Override
// public Resource getResource (final String name) {
// return this.resources.get(name);
// }

	@Override
	public void printAllResources () {
		this.resources.print("resources");
	}

	@Override
	public void printAllIndexes () {
		for (int i = 0; i < this.resources.size(); i++) {
			this.resources.getValueAt(i).printAllIndexes();
		}
	}

	@Override
	public ResourcesGroup getResourcesGroup (final ID name) {
		return this.resources.get(name);
	}

	@Override
	public DeployRemoteBanksTask prepareDeployRemoteBanksTask () {
		return new RedDeployRemoteBanksTask(this);
	}

	List<RemoteBankSettings> getRemoteBanksToDeploy () {
		return this.remoteBanksToDepoloy;
	}

	public File getAssetsCache () {
		return this.assets_cache_folder;
	}

//

}
