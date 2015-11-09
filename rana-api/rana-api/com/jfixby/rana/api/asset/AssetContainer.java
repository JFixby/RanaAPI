package com.jfixby.rana.api.asset;

import com.jfixby.cmns.api.assets.AssetID;

public interface AssetContainer {

	Asset getAsset(AssetID asset_id);

	void printAll();

	void checkAll();

}
