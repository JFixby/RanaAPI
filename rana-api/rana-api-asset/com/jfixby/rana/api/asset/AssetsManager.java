package com.jfixby.rana.api.asset;

import com.jfixby.cmns.api.ComponentInstaller;
import com.jfixby.cmns.api.assets.AssetID;
import com.jfixby.cmns.api.collections.Collection;

public class AssetsManager {
	
	public static final String UseAssetSandBox = "UseAssetSandBox";

	static private ComponentInstaller<AssetsManagerComponent> componentInstaller = new ComponentInstaller<AssetsManagerComponent>("AssetsManager");

	public static final void installComponent(AssetsManagerComponent component_to_install) {
		componentInstaller.installComponent(component_to_install);
	}

	public static final AssetsManagerComponent invoke() {
		return componentInstaller.invokeComponent();
	}

	public static final AssetsManagerComponent component() {
		return componentInstaller.getComponent();
	}

	public static void registerAsset(AssetID asset_id, AssetContainer container) {
		invoke().registerAssetContainer(asset_id, container);
	}

	public static AssetHandler obtainAsset(AssetID asset_id, AssetsConsumer consumer) {
		return invoke().obtainAsset(asset_id, consumer);
	}

	public static void releaseAsset(AssetHandler handler, AssetsConsumer consumer) {
		invoke().releaseAsset(handler, consumer);
	}

	public static void releaseAllAssets(AssetsConsumer consumer) {
		invoke().releaseAllAssets(consumer);
	}

	public static void printAllLoadedAssets() {
		invoke().printAllLoadedAssets();
	}

	public static void checkAll() {
		invoke().checkAll();
	}

	public static boolean autoResolveAsset(AssetID dependency) {
		return invoke().autoResolveAsset(dependency);
	}

	public static void autoResolveAssets(Collection<AssetID> dependencies) {
		invoke().autoResolveAssets(dependencies);
	}

	public static boolean autoResolveAssets() {
		return invoke().autoResolveAssets();
	}

}
