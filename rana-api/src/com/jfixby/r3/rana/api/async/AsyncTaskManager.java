
package com.jfixby.r3.rana.api.async;

import com.jfixby.scarabei.api.ComponentInstaller;

public class AsyncTaskManager {

	static private ComponentInstaller<AsyncTaskManagerComponent> componentInstaller = new ComponentInstaller<AsyncTaskManagerComponent>(
		"AsyncTaskManagerComponent");

	public static final void installComponent (final AsyncTaskManagerComponent component_to_install) {
		componentInstaller.installComponent(component_to_install);
	}

	public static final AsyncTaskManagerComponent invoke () {
		return componentInstaller.invokeComponent();
	}

	public static final AsyncTaskManagerComponent component () {
		return componentInstaller.getComponent();
	}

}
