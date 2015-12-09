package com.jfixby.red.engine.core.resources;

import com.jfixby.cmns.api.assets.AssetID;
import com.jfixby.cmns.api.collections.Collection;
import com.jfixby.cmns.api.collections.Collections;
import com.jfixby.cmns.api.collections.List;
import com.jfixby.cmns.api.collections.Map;
import com.jfixby.cmns.api.log.L;

public class UserAssets {

	Map<AssetUser, List<AssetID>> reg = Collections.newMap();

	public void addAsset(AssetUser user, AssetID asset_id) {
		List<AssetID> assets_by_user = reg.get(user);
		if (assets_by_user == null) {
			assets_by_user = Collections.newList();
			reg.put(user, assets_by_user);
		}
		assets_by_user.add(asset_id);
	}

	public boolean removeAsset(AssetUser user, AssetID asset_id) {
		List<AssetID> assets_by_user = reg.get(user);
		if (assets_by_user == null) {
			L.e("There is no information about this AssetsConsumer",
					user.consumer);
			L.e("            a nd it's relationship with the asset", asset_id);
			return false;
		}
		assets_by_user.remove(asset_id);
		return true;
	}

	public void print() {
		reg.print("assets used");
	}

	public Collection<AssetID> listAssetsUsedBy(AssetUser consumer) {
		return reg.get(consumer);
	}
}
