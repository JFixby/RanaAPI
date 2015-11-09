package com.jfixby.red.triplane.resources.fsbased;

import com.jfixby.cmns.api.filesystem.File;
import com.jfixby.r3.api.resources.manager.PACKAGE_PRIORITY;
import com.jfixby.r3.api.resources.manager.PackageHandler;
import com.jfixby.r3.api.resources.manager.PackageInput;
import com.jfixby.r3.api.resources.manager.PackageReaderListener;

public class PackageInputImpl implements PackageInput {

	private PackageReaderListener reader_listener;
	private PACKAGE_PRIORITY priority;
	private File root_file;
	private PackageHandlerImpl packageHandlerImpl;

	public PackageInputImpl(PackageReaderListener reader_listener,
			PACKAGE_PRIORITY priority, File root_file,
			PackageHandlerImpl packageHandlerImpl) {
		this.reader_listener = reader_listener;
		this.root_file = root_file;
		this.packageHandlerImpl = packageHandlerImpl;
		this.priority = priority;

	}

	@Override
	public PackageReaderListener getPackageReaderListener() {
		return reader_listener;
	}

	@Override
	public PACKAGE_PRIORITY getPriority() {
		return priority;
	}

	@Override
	public File getRootFile() {
		return root_file;
	}

	@Override
	public PackageHandler getPackageHandler() {
		return packageHandlerImpl;
	}

//	@Override
//	public Object requestFeature(String feature_string) {
//		return packageHandlerImpl.requestFeature(feature_string);
//	}

}
