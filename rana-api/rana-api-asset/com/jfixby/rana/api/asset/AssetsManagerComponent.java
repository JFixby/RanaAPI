
package com.jfixby.rana.api.asset;

import com.jfixby.cmns.api.assets.ID;
import com.jfixby.cmns.api.collections.Collection;
import com.jfixby.rana.api.pkg.PackageReaderListener;

public interface AssetsManagerComponent {

	AssetHandler obtainAsset (ID asset_id, AssetsConsumer consumer);

	void releaseAsset (AssetHandler handler, AssetsConsumer consumer);

	void releaseAllAssets (AssetsConsumer consumer);

	boolean autoResolveAsset (ID dependency, PackageReaderListener listener);

	void autoResolveAssets (Collection<ID> dependencies, PackageReaderListener listener);

	void printAllLoadedAssets ();

// void checkAll ();

	AssetHandler useAsset (ID spriteAssetID);

	boolean isRegisteredAsset (ID dependency);

	void purge ();

	void printUsages ();

	void registerAssetsContainer (SealedAssetsContainer container);

	void unRegisterAssetsContainer (SealedAssetsContainer container);

}
