
package com.jfixby.red.engine.core.resources;

import java.util.HashMap;

import com.jfixby.cmns.api.assets.AssetID;
import com.jfixby.cmns.api.collections.Collection;
import com.jfixby.cmns.api.collections.Collections;
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

	public void purgeAssets (final Collection<AssetID> assetsToDrop) {
		final Map<AssetsContainer, Set<AssetID>> related = Collections.newMap();
		for (int i = 0; i < assetsToDrop.size(); i++) {
			final AssetID asset = (assetsToDrop.getElementAt(i));
			final AssetsContainer handler = this.main_registry.get(asset).getContainer();
			Set<AssetID> set = related.get(handler);
			if (set == null) {
				set = Collections.newSet();
				related.put(handler, set);
			}
			set.add(asset);
		}

// related.print("related");
// related.keys().print("keys");
// final RedAssetAssetHandler key0 = related.keys().getElementAt(0);
// final RedAssetAssetHandler key1 = related.keys().getElementAt(1);
// L.d("key0 == key1", key0 == key1);
// L.d("key0 eq key1", key0.equals(key1));

		for (int i = 0; i < related.size(); i++) {
			final AssetsContainer container = related.getKeyAt(i);
			final Set<AssetID> relatedAssets = related.get(container);
// final AssetsContainer container = handler.getContainer();
			relatedAssets.print("purge: " + container);
			container.purgeAssets(relatedAssets);
		}
	}

}
