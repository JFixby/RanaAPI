
package com.jfixby.rana.api.pkg;

import com.jfixby.cmns.api.file.File;

public interface CachedResourceSpecs {

	void setBankRoot (File httpRemote);

	void setCacheRoot (File assets_cache_folder);

	void setName (String string);

	String getName ();

	File getBankRoot ();

	File getCacheRoot ();

}
