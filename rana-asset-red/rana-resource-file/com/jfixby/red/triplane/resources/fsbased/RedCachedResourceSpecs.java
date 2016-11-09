
package com.jfixby.red.triplane.resources.fsbased;

import com.jfixby.cmns.api.file.File;
import com.jfixby.rana.api.pkg.CachedResourceSpecs;

public class RedCachedResourceSpecs implements CachedResourceSpecs {

	private File bankRoot;
	private File cacheRoot;
	private String name;

	@Override
	public void setBankRoot (final File bankRoot) {
		this.bankRoot = bankRoot;
	}

	@Override
	public void setCacheRoot (final File cacheRoot) {
		this.cacheRoot = cacheRoot;
	}

	@Override
	public void setName (final String name) {
		this.name = name;
	}

	@Override
	public String getName () {
		return this.name;
	}

	@Override
	public File getBankRoot () {
		return this.bankRoot;
	}

	@Override
	public File getCacheRoot () {
		return this.cacheRoot;
	}

}
