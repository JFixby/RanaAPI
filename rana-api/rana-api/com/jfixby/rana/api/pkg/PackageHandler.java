
package com.jfixby.rana.api.pkg;

import com.jfixby.cmns.api.assets.AssetID;
import com.jfixby.cmns.api.collections.Collection;

public interface PackageHandler {

	public String getPackageName ();

	public PackageVersion getVersion ();

	public PackageFormat getFormat ();

	public PACKAGE_STATUS getStatus ();

	public Collection<AssetID> listPackedAssets ();

	public Collection<AssetID> listDependencies ();

	public void print ();

	public void install ();

	public void readPackage (PackageReaderListener reader_listener, PackageReader reader);

}
