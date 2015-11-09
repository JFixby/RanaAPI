package com.jfixby.r3.api.assets;

import com.jfixby.cmns.api.assets.AssetID;
import com.jfixby.cmns.api.collections.Collection;
import com.jfixby.cmns.api.components.ComponentInstaller;
import com.jfixby.r3.api.resources.manager.PACKAGE_PRIORITY;
import com.jfixby.r3.api.resources.manager.PackageFormat;
import com.jfixby.r3.api.resources.manager.PackageReader;

public class AssetsManager {

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

	public static Collection<PackageReader> findPackageReaders(PackageFormat format) {
		return invoke().findPackageReaders(format);
	}

	public static void registerPackageReader(PackageReader loader) {
		invoke().registerPackageReader(loader);
	}

	public static Collection<PackageFormat> listAcceptablePackageFormats() {
		return invoke().listAcceptablePackageFormats();
	}

	public static void printAllPackageReaders() {
		invoke().printInstalledPackageReaders();
	}

	public static <T extends Asset> void registerAsset(AssetID asset_id, AssetContainer<T> container) {
		invoke().registerAssetContainer(asset_id, container);
	}

	public static <T extends Asset> AssetHandler<T> obtainAsset(AssetID asset_id, AssetsConsumer consumer) {
		return invoke().obtainAsset(asset_id, consumer);
	}

	public static <T extends Asset> void releaseAsset(AssetHandler<T> handler, AssetsConsumer consumer) {
		invoke().releaseAsset(handler, consumer);
	}

	public static void releaseAllAssets(AssetsConsumer consumer) {
		invoke().releaseAllAssets(consumer);
	}

	public static boolean autoResolveAsset(AssetID dependency) {
		return invoke().autoResolveAsset(dependency);
	}

	public static void autoResolveAssets(Collection<AssetID> dependencies) {
		invoke().autoResolveAssets(dependencies);
	}

	public static void printAllLoadedAssets() {
		invoke().printAllLoadedAssets();
	}

	public static boolean autoResolveAsset(AssetID asset_id, PACKAGE_PRIORITY priority) {
		return invoke().autoResolveAsset(asset_id, priority);
	}

	public static void checkAll() {
		invoke().checkAll();
	}

}
