package com.jfixby.red.triplane.resources.fsbased;

import java.io.IOException;

import com.jfixby.cmns.api.assets.AssetID;
import com.jfixby.cmns.api.collections.Collection;
import com.jfixby.cmns.api.collections.JUtils;
import com.jfixby.cmns.api.collections.List;
import com.jfixby.cmns.api.filesystem.File;
import com.jfixby.cmns.api.filesystem.FileSystem;
import com.jfixby.cmns.api.filesystem.FileSystemSandBox;
import com.jfixby.cmns.api.log.L;
import com.jfixby.cmns.api.sys.Sys;
import com.jfixby.r3.api.resources.manager.PACKAGE_PRIORITY;
import com.jfixby.r3.api.resources.manager.PACKAGE_STATUS;
import com.jfixby.r3.api.resources.manager.PackageDescriptor;
import com.jfixby.r3.api.resources.manager.PackageFormat;
import com.jfixby.r3.api.resources.manager.PackageHandler;
import com.jfixby.r3.api.resources.manager.PackageReader;
import com.jfixby.r3.api.resources.manager.PackageReaderListener;
import com.jfixby.r3.api.resources.manager.PackageVersion;

public class PackageHandlerImpl implements PackageHandler, PackageVersion {


	private void autoResolveAssets(Collection<AssetID> dependencies) {
		resourceIndex.autoResolveAssets(dependencies);
//		AssetsManager.autoResolveAssets(dependencies);
	}

	List<AssetID> descriptors = JUtils.newList();
	List<AssetID> dependencies = JUtils.newList();

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

		private boolean auto() {
			L.d("Missing flag");
			return true;
		}
	};
	private String name;
	private File root_file;
	private PackageFormat format;
	private ResourceIndex resourceIndex;

	public PackageHandlerImpl(File package_folder, ResourceIndex resourceIndex) {
		this.resourceIndex = resourceIndex;
		fs = package_folder.getFileSystem();
		this.package_folder = package_folder;
		content_folder = fs.newFile(package_folder.child(PackageDescriptor.PACKAGE_CONTENT_FOLDER).getAbsoluteFilePath());
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
	public PackageFormat getFormat() {
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
	public void readPackage(PackageReaderListener reader_listener, PackageReader reader, PACKAGE_PRIORITY priority) {
		if (this.status == PACKAGE_STATUS.BROKEN) {
			throw new Error("Package is brocken: " + this);
		}
		if (this.status == PACKAGE_STATUS.NOT_INSTALLED) {
			throw new Error("Package is not installed: " + this);
		}
		FileSystem FS = package_folder.getFileSystem();
		File sandbox_folder = null;
		if (FS.isReadOnlyFileSystem()) {
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
			PackageInputImpl input = new PackageInputImpl(reader_listener, priority, root_file, this);
			reader.doReadPackage(input);
		} catch (IOException e) {
			this.status = PACKAGE_STATUS.BROKEN;
			reader_listener.onError(e);
		}
		root_file = null;
	}

	@Override
	public void readPackage(PackageReaderListener reader_listener, PackageReader reader) {
		this.readPackage(reader_listener, reader, PACKAGE_PRIORITY.USER);
	}

	public void setFormat(String format_string) {
		JUtils.checkNull("format", format_string);
		JUtils.checkEmpty("format", format_string);
		format = new PackageFormat(format_string);
	}

	public void setVersion(String version) {
		JUtils.checkNull("version", version);
		JUtils.checkEmpty("version", version);
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
