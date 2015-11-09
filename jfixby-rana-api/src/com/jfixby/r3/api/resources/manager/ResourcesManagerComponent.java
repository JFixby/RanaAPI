package com.jfixby.r3.api.resources.manager;


public interface ResourcesManagerComponent {

	PackageSearchParameters newSearchParameters();

	PackageSearchResult findPackages(PackageSearchParameters search_params);

	void updateAll();

	void printAllPackages();



	

}
