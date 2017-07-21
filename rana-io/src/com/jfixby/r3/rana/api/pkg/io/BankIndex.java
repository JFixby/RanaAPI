
package com.jfixby.r3.rana.api.pkg.io;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class BankIndex implements Serializable {

	private static final long serialVersionUID = -1093587103868366257L;
	public static final String FILE_NAME = "index.r3-bank";
	public static final String FILE_NAME_DEBUG = "index.r3-bank.json";

	public final LinkedHashMap<String, PackageDescriptor> descriptors = new LinkedHashMap<String, PackageDescriptor>();

	public void add (final String name, final PackageDescriptor descriptor) {
		this.descriptors.put(name, descriptor);
	}

}
