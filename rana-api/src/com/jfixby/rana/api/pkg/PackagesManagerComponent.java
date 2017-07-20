
package com.jfixby.rana.api.pkg;

import java.io.IOException;

import com.jfixby.scarabei.api.assets.ID;
import com.jfixby.scarabei.api.collections.Collection;
import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.net.http.HttpURL;

public interface PackagesManagerComponent {

// PackageSearchParameters newSearchParameters ();

	PackageSearchResult findPackages (PackageSearchParameters search_params);

	void printAllPackages ();

// PackageFormat newPackageFormat (String format_name);

// Collection<PackageReader> findPackageReaders (PackageFormat format);
//
// void registerPackageReader (PackageReader loader);

// Collection<PackageFormat> listAcceptablePackageFormats ();

// void printInstalledPackageReaders ();

	AssetsTankSpecs newResourceSpecs ();

	PackagesTank newResource (AssetsTankSpecs specs) throws IOException;

// void updateAll (ResourceRebuildIndexListener listener);

	Collection<PackagesBank> findBanks (File assets_folder) throws IOException;

	void printAllResources ();

	void printAllIndexes ();

	PackagesBank getBank (ID name);

	void installBanks (Collection<PackagesBank> resources);

	Collection<PackagesBank> findAndInstallResources (File assets_folder) throws IOException;

	PackagesBank installRemoteBank (final HttpURL bankUrl, final File assets_cache_folder, final Iterable<String> tanks)
		throws IOException;

	DeployRemoteBanksTask prepareDeployRemoteBanksTask ();

}
