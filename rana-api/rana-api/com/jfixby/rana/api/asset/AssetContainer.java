
package com.jfixby.rana.api.asset;

import com.jfixby.cmns.api.assets.AssetID;

public interface AssetContainer {

	Asset getAsset (AssetID asset_id);

	void printAll ();

	void checkAll ();

	long getPackageTimeStamp (AssetID assetID);

	long readPackageTimeStamp (AssetID assetID);

}
