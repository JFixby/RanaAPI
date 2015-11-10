package com.jfixby.rana.api.pkg;

import com.jfixby.cmns.api.collections.Collection;


public interface ResourcesManagerComponent {

	PackageSearchParameters newSearchParameters();

	PackageSearchResult findPackages(PackageSearchParameters search_params);

	void updateAll();

	void printAllPackages();
	
	

	PackageFormat newPackageFormat(String format_name);
	
	Collection<PackageReader> findPackageReaders(PackageFormat format);

	void registerPackageReader(PackageReader loader);

	Collection<PackageFormat> listAcceptablePackageFormats();

	void printInstalledPackageReaders();

}
