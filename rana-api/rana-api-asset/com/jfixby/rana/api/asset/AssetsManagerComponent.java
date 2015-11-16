package com.jfixby.rana.api.asset;

import com.jfixby.cmns.api.assets.AssetID;
import com.jfixby.cmns.api.collections.Collection;

public interface AssetsManagerComponent {

	AssetHandler obtainAsset(AssetID asset_id, AssetsConsumer consumer);

	void releaseAsset(AssetHandler handler, AssetsConsumer consumer);

	void releaseAllAssets(AssetsConsumer consumer);

	void registerAssetContainer(AssetID asset_id, AssetContainer container);

	boolean autoResolveAsset(AssetID dependency);

	void autoResolveAssets(Collection<AssetID> dependencies);

	void printAllLoadedAssets();

	void checkAll();

	boolean autoResolveAssets();

}
