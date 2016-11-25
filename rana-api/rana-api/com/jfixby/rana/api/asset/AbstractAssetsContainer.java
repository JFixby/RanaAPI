
package com.jfixby.rana.api.asset;

import com.jfixby.cmns.api.assets.ID;
import com.jfixby.cmns.api.collections.Collection;

public interface AbstractAssetsContainer {

	Collection<ID> listAssets ();

	Asset getAsset (ID asset_id);

	void printAll ();

	long getPackageTimeStamp ();

	long readPackageTimeStamp ();

	boolean purgeAssets (Collection<ID> relatedAssets);

}
