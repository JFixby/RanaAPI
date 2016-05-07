
package com.jfixby.rana.api.asset;

import com.jfixby.cmns.api.assets.AssetID;

public interface AssetHandler {

	<T extends Asset> T asset ();

	AssetID ID ();

	void printAll ();

}
