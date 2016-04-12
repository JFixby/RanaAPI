
package com.jfixby.red.engine.core.resources;

import java.util.ArrayList;

import com.jfixby.cmns.api.assets.AssetID;
import com.jfixby.cmns.api.collections.Collection;
import com.jfixby.cmns.api.collections.Collections;
import com.jfixby.cmns.api.debug.Debug;
import com.jfixby.cmns.api.debug.DebugTimer;
import com.jfixby.cmns.api.err.Err;
import com.jfixby.cmns.api.log.L;
import com.jfixby.cmns.api.sys.settings.SystemSettings;
import com.jfixby.rana.api.asset.AssetContainer;
import com.jfixby.rana.api.asset.AssetHandler;
import com.jfixby.rana.api.asset.AssetsConsumer;
import com.jfixby.rana.api.asset.AssetsManager;
import com.jfixby.rana.api.asset.AssetsManagerComponent;
import com.jfixby.rana.api.asset.AssetsManagerFlags;
import com.jfixby.rana.api.pkg.PACKAGE_STATUS;
import com.jfixby.rana.api.pkg.PackageFormat;
import com.jfixby.rana.api.pkg.PackageHandler;
import com.jfixby.rana.api.pkg.PackageReader;
import com.jfixby.rana.api.pkg.PackageSearchParameters;
import com.jfixby.rana.api.pkg.PackageSearchResult;
import com.jfixby.rana.api.pkg.ResourcesManager;

public class RedAssetsManager implements AssetsManagerComponent, AssetsConsumer {

	final Assets assets = new Assets();
	final ArrayList<AssetContainer> containers = new ArrayList<AssetContainer>();;

	final AssetUsers asset_users = new AssetUsers();
	final UserAssets user_assets = new UserAssets();
	int i = 0;

	@Override
	public void checkAll () {
		for (this.i = 0; this.i < this.containers.size(); this.i++) {
			final AssetContainer container = this.containers.get(this.i);
			container.checkAll();
		}
	}

	@Override
	public void registerAssetContainer (final AssetID asset_id, final AssetContainer container) {
		Debug.checkNull("asset_id", asset_id);
		Debug.checkNull("container", container);

		this.containers.add(container);

		final RedAssetAssetHandler info = new RedAssetAssetHandler(asset_id);
		this.assets.put(asset_id, info);
		info.setAssetContainer(container);

	}

	@Override
	public AssetHandler useAsset (final AssetID asset_id) {
		return this.assets.get(asset_id);
	}

	@Override
	public AssetHandler obtainAsset (final AssetID asset_id, final AssetsConsumer consumer) {
		Debug.checkNull("asset_id", asset_id);
		Debug.checkNull("consumer", consumer);
		final RedAssetAssetHandler asset = this.assets.get(asset_id);
		if (asset == null) {
			// assets.print("available assets");
			L.e("Asset not found <" + asset_id + "> (https://github.com/JFixby/RedTriplane/issues/7)");
			return null;
		} else {
			final AssetUser user = new AssetUser(consumer);
			this.asset_users.addUser(asset_id, user);
			this.user_assets.addAsset(user, asset_id);
			// asset_users.print("on obtainAsset");
			return asset;
		}
	}

	@Override
	public void releaseAsset (final AssetHandler asset_info, final AssetsConsumer consumer) {
		Debug.checkNull("asset_info", asset_info);
		final AssetID asset_id = asset_info.ID();
		Debug.checkNull("asset_id", asset_id);
		Debug.checkNull("consumer", consumer);
		final RedAssetAssetHandler asset = this.assets.get(asset_id);
		if (asset == null) {
			throw new Error("Asset " + asset_id + " is not registred here");
		}

		final AssetUser user = new AssetUser(consumer);
		final boolean succ_a = this.asset_users.removeUser(asset_id, user);
		final boolean succ_u = this.user_assets.removeAsset(user, asset_id);
		if (succ_a != succ_u) {
			this.asset_users.print("on releaseAsset");
			this.user_assets.print();
			throw new Error("Assets usage register is corrupted");
		}
		// asset_users.print("on releaseAsset");

	}

	@Override
	public void releaseAllAssets (final AssetsConsumer consumer) {
		Debug.checkNull("consumer", consumer);
		final AssetUser user = new AssetUser(consumer);
		final Collection<AssetID> assets_list = Collections.newList(this.user_assets.listAssetsUsedBy(user));
		if (assets_list == null) {
			L.d("AssetsConsumer: " + user.consumer, "is not using any assets");
			this.user_assets.print();
			this.asset_users.print("on releaseAllAssets");
			return;
		}
		for (final AssetID asset_id : assets_list) {
			final RedAssetAssetHandler asset_info = this.assets.get(asset_id);
			if (asset_info == null) {
				this.asset_users.print("on releaseAllAssets");
				this.user_assets.print();
				throw new Error("Assets usage register is corrupted. Asset <" + asset_id + "> not found.");
			}
			this.releaseAsset(asset_info, consumer);
		}

	}

	@Override
	public void autoResolveAssets (final Collection<AssetID> dependencies) {
		Debug.checkNull("dependencies", dependencies);
		boolean updated = true;
		for (final AssetID dependency : dependencies) {

			final AssetHandler asset_entry = AssetsManager.obtainAsset(dependency, this);
			if (asset_entry != null) {
				L.d("already loaded", dependency);
				AssetsManager.releaseAsset(asset_entry, this);
				continue;
			}
			if (!updated) {
				// ResourcesManager.updateAll();
				updated = true;
			}
			this.resolve(dependency, true);
		}
	}

	private boolean resolve (final AssetID dependency, final boolean print_debug_output) {
		L.d("RESOLVING DEPENDENCY", dependency);
		final PackageSearchParameters search_params = ResourcesManager.newSearchParameters();
		search_params.setAssetId(dependency);

		final PackageSearchResult search_result = ResourcesManager.findPackages(search_params);
		if (print_debug_output) {
			// search_result.print();

			// this.printAllLoadedAssets();
			// L.d();
		}
		if (search_result.isEmpty()) {
			final String msg = "Asset [" + dependency + "] was not found in any package.";

			ResourcesManager.printAllPackages();
			L.d(msg);
			Err.reportError(msg);
			//
		}

		final PackageHandler package_handler = search_result.getBest();
		// package_handler.print();

		final PACKAGE_STATUS package_status = package_handler.getStatus();
		if (PACKAGE_STATUS.NOT_INSTALLED == package_status) {
			package_handler.install();
		}

		final PackageFormat format = package_handler.getFormat();
		final Collection<PackageReader> package_loaders = ResourcesManager.findPackageReaders(format);
		if (package_loaders.isEmpty()) {
			ResourcesManager.printAllPackageReaders();
			L.e("Failed to read package", package_handler);
			throw new Error("No package reader for " + format);
			//
		}

		final PackageReader package_reader = package_loaders.getLast();
		final DebugTimer debigTimer = Debug.newTimer();
		debigTimer.reset();
		package_handler.readPackage(null, package_reader);
		debigTimer.printTimeAbove(50L, "LOAD-TIME: Asset[" + dependency + "] loaded");

		return true;
	}

	@Override
	public void printAllLoadedAssets () {
		this.assets.print("all loaded assets");
	}

	@Override
	public boolean autoResolveAsset (final AssetID dependency) {
		final AssetHandler asset_entry = AssetsManager.obtainAsset(dependency, this);
		if (asset_entry != null) {
			AssetsManager.releaseAsset(asset_entry, this);
			return true;
		}
		L.e("Asset[" + dependency + "] delays loading since it is not pre-loaded.");
		// ResourcesManager.updateAll();
		final boolean success = this.resolve(dependency, true);
		if (!success) {
			L.e("Asset[" + dependency + "] was not resolved!");
		}
		return success;
	}

	@Override
	public boolean autoResolveAssets () {
		return SystemSettings.getFlag(AssetsManagerFlags.AutoresolveDependencies);
	}

}
