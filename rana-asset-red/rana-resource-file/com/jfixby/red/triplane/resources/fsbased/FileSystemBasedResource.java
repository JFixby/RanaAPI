package com.jfixby.red.triplane.resources.fsbased;

import com.jfixby.cmns.api.assets.AssetID;
import com.jfixby.cmns.api.collections.Collection;
import com.jfixby.cmns.api.file.ChildrenList;
import com.jfixby.cmns.api.file.File;
import com.jfixby.cmns.api.file.FileSystem;
import com.jfixby.cmns.api.json.Json;
import com.jfixby.cmns.api.log.L;
import com.jfixby.cmns.api.util.path.AbsolutePath;
import com.jfixby.rana.api.asset.AssetsManager;
import com.jfixby.rana.api.pkg.PackageSearchParameters;
import com.jfixby.rana.api.pkg.PackageSearchResult;
import com.jfixby.rana.api.pkg.Resource;
import com.jfixby.rana.api.pkg.fs.PackageDescriptor;

public class FileSystemBasedResource implements Resource {

	@Override
	public String toString() {
		return "FileSystemBasedResource[" + bank_folder + "]";
	}

	ResourceIndex index = new ResourceIndex(this);
	private File bank_folder;

	public FileSystemBasedResource(File bank_folder) {
		if (!bank_folder.exists() || !bank_folder.isFolder()) {
			String msg = "Resource root folder was not found: " + bank_folder;
			L.e(msg);
			throw new Error(msg);
		}
		this.bank_folder = bank_folder;
	}

	@Override
	public void update() {
		index.reset();
		ChildrenList list = bank_folder.listChildren();
		FileSystem FS = bank_folder.getFileSystem();
		for (int i = 0; i < list.size(); i++) {
			File file_i = list.getElementAt(i);
			if (file_i.isFolder()) {
				try_to_index(file_i);
			}
		}
		// index.print();

	}

	private void try_to_index(File package_folder) {
		try {
			// file_i.listChildren().print();

			FileSystem FS = package_folder.getFileSystem();
			AbsolutePath<FileSystem> descriptor_file_path = package_folder.child(PackageDescriptor.PACKAGE_DESCRIPTOR_FILE_NAME).getAbsoluteFilePath();
			String data = FS.readFileToString(descriptor_file_path);
			PackageDescriptor descriptor = Json.deserializeFromString(PackageDescriptor.class, data);
			index(descriptor, package_folder);
		} catch (Exception e) {
			L.d("failed to read", package_folder);
			L.e(e.toString());
			e.printStackTrace();
		}
	}

	private void index(PackageDescriptor descriptor, File package_folder) {
		index.add(descriptor, package_folder);
	}

	@Override
	public PackageSearchResult findPackages(PackageSearchParameters search_params) {
		return this.index.findPackages(search_params);
	}

	public void autoResolveAssets(Collection<AssetID> dependencies) {
		AssetsManager.autoResolveAssets(dependencies);
	}

	public boolean autoResolveAssets() {
		return AssetsManager.autoResolveAssets();
	}
}
