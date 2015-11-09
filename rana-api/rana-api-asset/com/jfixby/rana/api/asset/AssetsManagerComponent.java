package com.jfixby.rana.api.asset;

import com.jfixby.cmns.api.assets.AssetID;
import com.jfixby.cmns.api.collections.Collection;
import com.jfixby.rana.api.pkg.PackageFormat;
import com.jfixby.rana.api.pkg.PackageReader;

public interface AssetsManagerComponent {

	Collection<PackageReader> findPackageReaders(PackageFormat format);

	void registerPackageReader(PackageReader loader);

	Collection<PackageFormat> listAcceptablePackageFormats();

	void printInstalledPackageReaders();

	AssetHandler obtainAsset(AssetID asset_id, AssetsConsumer consumer);

	void releaseAsset(AssetHandler handler, AssetsConsumer consumer);

	void releaseAllAssets(AssetsConsumer consumer);

	void registerAssetContainer(AssetID asset_id, AssetContainer container);

	boolean autoResolveAsset(AssetID dependency);

	void autoResolveAssets(Collection<AssetID> dependencies);

	void printAllLoadedAssets();

	void checkAll();

	PackageFormat newPackageFormat(String format_name);

}
