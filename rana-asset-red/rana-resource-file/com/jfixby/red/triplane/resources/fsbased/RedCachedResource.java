
package com.jfixby.red.triplane.resources.fsbased;

import java.io.IOException;

import com.jfixby.cmns.api.debug.Debug;
import com.jfixby.cmns.api.file.File;
import com.jfixby.cmns.api.file.FileConflistResolver;
import com.jfixby.cmns.api.file.FileSystem;
import com.jfixby.rana.api.pkg.CachedResource;
import com.jfixby.rana.api.pkg.CachedResourceSpecs;
import com.jfixby.rana.api.pkg.CachedResourceUpdateListener;
import com.jfixby.rana.api.pkg.PackageSearchParameters;
import com.jfixby.rana.api.pkg.PackageSearchResult;
import com.jfixby.rana.api.pkg.ResourceRebuildIndexListener;

public class RedCachedResource implements CachedResource {

	private final String name;
	private final FileSystemBasedResource local;
	private final File bankRoot;
	private final File cacheRoot;
	private final File localFolder;

	public RedCachedResource (final CachedResourceSpecs cacherdSpecs) throws IOException {
		this.name = Debug.checkNull("name", cacherdSpecs.getName());
		this.bankRoot = Debug.checkNull("bankRoot", cacherdSpecs.getBankRoot());
		this.cacheRoot = Debug.checkNull("cacheRoot", cacherdSpecs.getCacheRoot());
		Debug.checkTrue(this.bankRoot.isFolder());
		this.localFolder = this.cacheRoot.child(this.name);
		this.localFolder.makeFolder();
		this.local = new FileSystemBasedResource(this.localFolder);
	}

	@Override
	public PackageSearchResult findPackages (final PackageSearchParameters search_params) {
		return this.local.findPackages(search_params);
	}

	@Override
	public void rebuildIndex (final ResourceRebuildIndexListener listener) {
		final FileSystem FS = this.bankRoot.getFileSystem();
		try {
			final FileConflistResolver policy = FileConflistResolver.OVERWRITE_IF_NEW;
			FS.copyFolderContentsToFolder(this.bankRoot, this.localFolder, policy);
		} catch (final IOException e) {
			e.printStackTrace();
		}
		this.local.rebuildIndex(listener);
	}

	@Override
	public String getName () {
		return this.name;
	}

	@Override
	public void updateCache (final CachedResourceUpdateListener listener) {

	}

}
