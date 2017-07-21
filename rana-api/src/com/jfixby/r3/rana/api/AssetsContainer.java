
package com.jfixby.r3.rana.api;

import com.jfixby.scarabei.api.assets.ID;

public interface AssetsContainer extends AbstractAssetsContainer {

	void addAsset (ID assetID, Asset data);

	public SealedAssetsContainer seal ();

}
