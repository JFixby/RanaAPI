package com.jfixby.r3.api.resources.manager;

import com.jfixby.cmns.api.assets.AssetID;
import com.jfixby.cmns.api.collections.List;

public interface PackageSearchParameters {

	void setAssetId(AssetID asset_to_find);

	AssetID getAssetId();

//	List<PackageFormat> acceptPackageFormat();

	List<PACKAGE_STATUS> acceptPackageStatus();

	void print();

	void setGetAllAssetsFlag(boolean b);

	boolean isGetAllAssetsFlagActive();

}
