
package com.jfixby.rana.api.pkg;

import com.jfixby.cmns.api.err.Err;

public interface ResourceRebuildIndexListener {

	static final ResourceRebuildIndexListener DEFAULT = new ResourceRebuildIndexListener() {
		@Override
		public void onError (final Throwable e) {
			Err.reportError(e);
		}
	};

	void onError (Throwable e);

}
