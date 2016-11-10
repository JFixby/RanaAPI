
package com.jfixby.rana.api.pkg;

import java.io.IOException;

import com.jfixby.cmns.api.assets.AssetID;
import com.jfixby.cmns.api.collections.Collection;
import com.jfixby.cmns.api.err.Err;
import com.jfixby.cmns.api.sys.settings.SystemSettings;
import com.jfixby.rana.api.asset.AssetsManager;
import com.jfixby.rana.api.asset.AssetsManagerFlags;
import com.jfixby.rana.api.asset.SealedAssetsContainer;

public interface PackageReaderListener {

	public static final PackageReaderListener DEFAULT = new PackageReaderListener() {

		@Override
		public void onError (final IOException e) {
			e.printStackTrace();
			Err.reportError(e);
		}

		@Override
		public void onDependenciesRequired (final PackageHandler handler, final Collection<AssetID> dependencies) {
			final boolean auto = SystemSettings.getFlag(AssetsManagerFlags.AutoresolveDependencies);

			if (!auto) {
				dependencies.print("Missing dependencies");
				Err.reportError("RedTriplaneFlags." + auto + " flag is false.");
			} else {

				for (int i = 0; i < dependencies.size(); i++) {
					final AssetID dep = dependencies.getElementAt(i);
					if (AssetsManager.isRegisteredAsset(dep)) {

					} else {
						AssetsManager.autoResolveAsset(dep, DEFAULT);
					}
				}

			}
		}

		@Override
		public void onPackageDataDispose (final SealedAssetsContainer container) {
			AssetsManager.unRegisterAssetsContainer(container);
		}

		@Override
		public void onPackageDataLoaded (final SealedAssetsContainer container) {
			AssetsManager.registerAssetsContainer(container);
		}

	};

	void onError (IOException e);

	void onDependenciesRequired (PackageHandler requiredBy, Collection<AssetID> dependencies);

	void onPackageDataDispose (final SealedAssetsContainer data);

	void onPackageDataLoaded (final SealedAssetsContainer data);

}
