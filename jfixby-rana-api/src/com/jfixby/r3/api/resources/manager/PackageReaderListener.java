package com.jfixby.r3.api.resources.manager;

import java.io.IOException;

import com.jfixby.cmns.api.assets.AssetID;
import com.jfixby.cmns.api.collections.Collection;

public interface PackageReaderListener {

	void onError(IOException e);

	void onDependenciesRequired(Collection<AssetID> dependencies);

}
