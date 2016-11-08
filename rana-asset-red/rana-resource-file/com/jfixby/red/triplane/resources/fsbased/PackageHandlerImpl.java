
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
import com.jfixby.rana.api.asset.AssetsManagerFlags;
import com.jfixby.rana.api.asset.SealedAssetsContainer;
import com.jfixby.rana.api.pkg.PACKAGE_STATUS;
import com.jfixby.rana.api.pkg.PackageHandler;
import com.jfixby.rana.api.pkg.PackageReader;
import com.jfixby.rana.api.pkg.PackageReaderListener;
import com.jfixby.rana.api.pkg.PackageVersion;
import com.jfixby.rana.api.pkg.fs.PackageDescriptor;

public class PackageHandlerImpl implements PackageHandler, PackageVersion {

	final List<AssetID> descriptors = Collections.newList();
	final List<AssetID> dependencies = Collections.newList();

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
			Err.reportError(e);
		}

		@Override
		public void onDependenciesRequired (final PackageHandler handler, final Collection<AssetID> dependencies) {
			final boolean auto = SystemSettings.getFlag(AssetsManagerFlags.AutoresolveDependencies);

			if (!auto) {
				dependencies.print("Missing dependencies");
				throw new Error("RedTriplaneFlags." + auto + " flag is false.");
			} else {

				for (int i = 0; i < dependencies.size(); i++) {
					final AssetID dep = dependencies.getElementAt(i);
					if (AssetsManager.isRegisteredAsset(dep)) {

					} else {
						AssetsManager.autoResolveAsset(dep);
					}
				}

			}
		}

		@Override
		public void onPackageDataDispose (final SealedAssetsContainer container) {
			AssetsManager.unRegisterAssetsContainer(container);
		}

		@Override
		public void onPackageDataLoaded (final SealedAssetsContainer container) {
			AssetsManager.registerAssetsContainer(container);
		}

	};
	private final String name;
// private File root_file;
	private PackageFormatImpl format;
	private final ResourceIndex resourceIndex;

	public PackageHandlerImpl (final File package_folder, final ResourceIndex resourceIndex) throws IOException {
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
	public SealedAssetsContainer doReadPackage (PackageReaderListener reader_listener, final PackageReader reader)
		throws IOException {
		if (this.status == PACKAGE_STATUS.BROKEN) {
			Err.reportError("Package is brocken: " + this);
		}
		if (this.status == PACKAGE_STATUS.NOT_INSTALLED) {
			Err.reportError("Package is not installed: " + this);
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

		File root_file = sandbox_folder.child(this.root_file_name);
		if (reader_listener == null) {
			reader_listener = this.default_listener;
		}

		try {

			final RedSealedContainer packageData = new RedSealedContainer(this, reader_listener, reader);
			final PackageInputImpl input = new PackageInputImpl(reader_listener, root_file, packageData, this);
			L.d("reading", root_file);
			reader.doReadPackage(input);
			packageData.seal();
			this.isLoaded = true;
			return packageData;
		} catch (final IOException e) {
			this.status = PACKAGE_STATUS.BROKEN;
			reader_listener.onError(e);
		}
		root_file = null;
		return null;
	}

	public void flagUnload () {
		this.isLoaded = false;
	}

	boolean isLoaded = false;

	@Override
	public boolean isLoaded () {
		return this.isLoaded;
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

	@Override
	public long reReadTimeStamp () {
		return this.resourceIndex.reReadTimeStamp(this);
	}

	public File getPackageFolder () {
		return this.package_folder;
	}

}
