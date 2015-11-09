package com.jfixby.rana.api.pkg;

import java.io.IOException;

import com.jfixby.cmns.api.collections.Collection;

public interface PackageReader {

	Collection<PackageFormat> listAcceptablePackageFormats();

	void doReadPackage(PackageInput input) throws IOException;

}
