package com.jfixby.r3.api.assets;

import com.jfixby.cmns.api.assets.AssetID;

public interface AssetHandler<T extends Asset> {

	T asset();

	AssetID ID();

	void printAll();

}
