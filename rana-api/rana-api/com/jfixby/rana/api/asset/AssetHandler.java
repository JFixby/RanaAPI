package com.jfixby.rana.api.asset;

import com.jfixby.cmns.api.assets.AssetID;

public interface AssetHandler {

	Asset asset();

	AssetID ID();

	void printAll();

}
