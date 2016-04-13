
package com.jfixby.rana.api.asset;

import com.jfixby.cmns.api.ComponentInstaller;
import com.jfixby.cmns.api.assets.AssetID;
import com.jfixby.cmns.api.collections.Collection;

public class AssetsManager {

	public static final String UseAssetSandBox = "UseAssetSandBox";

	static private ComponentInstaller<AssetsManagerComponent> componentInstaller = new ComponentInstaller<AssetsManagerComponent>(
		"AssetsManager");

	public static final void installComponent (final AssetsManagerComponent component_to_install) {
		componentInstaller.installComponent(component_to_install);
	}

	public static final AssetsManagerComponent invoke () {
		return componentInstaller.invokeComponent();
	}

	public static final AssetsManagerComponent component () {
		return componentInstaller.getComponent();
	}

	public static void registerAsset (final AssetID asset_id, final AssetContainer container) {
		invoke().registerAssetContainer(asset_id, container);
	}

	public static AssetHandler obtainAsset (final AssetID asset_id, final AssetsConsumer consumer) {
		return invoke().obtainAsset(asset_id, consumer);
	}

	public static void releaseAsset (final AssetHandler handler, final AssetsConsumer consumer) {
		invoke().releaseAsset(handler, consumer);
	}

	public static void releaseAllAssets (final AssetsConsumer consumer) {
		invoke().releaseAllAssets(consumer);
	}

	public static void printAllLoadedAssets () {
		invoke().printAllLoadedAssets();
	}

	public static void checkAll () {
		invoke().checkAll();
	}

	public static boolean autoResolveAsset (final AssetID dependency) {
		return invoke().autoResolveAsset(dependency);
	}

	public static void autoResolveAssets (final Collection<AssetID> dependencies) {
		invoke().autoResolveAssets(dependencies);
	}

}
