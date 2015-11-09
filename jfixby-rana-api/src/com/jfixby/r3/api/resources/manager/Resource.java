package com.jfixby.r3.api.resources.manager;

public interface Resource {

	PackageSearchResult findPackages(PackageSearchParameters search_params);

	void update();

}
