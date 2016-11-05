
package com.jfixby.rana.api.pkg;

public interface Resource {

	PackageSearchResult findPackages (PackageSearchParameters search_params);

	void rebuildIndex ();

}
