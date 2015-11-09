package com.jfixby.r3.api.assets;

import com.jfixby.cmns.api.assets.AssetID;

public interface AssetContainer<T> {

	T getAsset(AssetID asset_id);

	void printAll();

	void checkAll();

}
