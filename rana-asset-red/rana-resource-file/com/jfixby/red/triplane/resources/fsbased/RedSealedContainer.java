
package com.jfixby.red.triplane.resources.fsbased;

import com.jfixby.cmns.api.assets.AssetID;
import com.jfixby.cmns.api.collections.Collection;
import com.jfixby.cmns.api.collections.Collections;
import com.jfixby.cmns.api.collections.Map;
import com.jfixby.cmns.api.debug.Debug;
import com.jfixby.cmns.api.err.Err;
import com.jfixby.rana.api.asset.Asset;
import com.jfixby.rana.api.asset.AssetsContainer;
import com.jfixby.rana.api.asset.SealedAssetsContainer;
import com.jfixby.rana.api.pkg.PackageReader;
import com.jfixby.rana.api.pkg.PackageReaderListener;

public class RedSealedContainer implements SealedAssetsContainer, AssetsContainer {

	final Map<AssetID, Asset> assets = Collections.newMap();

	private final PackageReaderListener reader_listener;
	private final PackageReader reader;
	private final RedPackageHandler handler;

	boolean sealed = false;

	public RedSealedContainer (final RedPackageHandler packageHandlerImpl, final PackageReaderListener reader_listener,
		final PackageReader reader) {
		this.reader_listener = reader_listener;
		this.handler = packageHandlerImpl;
		this.reader = reader;
	}

	@Override
	public void seal () {
		Debug.checkTrue(!this.sealed);
		this.reader_listener.onPackageDataLoaded(this);
	}

	@Override
	public long getPackageTimeStamp () {
		return this.handler.getTimeStamp();
	}

	@Override
	public long readPackageTimeStamp () {
		return this.handler.reReadTimeStamp();
	}

	@Override
	public Asset getAsset (final AssetID asset_id) {
		return this.assets.get(asset_id);
	}

	@Override
	public void printAll () {
		this.assets.print("assets");
	}

	@Override
	public boolean purgeAssets (final Collection<AssetID> assetsToPurge) {
		if (!assetsToPurge.containsAll(this.assets.keys())) {
// Err.reportWarning("Unable to purge");
// assetsToPurge.print("assets");
// final Set<AssetID> uncovered = Collections.newSet();
// uncovered.addAll(this.assets.keys());
// uncovered.removeAll(assetsToPurge);
// uncovered.print("uncovered");
// L.d("problems purging", this);
// this.assets.print(" assets");
// assetsToPurge.print("to purge");
// Err.reportError("Not equal sets");
			return false;
		}

		if (!this.assets.keys().containsAll(assetsToPurge)) {
			this.assets.print("  assets");
			assetsToPurge.print("to purge");
			Err.reportError("Not equal sets");
			return false;
		}

		this.assets.print("dispose");

		this.reader_listener.onPackageDataDispose(this);
		final Collection<AssetID> disposalList = assetsToPurge;
		for (final AssetID key : disposalList) {
			final Asset asset = this.assets.get(key);
			if (asset == null) {
				this.assets.print("assets");
				Err.reportError(key + " not found");
			}
			asset.getGroup().dispose();
			this.handler.flagUnload();
			break;
		}

		this.assets.clear();
		return true;
	}

	@Override
	public Collection<AssetID> listAssets () {
		return this.assets.keys();
	}

	@Override
	public void addAsset (final AssetID raster_id, final Asset data) {
		Debug.checkTrue(!this.sealed);
		Debug.checkNull("raster_id", raster_id);
		Debug.checkNull("data", data);
		this.assets.put(raster_id, data);
	}

	@Override
	public String toString () {
		return "Package<" + this.handler.getPackageName() + "> " + this.handler.getFormat() + " " + this.handler.getPackageFolder();
	}

}
