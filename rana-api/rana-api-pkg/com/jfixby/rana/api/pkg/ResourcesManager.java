
package com.jfixby.rana.api.pkg;

import com.jfixby.cmns.api.ComponentInstaller;
import com.jfixby.cmns.api.collections.Collection;
import com.jfixby.rana.api.asset.Asset;

public class ResourcesManager {

	static private ComponentInstaller<ResourcesManagerComponent> componentInstaller = new ComponentInstaller<ResourcesManagerComponent>(
		"ResourcesManager");

	public static final void installComponent (final ResourcesManagerComponent component_to_install) {
		componentInstaller.installComponent(component_to_install);
	}

	public static final ResourcesManagerComponent invoke () {
		return componentInstaller.invokeComponent();
	}

	public static final ResourcesManagerComponent component () {
		return componentInstaller.getComponent();
	}

	public static PackageSearchParameters newSearchParameters () {
		return invoke().newSearchParameters();
	}

	public static PackageSearchResult findPackages (final PackageSearchParameters search_params) {
		return invoke().findPackages(search_params);
	}

	public static void updateAll () {
		invoke().updateAll();
	}

	public static void printAllPackages () {
		invoke().printAllPackages();
	}

	public static PackageFormat newPackageFormat (final String format_name) {
		return invoke().newPackageFormat(format_name);
	}

	public static Collection<PackageReader> findPackageReaders (final PackageFormat format) {
		return invoke().findPackageReaders(format);
	}

	public static <T extends Asset> void registerPackageReader (final PackageReader loader) {
		invoke().registerPackageReader(loader);
	}

	public static Collection<PackageFormat> listAcceptablePackageFormats () {
		return invoke().listAcceptablePackageFormats();
	}

	public static void printAllPackageReaders () {
		invoke().printInstalledPackageReaders();
	}

	public static void rebuildIndex () {
		invoke().rebuildIndex();
	}

}
