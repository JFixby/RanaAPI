
package com.jfixby.red.triplane.resources.fsbased;

import java.io.IOException;

import com.jfixby.rana.api.pkg.PackageSearchParameters;
import com.jfixby.rana.api.pkg.PackageSearchResult;
import com.jfixby.rana.api.pkg.Resource;
import com.jfixby.rana.api.pkg.ResourceRebuildIndexListener;
import com.jfixby.rana.api.pkg.ResourceSpecs;
import com.jfixby.rana.api.pkg.bank.BankIndex;
import com.jfixby.rana.api.pkg.fs.PackageDescriptor;
import com.jfixby.scarabei.api.debug.Debug;
import com.jfixby.scarabei.api.err.Err;
import com.jfixby.scarabei.api.file.ChildrenList;
import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.file.FileInputStream;
import com.jfixby.scarabei.api.file.FileSystem;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.api.sys.settings.ExecutionMode;
import com.jfixby.scarabei.api.sys.settings.SystemSettings;

public class RedResource implements Resource {

	@Override
	public String toString () {
		return "Resource[" + this.name + "] " + this.bank_folder;
	}

	ResourceIndex index = new ResourceIndex(this);
	private final File bank_folder;
	private final String name;
	private final boolean caching_required;
	private File cache;

	RedResource (final ResourceSpecs specs) throws IOException {

		final File bank_folder = specs.getFolder();
		this.caching_required = specs.isChachingRequired();
		this.name = Debug.checkNull("name", specs.getName());
		if (this.caching_required) {
			this.cache = specs.getCacheFolder();
			this.cache.makeFolder();
		}
		if (!bank_folder.exists() || !bank_folder.isFolder()) {
			final String msg = "Resource root folder was not found: " + bank_folder;
			L.e(msg);
			throw new IOException(msg);
		}
		this.bank_folder = bank_folder;

	}

	@Override
	public String getName () {
		return this.name;
	}

// boolean indexNeverTouched = true;

	@Override
	public PackageSearchResult findPackages (final PackageSearchParameters search_params) {
// if (this.indexNeverTouched) {
// this.rebuildIndex(null);
// }
		return this.index.findPackages(search_params);
	}

	@Override
	public void rebuildIndex (final ResourceRebuildIndexListener listener) {
		L.d("rebuilding index", this);
		this.index.reset();
// this.indexNeverTouched = false;
		if (this.cache == null) {
			this.rebuildIndexLocal(listener, this.bank_folder);
			return;
		}
		try {
			final File indexFile = this.bank_folder.child(BankIndex.FILE_NAME);
			if (indexFile.exists()) {
				L.d("index found", indexFile);
			}
			final FileInputStream is = indexFile.newInputStream();
			is.open();
			final BankIndex index = BankIndex.deSerializeGZIPJava(is);
			is.close();

			for (final String packageName : index.descriptors.keySet()) {
				final PackageDescriptor descriptor = index.descriptors.get(packageName);
				final File package_folder = this.bank_folder.child(packageName);
				this.index(descriptor, package_folder);
			}

		} catch (final IOException e) {
			e.printStackTrace();
			this.rebuildIndexLocal(listener, this.cache);
			listener.onError(e);
			return;
		}
	}

	private void rebuildIndexLocal (final ResourceRebuildIndexListener listener, final File folder) {
		try {
			final ChildrenList list = folder.listDirectChildren();
			for (int i = 0; i < list.size(); i++) {
				final File file_i = list.getElementAt(i);
				if (file_i.isFolder()) {
					this.try_to_index(file_i);
				}
			}
		} catch (final IOException e) {
			listener.onError(e);
		}
	}

	private void try_to_index (final File package_folder) {
		final FileSystem FS = package_folder.getFileSystem();
		final File file = package_folder.child(PackageDescriptor.PACKAGE_DESCRIPTOR_FILE_NAME);
		try {
			final PackageDescriptor descriptor = file.readData(PackageDescriptor.class);
			this.index(descriptor, package_folder);
		} catch (final Exception e) {
			L.e(e.toString());
			e.printStackTrace();
			try {
				L.d(file.readToString());
			} catch (final IOException e1) {
				// e1.printStackTrace();
			}
			L.e("failed to read", file);

			if (SystemSettings.executionModeCovers(ExecutionMode.EARLY_DEVELOPMENT)) {
				Err.reportError(file + " " + e);
			}
		}
	}

	public long reReadTimeStamp (final RedPackageHandler packageHandlerImpl) {
		final File package_folder = packageHandlerImpl.getPackageFolder();
		final File file = package_folder.child(PackageDescriptor.PACKAGE_DESCRIPTOR_FILE_NAME);
		try {
			L.d("re-reading", file);
			final PackageDescriptor descriptor = file.readData(PackageDescriptor.class);
			return descriptor.timestamp();
		} catch (final Exception e) {
			L.e(e.toString());
			e.printStackTrace();
			try {
				L.d(file.readToString());
			} catch (final IOException e1) {
				// e1.printStackTrace();
			}
			L.e("failed to read", file);
		}

		return 0;
	}

	private void index (final PackageDescriptor descriptor, final File package_folder) throws IOException {
		this.index.add(descriptor, package_folder);
	}

	public boolean isCachingRequired () {
		return this.caching_required;
	}

	public File getCacheFolder () {
		return this.cache;
	}

	@Override
	public void printIndex () {
		this.index.print();
	}

}
