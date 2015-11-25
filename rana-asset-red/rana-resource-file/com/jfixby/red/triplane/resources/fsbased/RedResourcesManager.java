package com.jfixby.red.triplane.resources.fsbased;

import com.jfixby.cmns.api.collections.Collection;
import com.jfixby.cmns.api.collections.List;
import com.jfixby.cmns.api.collections.Map;
import com.jfixby.cmns.api.log.L;
import com.jfixby.cmns.api.util.JUtils;
import com.jfixby.rana.api.pkg.PackageFormat;
import com.jfixby.rana.api.pkg.PackageReader;
import com.jfixby.rana.api.pkg.PackageSearchParameters;
import com.jfixby.rana.api.pkg.PackageSearchResult;
import com.jfixby.rana.api.pkg.Resource;
import com.jfixby.rana.api.pkg.ResourcesManagerComponent;

public class RedResourcesManager implements ResourcesManagerComponent {

	final List<Resource> resources = JUtils.newList();

	@Override
	public PackageSearchParameters newSearchParameters() {
		return new RedPackageSearchParameters();
	}

	@Override
	public PackageSearchResult findPackages(PackageSearchParameters search_params) {
		RedPackageSearchResult result = new RedPackageSearchResult(search_params);
		JUtils.checkNull("search_params", search_params);
		for (int i = 0; i < resources.size(); i++) {
			PackageSearchResult result_i = resources.getElementAt(i).findPackages(search_params);
			result.add(result_i);
		}

		return result;
	}

	public void installResource(Resource resource_to_install) {
		JUtils.checkNull("resource_to_install", resource_to_install);
		if (resources.contains(resource_to_install)) {
			throw new Error("Resource is already installed: " + resource_to_install);
		}
		this.resources.add(resource_to_install);
	}

	public void removeResource(Resource resource) {
		this.resources.remove(resource);
	}

	@Override
	public void updateAll() {
		for (int i = 0; i < resources.size(); i++) {
			resources.getElementAt(i).update();
		}
	}

	@Override
	public void printAllPackages() {
		resources.print("All installed resources");
		PackageSearchParameters search_params = this.newSearchParameters();
		search_params.setGetAllAssetsFlag(true);
		PackageSearchResult packages = this.findPackages(search_params);
		packages.list().print("All available packages");
		;
	}

	@Override
	public Collection<PackageReader> findPackageReaders(PackageFormat format) {
		List<PackageReader> list = loaders_by_format.get(format);
		if (list == null) {
			// L.d("format", format);
			// loaders_by_format.print("acceptable formats");
			return JUtils.newList();
		}
		return list;
	}

	@Override
	public void registerPackageReader(PackageReader loader) {
		if (loaders.contains(loader)) {
			L.d("Loader is already registred:" + loader);
			return;
		}

		Collection<PackageFormat> can_read = loader.listAcceptablePackageFormats();
		JUtils.checkNull("PackageReader.listAcceptablePackageFormats()", can_read);
		loaders.add(loader);
		for (int i = 0; i < can_read.size(); i++) {
			PackageFormat format = can_read.getElementAt(i);
			List<PackageReader> loaders_list = this.loaders_by_format.get(format);
			if (loaders_list == null) {
				loaders_list = JUtils.newList();
				loaders_by_format.put(format, loaders_list);
			}
			loaders_list.add(loader);
		}

	}

	final Map<PackageFormat, List<PackageReader>> loaders_by_format = JUtils.newMap();
	final List<PackageReader> loaders = JUtils.newList();

	@Override
	public Collection<PackageFormat> listAcceptablePackageFormats() {
		return this.loaders_by_format.keys();
	}

	@Override
	public void printInstalledPackageReaders() {
		this.loaders_by_format.print("Installed package readers");
	}

	@Override
	public PackageFormat newPackageFormat(String format_name) {
		return new PackageFormatImpl(format_name);
	}

}
