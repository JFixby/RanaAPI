
package com.jfixby.rana.api.asset;

import com.jfixby.cmns.api.assets.AssetID;

public interface AssetsContainer extends AbstractAssetsContainer {

	void addAsset (AssetID raster_id, Asset data);

	public void seal ();

}
