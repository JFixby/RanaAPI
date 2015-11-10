package com.jfixby.rana.api.pkg;

import com.jfixby.cmns.api.collections.Collection;
import com.jfixby.cmns.api.components.ComponentInstaller;

public class ResourcesManager {

	static private ComponentInstaller<ResourcesManagerComponent> componentInstaller = new ComponentInstaller<ResourcesManagerComponent>("ResourcesManager");

	public static final void installComponent(ResourcesManagerComponent component_to_install) {
		componentInstaller.installComponent(component_to_install);
	}

	public static final ResourcesManagerComponent invoke() {
		return componentInstaller.invokeComponent();
	}

	public static final ResourcesManagerComponent component() {
		return componentInstaller.getComponent();
	}

	public static PackageSearchParameters newSearchParameters() {
		return invoke().newSearchParameters();
	}

	public static PackageSearchResult findPackages(PackageSearchParameters search_params) {
		return invoke().findPackages(search_params);
	}

	public static void updateAll() {
		invoke().updateAll();
	}

	public static void printAllPackages() {
		invoke().printAllPackages();
	}
	

	public static PackageFormat newPackageFormat(String format_name) {
		return invoke().newPackageFormat(format_name);
	}
	

	public static Collection<PackageReader> findPackageReaders(PackageFormat format) {
		return invoke().findPackageReaders(format);
	}

	public static void registerPackageReader(PackageReader loader) {
		invoke().registerPackageReader(loader);
	}

	public static Collection<PackageFormat> listAcceptablePackageFormats() {
		return invoke().listAcceptablePackageFormats();
	}

	public static void printAllPackageReaders() {
		invoke().printInstalledPackageReaders();
	}

}
