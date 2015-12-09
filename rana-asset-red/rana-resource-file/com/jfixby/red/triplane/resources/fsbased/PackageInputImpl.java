package com.jfixby.red.triplane.resources.fsbased;

import com.jfixby.cmns.api.file.File;
import com.jfixby.rana.api.pkg.PackageHandler;
import com.jfixby.rana.api.pkg.PackageInput;
import com.jfixby.rana.api.pkg.PackageReaderListener;

public class PackageInputImpl implements PackageInput {

	private PackageReaderListener reader_listener;

	private File root_file;
	private PackageHandlerImpl packageHandlerImpl;

	public PackageInputImpl(PackageReaderListener reader_listener, File root_file, PackageHandlerImpl packageHandlerImpl) {
		this.reader_listener = reader_listener;
		this.root_file = root_file;
		this.packageHandlerImpl = packageHandlerImpl;

	}

	@Override
	public PackageReaderListener getPackageReaderListener() {
		return reader_listener;
	}

	@Override
	public File getRootFile() {
		return root_file;
	}

	@Override
	public PackageHandler getPackageHandler() {
		return packageHandlerImpl;
	}

}
