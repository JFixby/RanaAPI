package com.jfixby.rana.api.pkg;

import com.jfixby.cmns.api.filesystem.File;

public interface PackageInput {

	PackageReaderListener getPackageReaderListener();

	File getRootFile();

	PackageHandler getPackageHandler();

}
