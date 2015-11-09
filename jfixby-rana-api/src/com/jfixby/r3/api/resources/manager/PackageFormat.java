package com.jfixby.r3.api.resources.manager;

import com.jfixby.cmns.api.collections.JUtils;

public class PackageFormat {
	String format_name;

	public PackageFormat(String format_name) {
		JUtils.checkNull("format_name", format_name);
		JUtils.checkEmpty("format_name", format_name);
		this.format_name = format_name;
	}

	public String getName() {
		return format_name;
	}

	@Override
	public String toString() {
		return "PackageFormat<" + format_name + ">";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((format_name == null) ? 0 : format_name.hashCode());
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
		PackageFormat other = (PackageFormat) obj;
		if (format_name == null) {
			if (other.format_name != null)
				return false;
		} else if (!format_name.equals(other.format_name))
			return false;
		return true;
	}

}
