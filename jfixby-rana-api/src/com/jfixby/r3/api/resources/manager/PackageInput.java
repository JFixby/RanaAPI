package com.jfixby.r3.api.resources.manager;

import com.jfixby.cmns.api.filesystem.File;

public interface PackageInput {

	PackageReaderListener getPackageReaderListener();

	PACKAGE_PRIORITY getPriority();

	File getRootFile();

	PackageHandler getPackageHandler();

//	Object requestFeature(String string);

}
