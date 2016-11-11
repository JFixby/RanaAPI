
package com.jfixby.red.triplane.resources.fsbased;

import java.io.IOException;

import com.jfixby.cmns.api.collections.Collection;
import com.jfixby.cmns.api.collections.Collections;
import com.jfixby.cmns.api.collections.List;
import com.jfixby.cmns.api.collections.Map;
import com.jfixby.cmns.api.collections.Set;
import com.jfixby.cmns.api.debug.Debug;
import com.jfixby.cmns.api.err.Err;
import com.jfixby.cmns.api.file.File;
import com.jfixby.cmns.api.file.LocalFileSystem;
import com.jfixby.cmns.api.json.Json;
import com.jfixby.cmns.api.json.JsonString;
import com.jfixby.cmns.api.log.L;
import com.jfixby.cmns.api.net.http.Http;
import com.jfixby.cmns.api.net.http.HttpFileSystem;
import com.jfixby.cmns.api.net.http.HttpFileSystemSpecs;
import com.jfixby.cmns.api.net.http.HttpURL;
import com.jfixby.rana.api.cfg.AssetsFolder;
import com.jfixby.rana.api.cfg.ResourcesConfigFile;
import com.jfixby.rana.api.pkg.PackageFormat;
import com.jfixby.rana.api.pkg.PackageReader;
import com.jfixby.rana.api.pkg.PackageSearchParameters;
import com.jfixby.rana.api.pkg.PackageSearchResult;
import com.jfixby.rana.api.pkg.Resource;
import com.jfixby.rana.api.pkg.ResourceRebuildIndexListener;
import com.jfixby.rana.api.pkg.ResourceSpecs;
import com.jfixby.rana.api.pkg.ResourcesManagerComponent;
import com.jfixby.rana.api.pkg.bank.BankHeaderInfo;

public class RedResourcesManager implements ResourcesManagerComponent {

	final Map<String, Resource> resources = Collections.newMap();

	@Override
	public PackageSearchParameters newSearchParameters () {
		return new RedPackageSearchParameters();
	}

	@Override
	public PackageSearchResult findPackages (final PackageSearchParameters search_params) {
		final RedPackageSearchResult result = new RedPackageSearchResult(search_params);
		Debug.checkNull("search_params", search_params);
		for (int i = 0; i < this.resources.size(); i++) {
			final Resource resource = this.resources.getValueAt(i);
			final PackageSearchResult result_i = resource.findPackages(search_params);
			result.add(result_i);
		}

		return result;
	}

	public void installResource (final Resource resource_to_install) {
		Debug.checkNull("resource_to_install", resource_to_install);
		final String name = resource_to_install.getName();
		if (this.resources.containsKey(name)) {
			Err.reportError("Resource with this name <" + name + "> is already installed: " + this.resources.get(name));
		}
		this.resources.put(name, resource_to_install);
	}

	public void removeResource (final Resource resource) {
		this.resources.remove(resource);
	}

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

	public void findAndInstallResources (final File assets_folder) throws IOException {
		final Collection<Resource> resources = this.findResources(assets_folder);
		this.installResources(resources);
	}

	@Override
	public void installResources (final Collection<Resource> resources) {
		for (final Resource r : resources) {
			this.installResource(r);
		}
	}

	@Override
	public Collection<Resource> findResources (final File assets_folder) throws IOException {
		final Set<Resource> result = Collections.newSet();
		if (!assets_folder.exists()) {
			L.e("bank not found", assets_folder);
			return result;
		}
		{
			final BankHeader bankHeader = this.findAndLoadBank(assets_folder);
			if (bankHeader == null) {
// L.e("corrupted bank", assets_folder);
			} else {
				L.d("found bank", bankHeader);
				final File bank_folder = bankHeader.getRoot();

				final ResourceSpecs resSpec = this.newResourceSpecs();
				resSpec.setBankFolder(bank_folder);
				resSpec.setCachingRequired(false);
				final String bankName = bank_folder.getName();
				resSpec.setName(bankName);

				final Resource resource = this.newResource(resSpec);

				result.add(resource);
				return result;
			}
		}

		for (final File file : assets_folder.listDirectChildren()) {
			if (file.isFile()) {
				continue;
			}
			final BankHeader bankHeader = this.findAndLoadBank(file);
			if (bankHeader == null) {
				L.e("corrupted bank", file);
			} else {
				L.d("found bank", bankHeader);
				final File bank_folder = bankHeader.getRoot();

				final ResourceSpecs resSpec = this.newResourceSpecs();
				resSpec.setBankFolder(bank_folder);
				resSpec.setCachingRequired(false);
				final String bankName = bank_folder.getName();
				resSpec.setName(bankName);
				final Resource resource = this.newResource(resSpec);

				result.add(resource);
			}
		}

		return result;
	}

	@Override
	public Resource newResource (final ResourceSpecs resSpec) throws IOException {
		return new RedResource(resSpec);
	}

	@Override
	public ResourceSpecs newResourceSpecs () {
		return new RedResourceSpecs();
	}

	private BankHeader findAndLoadBank (final File bank_folder) throws IOException {
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

	public ResourcesConfigFile loadConfigFile () {

		ResourcesConfigFile config = null;
		try {
			final File resources_config_file = LocalFileSystem.ApplicationHome().child(ResourcesConfigFile.FILE_NAME);
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

	public void tryToLoadConfigFile () throws IOException {
		ResourcesConfigFile cfg = this.loadConfigFile();
		if (cfg == null) {
			cfg = this.tryToMakeConfigFile();
		}

		if (cfg == null) {
			return;
		}
		for (final AssetsFolder assets : cfg.local_assets) {
			final String java_path = assets.java_path;
			final File assets_folder = LocalFileSystem.newFile(java_path);
			this.findAndInstallResources(assets_folder);
		}

	}

	// File dev_assets_home =
	// LocalFileSystem.newFile(TintoAssetsConfig.PACKED_ASSETS_HOME);
// final File dev_assets_home = LocalFileSystem.newFile(TintoAssetsConfig.PACKED_ASSETS_HOME);

	private ResourcesConfigFile tryToMakeConfigFile () {
		final ResourcesConfigFile config = new ResourcesConfigFile();

		final AssetsFolder assets_folder = new AssetsFolder();
		assets_folder.java_path = "path?";
		config.local_assets.add(assets_folder);

		final File resources_config_file = LocalFileSystem.ApplicationHome().child(ResourcesConfigFile.FILE_NAME);
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

	@Override
	public void updateAll (ResourceRebuildIndexListener listener) {
		if (listener == null) {
			listener = ResourceRebuildIndexListener.DEFAULT;
		}
		for (final Resource res : this.resources.values()) {
			res.rebuildIndex(listener);
		}
	}

	public void installRemoteBank (final String bankName, final String bankUrl, final File assets_cache_folder)
		throws IOException {
		Debug.checkNull("bankName", bankName);
		Debug.checkNull("bankUrl", bankUrl);

		assets_cache_folder.makeFolder();

		final HttpFileSystemSpecs specs = Http.newHttpFileSystemSpecs();
		final String urlString = bankUrl;
		final HttpURL url = Http.newURL(urlString);
		specs.setRootUrl(url);
		specs.setCacheSize(200);
		final HttpFileSystem fs = Http.newHttpFileSystem(specs);
		final File httpRemote = fs.ROOT();
// httpRemote.listDirectChildren().print(bankName);

		final ResourceSpecs resSpec = this.newResourceSpecs();
		resSpec.setBankFolder(httpRemote);
		resSpec.setCachingRequired(true);
		resSpec.setCacheFolder(assets_cache_folder.child(bankName));
		resSpec.setName(bankName);

		final Resource resource = this.newResource(resSpec);

		this.installResource(resource);

	}

	@Override
	public Resource getResource (final String name) {
		return this.resources.get(name);
	}

	@Override
	public void printAllResources () {
		this.resources.print("resources");
	}

	@Override
	public void printAllIndexes () {
		for (int i = 0; i < this.resources.size(); i++) {
			final Resource resouce = this.resources.getValueAt(i);
			L.d("index of ", resouce);
			resouce.printIndex();
		}
	}

//

}
