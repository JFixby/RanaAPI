
package com.jfixby.r3.rana.api.loader;

import com.jfixby.r3.rana.api.format.PackageFormat;
import com.jfixby.scarabei.api.assets.ID;
import com.jfixby.scarabei.api.collections.Collection;

public class PackageInfo {

	public String packageName;
	public PackageFormat packageFormat;
	public Collection<ID> packedAssets;
	public Collection<ID> dependencies;

}
