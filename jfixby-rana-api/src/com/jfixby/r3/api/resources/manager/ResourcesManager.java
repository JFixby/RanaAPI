package com.jfixby.r3.api.resources.manager;

import com.jfixby.cmns.api.components.ComponentInstaller;

public class ResourcesManager {

	static private ComponentInstaller<ResourcesManagerComponent> componentInstaller = new ComponentInstaller<ResourcesManagerComponent>(
			"ResourcesManager");

	public static final void installComponent(
			ResourcesManagerComponent component_to_install) {
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

	public static PackageSearchResult findPackages(
			PackageSearchParameters search_params) {
		return invoke().findPackages(search_params);
	}

	public static void updateAll() {
		invoke().updateAll();
	}

	public static void printAllPackages() {
		invoke().printAllPackages();
	}



}
