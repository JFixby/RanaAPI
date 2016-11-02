
package com.jfixby.red.engine.core.resources;

import com.jfixby.cmns.api.assets.AssetID;
import com.jfixby.rana.api.asset.Asset;
import com.jfixby.rana.api.asset.AssetContainer;
import com.jfixby.rana.api.asset.AssetHandler;

public class RedAssetAssetHandler implements AssetHandler {

	private final AssetID AssetID;
	private AssetContainer AssetContainer;

	@Override
	public String toString () {
		return this.AssetContainer + "";
	}

	public RedAssetAssetHandler (final AssetID asset_id) {
		this.AssetID = asset_id;
	}

	public void setAssetContainer (final AssetContainer container) {
		this.AssetContainer = container;
	}

	@Override
	public AssetID ID () {
		return this.AssetID;
	}

	@Override
	public Asset asset () {
		return this.AssetContainer.getAsset(this.AssetID);
	}

	@Override
	public void printAll () {
		this.AssetContainer.printAll();
	}

	@Override
	public long readPackageTimeStamp () {
		return this.AssetContainer.readPackageTimeStamp(this.AssetID);
	}

}
