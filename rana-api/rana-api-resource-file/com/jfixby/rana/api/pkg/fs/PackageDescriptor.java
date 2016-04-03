package com.jfixby.rana.api.pkg.fs;

import java.io.Serializable;
import java.util.Vector;

public class PackageDescriptor implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 940908719512054127L;

    public PackageDescriptor() {
	super();
    }

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

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((format == null) ? 0 : format.hashCode());
	result = prime * result + ((package_dependencies == null) ? 0 : package_dependencies.hashCode());
	result = prime * result + ((packed_assets == null) ? 0 : packed_assets.hashCode());
	result = prime * result + ((root_file_name == null) ? 0 : root_file_name.hashCode());
	result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
	result = prime * result + ((version == null) ? 0 : version.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	PackageDescriptor other = (PackageDescriptor) obj;
	if (format == null) {
	    if (other.format != null)
		return false;
	} else if (!format.equals(other.format))
	    return false;
	if (package_dependencies == null) {
	    if (other.package_dependencies != null)
		return false;
	} else if (!package_dependencies.equals(other.package_dependencies))
	    return false;
	if (packed_assets == null) {
	    if (other.packed_assets != null)
		return false;
	} else if (!packed_assets.equals(other.packed_assets))
	    return false;
	if (root_file_name == null) {
	    if (other.root_file_name != null)
		return false;
	} else if (!root_file_name.equals(other.root_file_name))
	    return false;
	if (timestamp == null) {
	    if (other.timestamp != null)
		return false;
	} else if (!timestamp.equals(other.timestamp))
	    return false;
	if (version == null) {
	    if (other.version != null)
		return false;
	} else if (!version.equals(other.version))
	    return false;
	return true;
    }

}
