
package com.jfixby.rana.api.asset;

import com.jfixby.cmns.api.assets.ID;

public interface AssetHandler {

	<T extends Asset> T asset ();

	ID ID ();

	void printAll ();

	long readPackageTimeStamp ();

}
