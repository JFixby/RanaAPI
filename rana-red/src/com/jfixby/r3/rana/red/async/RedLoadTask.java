
package com.jfixby.r3.rana.red.async;

import com.jfixby.r3.rana.api.async.LoadTaskHandler;
import com.jfixby.scarabei.api.assets.ID;
import com.jfixby.scarabei.api.collections.Collection;
import com.jfixby.scarabei.api.collections.Collections;
import com.jfixby.scarabei.api.collections.List;
import com.jfixby.scarabei.api.taskman.SimpleProgress;
import com.jfixby.scarabei.api.taskman.Task;
import com.jfixby.scarabei.api.taskman.TaskManager;
import com.jfixby.scarabei.api.taskman.TaskProgress;

class RedLoadTask implements LoadTaskHandler {

	public RedLoadTask (final Collection<ID> assets_to_load_list) {
		// assets_to_load_list.print("preload");
		this.progress.setTotal(assets_to_load_list.size());
		this.progress.updateProcessed(0);
		for (int i = 0; i < assets_to_load_list.size(); i++) {
			final RedLoadTaskStep step = new RedLoadTaskStep(assets_to_load_list.getElementAt(i));
			this.assets_to_load_list.add(step);
		}

	}

	// private long start;
	// private long DELTA = 1000;
	// private long end;

	final List<RedLoadTaskStep> assets_to_load_list = Collections.newList();

	final SimpleProgress progress = TaskManager.newSimpleProgress();

	private final UpdateProgressJob update_progress_job = new UpdateProgressJob(this);

	public void launch () {
		final Task task = TaskManager.newTask("update_progress_job", this.update_progress_job);
	}

	@Override
	public TaskProgress getProgress () {
		return this.progress;
	}

}
