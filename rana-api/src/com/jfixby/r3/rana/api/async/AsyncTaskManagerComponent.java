
package com.jfixby.r3.rana.api.async;

import com.jfixby.scarabei.api.assets.ID;
import com.jfixby.scarabei.api.collections.Collection;
import com.jfixby.scarabei.api.taskman.ProgressListener;

public interface AsyncTaskManagerComponent {

	LoadTaskHandler prepareLoadUITask (Collection<ID> asetsToLoad);

	LoadTaskHandler prepareLoadUITask (ID... asetsToLoad);

	void pushTaskToLoader (LoadTaskHandler task, ProgressListener ui_loader_listener);

	void pushLoadAssetsTask (Collection<ID> newList, ProgressListener loader_listener);

}
