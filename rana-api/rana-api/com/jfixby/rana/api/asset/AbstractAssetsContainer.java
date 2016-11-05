
package com.jfixby.rana.api.asset;

import com.jfixby.cmns.api.assets.AssetID;
import com.jfixby.cmns.api.collections.Collection;

public interface AbstractAssetsContainer {

	Collection<AssetID> listAssets ();

	Asset getAsset (AssetID asset_id);

	void printAll ();

	long getPackageTimeStamp ();

	long readPackageTimeStamp ();

	boolean purgeAssets (Collection<AssetID> relatedAssets);

}
