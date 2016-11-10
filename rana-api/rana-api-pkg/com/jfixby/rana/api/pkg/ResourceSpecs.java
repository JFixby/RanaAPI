
package com.jfixby.rana.api.pkg;

import com.jfixby.cmns.api.file.File;

public interface ResourceSpecs {

	File getBankFolder ();

	void setBankFolder (File bank);

	boolean isChachingRequired ();

	void setCachingRequired (boolean required);

	void setCacheFolder (File bank_cache);

	File getCacheFolder ();

}
