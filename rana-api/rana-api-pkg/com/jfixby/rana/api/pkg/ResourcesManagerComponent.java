package com.jfixby.rana.api.pkg;


public interface ResourcesManagerComponent {

	PackageSearchParameters newSearchParameters();

	PackageSearchResult findPackages(PackageSearchParameters search_params);

	void updateAll();

	void printAllPackages();

}
