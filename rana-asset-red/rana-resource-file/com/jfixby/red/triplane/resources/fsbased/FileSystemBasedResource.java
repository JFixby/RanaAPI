
package com.jfixby.red.triplane.resources.fsbased;

import java.io.IOException;

import com.jfixby.cmns.api.err.Err;
import com.jfixby.cmns.api.file.ChildrenList;
import com.jfixby.cmns.api.file.File;
import com.jfixby.cmns.api.file.FileSystem;
import com.jfixby.cmns.api.log.L;
import com.jfixby.cmns.api.sys.settings.ExecutionMode;
import com.jfixby.cmns.api.sys.settings.SystemSettings;
import com.jfixby.rana.api.pkg.PackageSearchParameters;
import com.jfixby.rana.api.pkg.PackageSearchResult;
import com.jfixby.rana.api.pkg.Resource;
import com.jfixby.rana.api.pkg.fs.PackageDescriptor;

public class FileSystemBasedResource implements Resource {

	@Override
	public String toString () {
		return "FileSystemBasedResource[" + this.bank_folder + "]";
	}

	ResourceIndex index = new ResourceIndex(this);
	private final File bank_folder;

	public FileSystemBasedResource (final File bank_folder) {
		if (!bank_folder.exists() || !bank_folder.isFolder()) {
			final String msg = "Resource root folder was not found: " + bank_folder;
			L.e(msg);
			throw new Error(msg);
		}
		this.bank_folder = bank_folder;
	}

	@Override
	public void update () {

		this.index.reset();
		final ChildrenList list = this.bank_folder.listDirectChildren();
		final FileSystem FS = this.bank_folder.getFileSystem();
		for (int i = 0; i < list.size(); i++) {
			final File file_i = list.getElementAt(i);
			if (file_i.isFolder()) {
				this.try_to_index(file_i);
			}
		}
		// index.print();

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

	private void index (final PackageDescriptor descriptor, final File package_folder) {
		this.index.add(descriptor, package_folder);
	}

	@Override
	public PackageSearchResult findPackages (final PackageSearchParameters search_params) {
		return this.index.findPackages(search_params);
	}

}
