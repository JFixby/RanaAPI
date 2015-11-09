package com.jfixby.red.engine.core.resources;

import com.jfixby.cmns.api.assets.AssetID;
import com.jfixby.rana.api.asset.Asset;
import com.jfixby.rana.api.asset.AssetContainer;
import com.jfixby.rana.api.asset.AssetHandler;

public class RedAssetAssetHandler implements AssetHandler {

	private final AssetID AssetID;
	private AssetContainer AssetContainer;

	@Override
	public String toString() {
		return AssetContainer + "";
	}

	public RedAssetAssetHandler(AssetID asset_id) {
		this.AssetID = asset_id;
	}

	public void setAssetContainer(AssetContainer container) {
		this.AssetContainer = container;
	}

	@Override
	public AssetID ID() {
		return AssetID;
	}

	@Override
	public Asset asset() {
		return AssetContainer.getAsset(AssetID);
	}

	@Override
	public void printAll() {
		AssetContainer.printAll();
	}

}
