package com.jfixby.red.engine.core.resources;

import com.jfixby.cmns.api.assets.AssetID;
import com.jfixby.cmns.api.collections.Collection;
import com.jfixby.cmns.api.collections.List;
import com.jfixby.cmns.api.collections.Map;
import com.jfixby.cmns.api.log.L;
import com.jfixby.cmns.api.sys.Sys;
import com.jfixby.cmns.api.util.JUtils;
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

	final Map<AssetID, RedAssetAssetHandler> assets = JUtils.newMap();
	final List<AssetContainer> containers = JUtils.newList();

	final AssetUsers asset_users = new AssetUsers();
	final UserAssets user_assets = new UserAssets();

	@Override
	public void checkAll() {
		for (AssetContainer container : containers) {
			container.checkAll();
		}
	}

	@Override
	public void registerAssetContainer(AssetID asset_id, AssetContainer container) {
		JUtils.checkNull("asset_id", asset_id);
		JUtils.checkNull("container", container);

		containers.add(container);

		final RedAssetAssetHandler info = new RedAssetAssetHandler(asset_id);
		assets.put(asset_id, info);
		info.setAssetContainer(container);

	}

	@Override
	public AssetHandler obtainAsset(AssetID asset_id, AssetsConsumer consumer) {
		JUtils.checkNull("asset_id", asset_id);
		JUtils.checkNull("consumer", consumer);
		RedAssetAssetHandler asset = assets.get(asset_id);
		if (asset == null) {
			// assets.print("available assets");
			L.e("Asset not found <" + asset_id + ">");
			return null;
		} else {

			AssetUser user = new AssetUser(consumer);
			asset_users.addUser(asset_id, user);
			user_assets.addAsset(user, asset_id);
			// asset_users.print("on obtainAsset");
			return asset;
		}
	}

	@Override
	public void releaseAsset(AssetHandler asset_info, AssetsConsumer consumer) {
		JUtils.checkNull("asset_info", asset_info);
		AssetID asset_id = asset_info.ID();
		JUtils.checkNull("asset_id", asset_id);
		JUtils.checkNull("consumer", consumer);
		RedAssetAssetHandler asset = assets.get(asset_id);
		if (asset == null) {
			throw new Error("Asset " + asset_id + " is not registred here");
		}

		AssetUser user = new AssetUser(consumer);
		boolean succ_a = asset_users.removeUser(asset_id, user);
		boolean succ_u = user_assets.removeAsset(user, asset_id);
		if (succ_a != succ_u) {
			asset_users.print("on releaseAsset");
			user_assets.print();
			throw new Error("Assets usage register is corrupted");
		}
		// asset_users.print("on releaseAsset");

	}

	@Override
	public void releaseAllAssets(AssetsConsumer consumer) {
		JUtils.checkNull("consumer", consumer);
		AssetUser user = new AssetUser(consumer);
		Collection<AssetID> assets_list = JUtils.newList(user_assets.listAssetsUsedBy(user));
		if (assets_list == null) {
			L.d("AssetsConsumer: " + user.consumer, "is not using any assets");
			user_assets.print();
			asset_users.print("on releaseAllAssets");
			return;
		}
		for (AssetID asset_id : assets_list) {
			RedAssetAssetHandler asset_info = assets.get(asset_id);
			if (asset_info == null) {
				asset_users.print("on releaseAllAssets");
				user_assets.print();
				throw new Error("Assets usage register is corrupted. Asset <" + asset_id + "> not found.");
			}
			this.releaseAsset(asset_info, consumer);
		}

	}

	@Override
	public void autoResolveAssets(Collection<AssetID> dependencies) {
		JUtils.checkNull("dependencies", dependencies);
		boolean updated = false;
		for (AssetID dependency : dependencies) {

			AssetHandler asset_entry = AssetsManager.obtainAsset(dependency, this);
			if (asset_entry != null) {
				L.d("already loaded", dependency);
				AssetsManager.releaseAsset(asset_entry, this);
				continue;
			}
			if (!updated) {
				ResourcesManager.updateAll();
				updated = true;
			}
			this.resolve(dependency);
		}
	}

	private boolean resolve(AssetID dependency) {
		L.d("RESOLVING DEPENDENCY", dependency);
		PackageSearchParameters search_params = ResourcesManager.newSearchParameters();
		search_params.setAssetId(dependency);

		PackageSearchResult search_result = ResourcesManager.findPackages(search_params);

		// search_result.print();
		if (search_result.isEmpty()) {
			String msg = "Asset [" + dependency + "] was not found in any package.";
			L.d(msg);
			ResourcesManager.printAllPackages();
			throw new Error(msg);
			//
		}

		PackageHandler package_handler = search_result.getBest();
		// package_handler.print();

		PACKAGE_STATUS package_status = package_handler.getStatus();
		if (PACKAGE_STATUS.NOT_INSTALLED == package_status) {
			package_handler.install();
		}

		PackageFormat format = package_handler.getFormat();
		Collection<PackageReader> package_loaders = ResourcesManager.findPackageReaders(format);
		if (package_loaders.isEmpty()) {
			ResourcesManager.printAllPackageReaders();
			L.e("Failed to read package", package_handler);
			throw new Error("No package reader for " + format);
			//
		}

		PackageReader package_reader = package_loaders.getLast();
		package_handler.readPackage(null, package_reader);

		return true;
	}

	@Override
	public void printAllLoadedAssets() {
		this.assets.print("all loaded assets");
	}

	@Override
	public boolean autoResolveAsset(AssetID dependency) {
		AssetHandler asset_entry = AssetsManager.obtainAsset(dependency, this);
		if (asset_entry != null) {
			AssetsManager.releaseAsset(asset_entry, this);
			return true;
		}
		ResourcesManager.updateAll();
		return resolve(dependency);
	}

	@Override
	public boolean autoResolveAssets() {
		return Sys.getFlag(AssetsManagerFlags.AutoresolveDependencies);
	}

}
