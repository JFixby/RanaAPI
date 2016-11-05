
package com.jfixby.red.engine.core.resources;

import com.jfixby.cmns.api.assets.AssetID;
import com.jfixby.rana.api.asset.Asset;
import com.jfixby.rana.api.asset.AssetHandler;
import com.jfixby.rana.api.asset.SealedAssetsContainer;

public class RedAssetHandler implements AssetHandler {

	private final AssetID id;
	private SealedAssetsContainer assetsContainer;

	@Override
	public String toString () {
		return "#[" + this.assetsContainer + "]";
	}

	public RedAssetHandler (final AssetID asset_id) {
		this.id = asset_id;
	}

	public void setAssetContainer (final SealedAssetsContainer container) {
		this.assetsContainer = container;
	}

	@Override
	public AssetID ID () {
		return this.id;
	}

	@Override
	public Asset asset () {
		return this.assetsContainer.getAsset(this.id);
	}

	@Override
	public void printAll () {
		this.assetsContainer.printAll();
	}

	@Override
	public long readPackageTimeStamp () {
		return this.assetsContainer.readPackageTimeStamp();
	}

	public SealedAssetsContainer getContainer () {
		return this.assetsContainer;
	}

}
