
package com.jfixby.red.triplane.resources.fsbased;

import com.jfixby.cmns.api.file.File;
import com.jfixby.rana.api.pkg.ResourceSpecs;

public class RedResourceSpecs implements ResourceSpecs {

	private File bank;
	private boolean required;
	private File bank_cache;

	@Override
	public File getBankFolder () {
		return this.bank;
	}

	@Override
	public void setBankFolder (final File bank) {
		this.bank = bank;
	}

	@Override
	public boolean isChachingRequired () {
		return this.required;
	}

	@Override
	public void setCachingRequired (final boolean required) {
		this.required = required;
	}

	@Override
	public void setCacheFolder (final File bank_cache) {
		this.bank_cache = bank_cache;
	}

	@Override
	public File getCacheFolder () {
		return this.bank_cache;
	}

}
