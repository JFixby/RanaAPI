
package com.jfixby.r3.rana.red.async;

import com.jfixby.r3.rana.api.async.AsyncTaskManagerComponent;
import com.jfixby.r3.rana.api.async.LoadTaskHandler;
import com.jfixby.scarabei.api.assets.ID;
import com.jfixby.scarabei.api.collections.Collection;
import com.jfixby.scarabei.api.collections.Collections;
import com.jfixby.scarabei.api.err.Err;
import com.jfixby.scarabei.api.taskman.ProgressListener;

public class RedAsyncTaskComponent implements AsyncTaskManagerComponent {

	@Override
	public LoadTaskHandler prepareLoadUITask (final Collection<ID> asetsToLoad) {
		final RedLoadTask load_task = new RedLoadTask(asetsToLoad);
		return load_task;
	}

	@Override
	public LoadTaskHandler prepareLoadUITask (final ID... asetsToLoad) {
		return this.prepareLoadUITask(Collections.newList(asetsToLoad));
	}

	@Override
	public void pushTaskToLoader (final LoadTaskHandler task, final ProgressListener ui_loader_listener) {
// final LoadTaskEvent event = new LoadTaskEvent(task, ui_loader_listener, this);
// this.events_queue.put(event);
		Err.throwNotImplementedYet();
	}

	@Override
	public void pushLoadAssetsTask (final Collection<ID> newList, final ProgressListener loader_listener) {
		final RedLoadTask task = new RedLoadTask(newList);
		this.pushTaskToLoader(task, loader_listener);

	}
}
