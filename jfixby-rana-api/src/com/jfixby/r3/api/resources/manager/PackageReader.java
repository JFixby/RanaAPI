package com.jfixby.r3.api.resources.manager;

import java.io.IOException;

import com.jfixby.cmns.api.collections.Collection;

public interface PackageReader {

	Collection<PackageFormat> listAcceptablePackageFormats();

	void doReadPackage(PackageInput input) throws IOException;

}
