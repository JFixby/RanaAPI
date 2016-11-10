
package com.jfixby.rana.api.pkg;

import com.jfixby.cmns.api.assets.AssetID;
import com.jfixby.cmns.api.collections.Collection;
import com.jfixby.rana.api.asset.SealedAssetsContainer;

public interface PackageHandler {

	public String getPackageName ();

	public PackageVersion getVersion ();

	public PackageFormat getFormat ();

	public PACKAGE_STATUS getStatus ();

	public Collection<AssetID> listPackedAssets ();

	public Collection<AssetID> listDependencies ();

	public void print ();

	public void install ();

	public SealedAssetsContainer doReadPackage (PackageReaderListener reader_listener, PackageReader reader);

	public boolean isLoaded ();

}
