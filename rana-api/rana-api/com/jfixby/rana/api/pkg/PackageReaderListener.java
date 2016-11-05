
package com.jfixby.rana.api.pkg;

import java.io.IOException;

import com.jfixby.cmns.api.assets.AssetID;
import com.jfixby.cmns.api.collections.Collection;
import com.jfixby.rana.api.asset.SealedAssetsContainer;

public interface PackageReaderListener {

	void onError (IOException e);

	void onDependenciesRequired (PackageHandler requiredBy, Collection<AssetID> dependencies);

	void onPackageDataDispose (final SealedAssetsContainer data);

	void onPackageDataLoaded (final SealedAssetsContainer data);

}
