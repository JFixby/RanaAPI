
package com.jfixby.rana.api.asset;

import com.jfixby.cmns.api.assets.AssetID;
import com.jfixby.cmns.api.collections.Collection;

public interface AssetsContainer {

	Asset getAsset (AssetID asset_id);

	void printAll ();

	void checkAll ();

	long getPackageTimeStamp (AssetID assetID);

	long readPackageTimeStamp (AssetID assetID);

	void purgeAssets (Collection<AssetID> relatedAssets);

}
