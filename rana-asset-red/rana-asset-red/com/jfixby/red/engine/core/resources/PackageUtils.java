package com.jfixby.red.engine.core.resources;

import java.io.IOException;

import com.jfixby.cmns.api.assets.AssetID;
import com.jfixby.cmns.api.collections.Collection;
import com.jfixby.cmns.api.file.File;
import com.jfixby.cmns.api.io.IO;
import com.jfixby.cmns.api.java.ByteArray;
import com.jfixby.cmns.api.json.Json;
import com.jfixby.cmns.api.log.L;
import com.jfixby.cmns.api.sys.Sys;
import com.jfixby.rana.api.pkg.fs.PackageDescriptor;

public class PackageUtils {
    public static void pack(PackerSpecs specs) throws IOException {

	File package_folder = specs.getPackageFolder();
	File package_content_folder = package_folder.child(PackageDescriptor.PACKAGE_CONTENT_FOLDER);
	package_content_folder.makeFolder();
	package_content_folder.clearFolder();

	String package_format = specs.getPackageFormat();
	String version = specs.getVersion();
	Collection<AssetID> provisions = specs.getPackedAssets();
	Collection<AssetID> dependencies = specs.getRequiredAssets();
	String root_file_name = specs.getRootFileName();
	producePackageDescriptor(package_folder, package_format, version, provisions, dependencies, root_file_name);

	Collection<File> files_list = specs.getPackedFiles();
	package_content_folder.getFileSystem().copyFilesTo(files_list, package_content_folder);

    }

    static public void producePackageDescriptor(File output_folder, String format, String version,
	    Collection<AssetID> provisions, Collection<AssetID> dependencies, String root_file_name)
	    throws IOException {

	PackageDescriptor descriptor = new PackageDescriptor();
	descriptor.format = format;
	descriptor.timestamp = "" + Sys.SystemTime().currentTimeMillis();
	descriptor.version = version;
	for (AssetID d : provisions) {
	    descriptor.packed_assets.addElement(d.toString());
	}
	for (AssetID d : dependencies) {
	    descriptor.package_dependencies.addElement(d.toString());
	}

	descriptor.root_file_name = root_file_name;
	File output_file = output_folder.child(PackageDescriptor.PACKAGE_DESCRIPTOR_FILE_NAME);
	ByteArray data = IO.serialize(descriptor);
	output_file.writeBytes(data);

	 L.d("packing", Json.serializeToString(descriptor));

    }
}
