package com.jfixby.red.triplane.resources.fsbased;

import com.jfixby.cmns.api.collections.JUtils;
import com.jfixby.cmns.api.collections.List;
import com.jfixby.r3.api.resources.manager.PackageSearchParameters;
import com.jfixby.r3.api.resources.manager.PackageSearchResult;
import com.jfixby.r3.api.resources.manager.Resource;
import com.jfixby.r3.api.resources.manager.ResourcesManagerComponent;

public class RedResourcesManager implements ResourcesManagerComponent {

	final List<Resource> resources = JUtils.newList();

	@Override
	public PackageSearchParameters newSearchParameters() {
		return new RedPackageSearchParameters();
	}

	@Override
	public PackageSearchResult findPackages(
			PackageSearchParameters search_params) {
		RedPackageSearchResult result = new RedPackageSearchResult(
				search_params);
		JUtils.checkNull("search_params", search_params);
		for (int i = 0; i < resources.size(); i++) {
			PackageSearchResult result_i = resources.getElementAt(i)
					.findPackages(search_params);
			result.add(result_i);
		}

		return result;
	}

	public void installResource(Resource resource_to_install) {
		JUtils.checkNull("resource_to_install", resource_to_install);
		if (resources.contains(resource_to_install)) {
			throw new Error("Resource is already installed: "
					+ resource_to_install);
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

}
