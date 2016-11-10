
package com.jfixby.rana.api.pkg;

import java.io.IOException;

import com.jfixby.cmns.api.collections.Collection;
import com.jfixby.cmns.api.file.File;

public interface ResourcesManagerComponent {

	PackageSearchParameters newSearchParameters ();

	PackageSearchResult findPackages (PackageSearchParameters search_params);

	void printAllPackages ();

	PackageFormat newPackageFormat (String format_name);

	Collection<PackageReader> findPackageReaders (PackageFormat format);

	void registerPackageReader (PackageReader loader);

	Collection<PackageFormat> listAcceptablePackageFormats ();

	void printInstalledPackageReaders ();

	ResourceSpecs newResourceSpecs ();

	Resource newResource (ResourceSpecs specs) throws IOException;

	void updateAll (ResourceRebuildIndexListener listener);

	void installResources (Collection<Resource> resources);

	Collection<Resource> findResources (File assets_folder) throws IOException;

}
