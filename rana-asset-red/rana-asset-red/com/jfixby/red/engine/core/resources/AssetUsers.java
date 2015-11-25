package com.jfixby.red.engine.core.resources;

import com.jfixby.cmns.api.assets.AssetID;
import com.jfixby.cmns.api.collections.List;
import com.jfixby.cmns.api.collections.Map;
import com.jfixby.cmns.api.log.L;
import com.jfixby.cmns.api.util.JUtils;

public class AssetUsers {

	Map<AssetID, List<AssetUser>> asset_users = JUtils.newMap();

	public void print(String tag) {
		asset_users.print(tag);
	}

	public void addUser(AssetID asset_id, AssetUser user) {
		List<AssetUser> users = asset_users.get(asset_id);
		if (users == null) {
			users = JUtils.newList();
			asset_users.put(asset_id, users);
		}
		users.add(user);
	}

	public boolean removeUser(AssetID asset_id, AssetUser user) {
		List<AssetUser> users = asset_users.get(asset_id);
		if (users == null) {
			L.e("There is no information about this asset usage", asset_id);
			L.e("     and especially it's relationship with the", user.consumer);
			return false;
		}
		users.remove(user);
		return true;
	}

}
