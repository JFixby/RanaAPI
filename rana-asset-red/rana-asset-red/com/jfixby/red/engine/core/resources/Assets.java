
package com.jfixby.red.engine.core.resources;

import java.util.HashMap;

import com.jfixby.cmns.api.assets.AssetID;
import com.jfixby.cmns.api.collections.Collections;

public class Assets {

	final HashMap<AssetID, RedAssetAssetHandler> main_registry = new HashMap<AssetID, RedAssetAssetHandler>();

	final public void put (final AssetID asset_id, final RedAssetAssetHandler info) {
		this.main_registry.put(asset_id, info);
	}

	final public RedAssetAssetHandler get (final AssetID asset_id) {
		return this.main_registry.get(asset_id);
	}

	final public void print (final String tag) {
		Collections.newMap(this.main_registry).print(tag);
	}

}
