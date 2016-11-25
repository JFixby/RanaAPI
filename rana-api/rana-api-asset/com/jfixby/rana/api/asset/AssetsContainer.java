
package com.jfixby.rana.api.asset;

import com.jfixby.cmns.api.assets.ID;

public interface AssetsContainer extends AbstractAssetsContainer {

	void addAsset (ID assetID, Asset data);

}
