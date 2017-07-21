
package com.jfixby.r3.rana.api.loader;

import java.io.IOException;

import com.jfixby.r3.rana.api.format.PackageFormat;
import com.jfixby.scarabei.api.collections.Collection;

public interface PackageLoader {
	Collection<PackageFormat> listAcceptablePackageFormats ();

	void doReadPackage (PackageReaderInput input) throws IOException;
}
