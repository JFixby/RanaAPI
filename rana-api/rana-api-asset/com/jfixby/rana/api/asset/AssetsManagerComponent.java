
package com.jfixby.rana.api.asset;

import com.jfixby.cmns.api.assets.AssetID;
import com.jfixby.cmns.api.collections.Collection;
import com.jfixby.rana.api.pkg.PackageReaderListener;

public interface AssetsManagerComponent {

	AssetHandler obtainAsset (AssetID asset_id, AssetsConsumer consumer);

	void releaseAsset (AssetHandler handler, AssetsConsumer consumer);

	void releaseAllAssets (AssetsConsumer consumer);

	void registerAssetContainer (AssetID asset_id, AssetsContainer container);

	void registerAssetsContainer (Collection<AssetID> list, AssetsContainer container);

	boolean autoResolveAsset (AssetID dependency, PackageReaderListener listener);

	void autoResolveAssets (Collection<AssetID> dependencies, PackageReaderListener listener);

	void printAllLoadedAssets ();

// void checkAll ();

	AssetHandler useAsset (AssetID spriteAssetID);

	boolean autoResolveAsset (AssetID dependency);

	void autoResolveAssets (Collection<AssetID> dependencies);

	boolean isRegisteredAsset (AssetID dependency);

	void purge ();

	void unRegisterAssetsContainer (Collection<AssetID> assets, AssetsContainer container);

	void unRegisterAssetContainer (AssetID asset, AssetsContainer container);

}
