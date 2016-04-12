
package com.jfixby.red.triplane.resources.fsbased;

import java.io.IOException;

import com.jfixby.cmns.api.assets.AssetID;
import com.jfixby.cmns.api.collections.Collection;
import com.jfixby.cmns.api.collections.Collections;
import com.jfixby.cmns.api.collections.List;
import com.jfixby.cmns.api.debug.Debug;
import com.jfixby.cmns.api.err.Err;
import com.jfixby.cmns.api.file.File;
import com.jfixby.cmns.api.file.FileSystem;
import com.jfixby.cmns.api.file.FileSystemSandBox;
import com.jfixby.cmns.api.log.L;
import com.jfixby.cmns.api.sys.settings.SystemSettings;
import com.jfixby.rana.api.asset.AssetsManager;
import com.jfixby.rana.api.pkg.PACKAGE_STATUS;
import com.jfixby.rana.api.pkg.PackageHandler;
import com.jfixby.rana.api.pkg.PackageReader;
import com.jfixby.rana.api.pkg.PackageReaderListener;
import com.jfixby.rana.api.pkg.PackageVersion;
import com.jfixby.rana.api.pkg.fs.PackageDescriptor;

public class PackageHandlerImpl implements PackageHandler, PackageVersion {

	private void autoResolveAssets (final Collection<AssetID> dependencies) {
		this.resourceIndex.autoResolveAssets(dependencies);
		// AssetsManager.autoResolveAssets(dependencies);
	}

	private boolean auto () {
		return this.resourceIndex.autoResolveAssets();
	}

	List<AssetID> descriptors = Collections.newList();
	List<AssetID> dependencies = Collections.newList();

	private String version;
	private long timestamp;
	private String root_file_name;
	private final File package_folder;
	private PACKAGE_STATUS status;
	private final FileSystem fs;
	private final File content_folder;
	private final PackageReaderListener default_listener = new PackageReaderListener() {

		@Override
		public void onError (final IOException e) {
			e.printStackTrace();
			// L.e(e);
			Err.reportError(e);
		}

		@Override
		public void onDependenciesRequired (final Collection<AssetID> dependencies) {
			final boolean auto = PackageHandlerImpl.this.auto();
			if (!auto) {
				dependencies.print("Missing dependencies");
				throw new Error("RedTriplaneFlags." + auto + " flag is false.");
			} else {
				dependencies.print("RESOLVING");
				PackageHandlerImpl.this.autoResolveAssets(dependencies);
			}
		}

	};
	private final String name;
	private File root_file;
	private PackageFormatImpl format;
	private final ResourceIndex resourceIndex;

	public PackageHandlerImpl (final File package_folder, final ResourceIndex resourceIndex) {
		this.resourceIndex = resourceIndex;
		this.fs = package_folder.getFileSystem();
		this.package_folder = package_folder;
		this.content_folder = this.fs.newFile(package_folder.child(PackageDescriptor.PACKAGE_CONTENT_FOLDER).getAbsoluteFilePath());
		if (!this.content_folder.exists()) {
			this.status = PACKAGE_STATUS.BROKEN;
		} else {
			this.status = PACKAGE_STATUS.INSTALLED;
		}
		this.name = package_folder.getName();

	}

	@Override
	public PackageVersion getVersion () {
		return this;
	}

	@Override
	public PackageFormatImpl getFormat () {
		return this.format;
	}

	@Override
	public PACKAGE_STATUS getStatus () {
		return this.status;
	}

	@Override
	public Collection<AssetID> listPackedAssets () {
		return this.descriptors;
	}

	@Override
	public String toString () {
		return "PackageHandler[" + this.format + "] ver." + this.version + " " + this.descriptors + " timestamp=" + this.timestamp;
	}

	@Override
	public void print () {
		L.d(this);
	}

	@Override
	public void install () {
		L.d("already installed", this);
	}

	@Override
	public void readPackage (PackageReaderListener reader_listener, final PackageReader reader) {
		if (this.status == PACKAGE_STATUS.BROKEN) {
			throw new Error("Package is brocken: " + this);
		}
		if (this.status == PACKAGE_STATUS.NOT_INSTALLED) {
			throw new Error("Package is not installed: " + this);
		}
		FileSystem FS = this.package_folder.getFileSystem();
		File sandbox_folder = null;

		final boolean use_sandbox = SystemSettings.getFlag(AssetsManager.UseAssetSandBox);
		if (FS.isReadOnlyFileSystem() || !use_sandbox) {
			sandbox_folder = this.content_folder;
		} else {
			sandbox_folder = FileSystemSandBox.wrap(this.package_folder.getName(), this.content_folder).ROOT();
			FS = sandbox_folder.getFileSystem();
		}

		this.root_file = sandbox_folder.child(this.root_file_name);
		if (reader_listener == null) {
			reader_listener = this.default_listener;
		}

		try {
			final PackageInputImpl input = new PackageInputImpl(reader_listener, this.root_file, this);
			// L.d("reading", root_file);
			reader.doReadPackage(input);
		} catch (final IOException e) {
			this.status = PACKAGE_STATUS.BROKEN;
			reader_listener.onError(e);
		}
		this.root_file = null;
	}

	public void setFormat (final String format_string) {
		Debug.checkNull("format", format_string);
		Debug.checkEmpty("format", format_string);
		this.format = new PackageFormatImpl(format_string);
	}

	public void setVersion (final String version) {
		Debug.checkNull("version", version);
		Debug.checkEmpty("version", version);
		this.version = version;
	}

	@Override
	public long getTimeStamp () {
		return this.timestamp;
	}

	@Override
	public String getVersionName () {
		return this.version;
	}

	public void setTimestamp (final long timestamp) {
		this.timestamp = timestamp;
	}

	public void setRootFileName (final String root_file_name) {
		this.root_file_name = root_file_name;
	}

	@Override
	public String getPackageName () {
		return this.name;
	}

	@Override
	public Collection<AssetID> listDependencies () {
		return this.dependencies;
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
