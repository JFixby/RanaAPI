
package com.jfixby.rana.api.pkg;

import com.jfixby.cmns.api.file.File;
import com.jfixby.rana.api.asset.AssetsContainer;

public interface PackageReaderInput {

	PackageReaderListener getPackageReaderListener ();

	File getRootFile ();

	PackageHandler getPackageHandler ();

	AssetsContainer getStorage ();

}
