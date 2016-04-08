
package com.jfixby.red.engine.core.resources;

import com.jfixby.cmns.api.assets.AssetID;
import com.jfixby.cmns.api.collections.Collections;
import com.jfixby.cmns.api.collections.List;
import com.jfixby.cmns.api.collections.Map;
import com.jfixby.cmns.api.err.Err;
import com.jfixby.cmns.api.log.L;

public class AssetUsers {

	final Map<AssetID, List<AssetUser>> asset_users = Collections.newMap();

	public void print (String tag) {
		asset_users.print(tag);
	}

	public void addUser (final AssetID asset_id, final AssetUser user) {
		List<AssetUser> users = asset_users.get(asset_id);
		if (users == null) {
			users = Collections.newList();
			asset_users.put(asset_id, users);
		}
		users.add(user);
	}

	public boolean removeUser (AssetID asset_id, AssetUser user) {
		List<AssetUser> users = asset_users.get(asset_id);
		if (users == null) {
			L.e("There is no information about this asset usage", asset_id);
			L.e("     and especially it's relationship with the", user.consumer);
			return false;
		}
		users.remove(user);
		if (users.size() == 0) {
			Err.reportWarning("Unused asset: " + asset_id);
		}
		return true;
	}

}
