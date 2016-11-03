
package com.jfixby.red.engine.core.resources;

import com.jfixby.cmns.api.assets.AssetID;
import com.jfixby.cmns.api.collections.Collection;
import com.jfixby.cmns.api.collections.Collections;
import com.jfixby.cmns.api.collections.List;
import com.jfixby.cmns.api.collections.Map;
import com.jfixby.cmns.api.log.L;

public class UserAssets {

	Map<AssetUser, List<AssetID>> reg = Collections.newMap();

	public void addAsset (final AssetUser user, final AssetID asset_id) {
		List<AssetID> assets_by_user = this.reg.get(user);
		if (assets_by_user == null) {
			assets_by_user = Collections.newList();
			this.reg.put(user, assets_by_user);
		}
		assets_by_user.add(asset_id);
	}

	public boolean removeAsset (final AssetUser user, final AssetID asset_id) {
		final List<AssetID> assets_by_user = this.reg.get(user);
		if (assets_by_user == null) {
			L.e("There is no information about this AssetsConsumer", user.consumer);
			L.e("            a nd it's relationship with the asset", asset_id);
			return false;
		}
		assets_by_user.remove(asset_id);
		if (assets_by_user.size() == 0) {
			this.reg.remove(user);
		}
		return true;
	}

	public void print () {
		this.reg.print("assets used");
	}

	public Collection<AssetID> listAssetsUsedBy (final AssetUser consumer) {
		return this.reg.get(consumer);
	}
}
