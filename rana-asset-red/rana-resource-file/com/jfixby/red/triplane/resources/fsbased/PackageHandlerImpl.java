package com.jfixby.red.triplane.resources.fsbased;

import java.io.IOException;

import com.jfixby.cmns.api.assets.AssetID;
import com.jfixby.cmns.api.collections.Collection;
import com.jfixby.cmns.api.collections.Collections;
import com.jfixby.cmns.api.collections.List;
import com.jfixby.cmns.api.debug.Debug;
import com.jfixby.cmns.api.file.File;
import com.jfixby.cmns.api.file.FileSystem;
import com.jfixby.cmns.api.file.FileSystemSandBox;
import com.jfixby.cmns.api.log.L;
import com.jfixby.cmns.api.sys.Sys;
import com.jfixby.cmns.api.sys.settings.SystemSettings;
import com.jfixby.rana.api.asset.AssetsManager;
import com.jfixby.rana.api.pkg.PACKAGE_STATUS;
import com.jfixby.rana.api.pkg.PackageHandler;
import com.jfixby.rana.api.pkg.PackageReader;
import com.jfixby.rana.api.pkg.PackageReaderListener;
import com.jfixby.rana.api.pkg.PackageVersion;
import com.jfixby.rana.api.pkg.fs.PackageDescriptor;

public class PackageHandlerImpl implements PackageHandler, PackageVersion {

	private void autoResolveAssets(Collection<AssetID> dependencies) {
		resourceIndex.autoResolveAssets(dependencies);
		// AssetsManager.autoResolveAssets(dependencies);
	}

	private boolean auto() {
		return resourceIndex.autoResolveAssets();
	}

	List<AssetID> descriptors = Collections.newList();
	List<AssetID> dependencies = Collections.newList();

	private String version;
	private long timestamp;
	private String root_file_name;
	private File package_folder;
	private PACKAGE_STATUS status;
	private FileSystem fs;
	private File content_folder;
	private PackageReaderListener default_listener = new PackageReaderListener() {

		@Override
		public void onError(IOException e) {
			e.printStackTrace();
			L.e(e);
			Sys.exit();
		}

		@Override
		public void onDependenciesRequired(Collection<AssetID> dependencies) {
			boolean auto = auto();
			if (!auto) {
				dependencies.print("Missing dependencies");
				throw new Error("RedTriplaneFlags." + auto + " flag is false.");
			} else {
				autoResolveAssets(dependencies);
			}
		}

	};
	private String name;
	private File root_file;
	private PackageFormatImpl format;
	private ResourceIndex resourceIndex;

	public PackageHandlerImpl(File package_folder, ResourceIndex resourceIndex) {
		this.resourceIndex = resourceIndex;
		fs = package_folder.getFileSystem();
		this.package_folder = package_folder;
		content_folder = fs
				.newFile(package_folder.child(PackageDescriptor.PACKAGE_CONTENT_FOLDER).getAbsoluteFilePath());
		if (!content_folder.exists()) {
			this.status = PACKAGE_STATUS.BROKEN;
		} else {
			this.status = PACKAGE_STATUS.INSTALLED;
		}
		name = package_folder.getName();

	}

	@Override
	public PackageVersion getVersion() {
		return this;
	}

	@Override
	public PackageFormatImpl getFormat() {
		return format;
	}

	@Override
	public PACKAGE_STATUS getStatus() {
		return status;
	}

	@Override
	public Collection<AssetID> listPackedAssets() {
		return descriptors;
	}

	@Override
	public String toString() {
		return "PackageHandler[" + format + "] ver." + version + " " + descriptors + " timestamp=" + this.timestamp;
	}

	@Override
	public void print() {
		L.d(this);
	}

	@Override
	public void install() {
		L.d("already installed", this);
	}

	@Override
	public void readPackage(PackageReaderListener reader_listener, PackageReader reader) {
		if (this.status == PACKAGE_STATUS.BROKEN) {
			throw new Error("Package is brocken: " + this);
		}
		if (this.status == PACKAGE_STATUS.NOT_INSTALLED) {
			throw new Error("Package is not installed: " + this);
		}
		FileSystem FS = package_folder.getFileSystem();
		File sandbox_folder = null;

		boolean use_sandbox = SystemSettings.getFlag(AssetsManager.UseAssetSandBox);
		if (FS.isReadOnlyFileSystem() || !use_sandbox) {
			sandbox_folder = content_folder;
		} else {
			sandbox_folder = FileSystemSandBox.wrap(package_folder.getName(), content_folder).ROOT();
			FS = sandbox_folder.getFileSystem();
		}

		root_file = sandbox_folder.child(root_file_name);
		if (reader_listener == null) {
			reader_listener = default_listener;
		}

		try {
			PackageInputImpl input = new PackageInputImpl(reader_listener, root_file, this);
			L.d("reading", root_file);
			reader.doReadPackage(input);
		} catch (IOException e) {
			this.status = PACKAGE_STATUS.BROKEN;
			reader_listener.onError(e);
		}
		root_file = null;
	}

	public void setFormat(String format_string) {
		Debug.checkNull("format", format_string);
		Debug.checkEmpty("format", format_string);
		format = new PackageFormatImpl(format_string);
	}

	public void setVersion(String version) {
		Debug.checkNull("version", version);
		Debug.checkEmpty("version", version);
		this.version = version;
	}

	@Override
	public long getTimeStamp() {
		return timestamp;
	}

	@Override
	public String getVersionName() {
		return version;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public void setRootFileName(String root_file_name) {
		this.root_file_name = root_file_name;
	}

	@Override
	public String getPackageName() {
		return name;
	}

	@Override
	public Collection<AssetID> listDependencies() {
		return dependencies;
	}

	// public Object requestFeature(String feature_string) {
	//
	// if ("com.badlogic.gdx.files.FileHandle".equals(feature_string)) {
	// String class_name = this.root_file.getClass().toString();
	//
	// if (class_name.equals(feature_string)) {
	//
	// }
	// return this.root_file;
	// }
	// return null;
	// }
}
