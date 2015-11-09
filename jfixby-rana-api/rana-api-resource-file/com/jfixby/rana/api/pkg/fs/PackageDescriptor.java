package com.jfixby.rana.api.pkg.fs;

import java.util.Vector;

public class PackageDescriptor {

	public static final String PACKAGE_DESCRIPTOR_FILE_NAME = "package.descriptor";
	public static final String PACKAGE_CONTENT_FOLDER = "content";

	public String format;
	public String version;
	public String timestamp;
	public String root_file_name;
	public Vector<String> packed_assets = new Vector<String>();
	public Vector<String> package_dependencies = new Vector<String>();

	public long timestamp() {
		return Long.parseLong(timestamp);
	}

}
