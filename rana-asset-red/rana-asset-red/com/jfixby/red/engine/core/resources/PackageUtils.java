package com.jfixby.red.engine.core.resources;

import java.io.IOException;

import com.jfixby.cmns.api.assets.AssetID;
import com.jfixby.cmns.api.collections.Collection;
import com.jfixby.cmns.api.file.File;
import com.jfixby.cmns.api.json.Json;
import com.jfixby.cmns.api.sys.Sys;
import com.jfixby.rana.api.pkg.fs.PackageDescriptor;

public class PackageUtils {

	static public void producePackageDescriptor(File package_folder, String format, String version, Collection<AssetID> provisions, Collection<AssetID> dependencies, String root_file_name) throws IOException {

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
		File output_file = package_folder.child(PackageDescriptor.PACKAGE_DESCRIPTOR_FILE_NAME);

		output_file.writeString(Json.serializeToString(descriptor));

	}

}
