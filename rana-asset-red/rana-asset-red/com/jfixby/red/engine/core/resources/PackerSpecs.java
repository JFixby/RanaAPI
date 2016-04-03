package com.jfixby.red.engine.core.resources;

import com.jfixby.cmns.api.assets.AssetID;
import com.jfixby.cmns.api.collections.Collection;
import com.jfixby.cmns.api.collections.Collections;
import com.jfixby.cmns.api.collections.List;
import com.jfixby.cmns.api.collections.Set;
import com.jfixby.cmns.api.file.File;

public class PackerSpecs {

	private File PackageFolder;
	final Set<File> PackedFiles = Collections.newSet();
	final Set<AssetID> PackedAssets = Collections.newSet();
	final Set<AssetID> RequiredAssets = Collections.newSet();
	private String rootFileName;
	String PackageFormat;
	String Version;

	public File getPackageFolder() {
		return PackageFolder;
	}

	public Set<File> getPackedFiles() {
		return PackedFiles;
	}

	public Set<AssetID> getPackedAssets() {
		return PackedAssets;
	}

	public Set<AssetID> getRequiredAssets() {
		return RequiredAssets;
	}

	public String getRootFileName() {
		return rootFileName;
	}

	public void setPackageFolder(File package_folder) {
		this.PackageFolder = package_folder;
	}

	public void addPackedFiles(Collection<File> files) {
		PackedFiles.addAll(files);
	}

	public void setRootFileName(String rootFileName) {
		this.rootFileName = rootFileName;
	}

	public void setPackedAssets(List<AssetID> packed) {
		this.PackedAssets.addAll(packed);
	}

	public void setRequiredAssets(List<AssetID> required) {
		this.RequiredAssets.addAll(required);
	}

	public String getPackageFormat() {
		return PackageFormat;
	}

	public void setPackageFormat(String packageFormat) {
		PackageFormat = packageFormat;
	}

	public String getVersion() {
		return Version;
	}

	public void setVersion(String version) {
		Version = version;
	}

}
