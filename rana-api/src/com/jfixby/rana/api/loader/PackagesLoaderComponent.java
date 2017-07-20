
package com.jfixby.rana.api.loader;

import com.jfixby.rana.api.format.PackageFormat;
import com.jfixby.scarabei.api.collections.Collection;

public interface PackagesLoaderComponent {
	Collection<PackageLoader> findPackageReaders (PackageFormat format);

	void registerPackageReader (PackageLoader reader);

	Collection<PackageFormat> listAcceptablePackageFormats ();

	void printInstalledPackageReaders ();
}
