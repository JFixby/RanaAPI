
package com.jfixby.red.engine.core.resources;

import java.util.HashMap;

import com.jfixby.cmns.api.assets.AssetID;
import com.jfixby.cmns.api.collections.Collections;
import com.jfixby.cmns.api.collections.List;
import com.jfixby.cmns.api.collections.Map;
import com.jfixby.cmns.api.collections.Set;
import com.jfixby.cmns.api.debug.Debug;
import com.jfixby.rana.api.asset.AssetsContainer;

public class Assets {

	final HashMap<AssetID, RedAssetAssetHandler> main_registry = new HashMap<AssetID, RedAssetAssetHandler>();

	final public void put (final AssetID asset_id, final RedAssetAssetHandler info) {
		this.main_registry.put(asset_id, info);
	}

	public void remove (final AssetID asset, final AssetsContainer container) {
		final RedAssetAssetHandler handler = this.main_registry.get(asset);
		Debug.checkTrue(handler.getContainer() == container);
		this.main_registry.remove(asset);
	}

	final public RedAssetAssetHandler get (final AssetID asset_id) {
		return this.main_registry.get(asset_id);
	}

	final public void print (final String tag) {
		Collections.newMap(this.main_registry).print(tag);
	}

	public void purgeAssets (final List<AssetID> assetsToDrop) {
		final Map<RedAssetAssetHandler, Set<AssetID>> related = Collections.newMap();
		for (int i = 0; i < assetsToDrop.size(); i++) {
			final AssetID asset = (assetsToDrop.getElementAt(i));
			final RedAssetAssetHandler handler = this.main_registry.get(asset);
			Set<AssetID> set = related.get(handler);
			if (set == null) {
				set = Collections.newSet();
				related.put(handler, set);
			}
			set.add(asset);
		}

		for (int i = 0; i < related.size(); i++) {
			final RedAssetAssetHandler handler = related.getKeyAt(i);
			final Set<AssetID> relatedAssets = related.get(handler);
			final AssetsContainer container = handler.getContainer();
			container.purgeAssets(relatedAssets);
		}
	}

}
