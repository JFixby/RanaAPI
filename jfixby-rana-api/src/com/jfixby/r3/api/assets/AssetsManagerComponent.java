package com.jfixby.r3.api.assets;

import com.jfixby.cmns.api.assets.AssetID;
import com.jfixby.cmns.api.collections.Collection;
import com.jfixby.r3.api.resources.manager.PACKAGE_PRIORITY;
import com.jfixby.r3.api.resources.manager.PackageFormat;
import com.jfixby.r3.api.resources.manager.PackageReader;

public interface AssetsManagerComponent {

	Collection<PackageReader> findPackageReaders(PackageFormat format);

	void registerPackageReader(PackageReader loader);

	Collection<PackageFormat> listAcceptablePackageFormats();

	void printInstalledPackageReaders();

	<T extends Asset> AssetHandler<T> obtainAsset(AssetID asset_id,
			AssetsConsumer consumer);

	<T extends Asset> void releaseAsset(AssetHandler<T> handler,
			AssetsConsumer consumer);

	void releaseAllAssets(AssetsConsumer consumer);

	<T extends Asset> void registerAssetContainer(AssetID asset_id,
			AssetContainer<T> container);

	boolean autoResolveAsset(AssetID dependency);

	void autoResolveAssets(Collection<AssetID> dependencies);

	void printAllLoadedAssets();

	boolean autoResolveAsset(AssetID asset_id, PACKAGE_PRIORITY priority);

	void checkAll();

}
