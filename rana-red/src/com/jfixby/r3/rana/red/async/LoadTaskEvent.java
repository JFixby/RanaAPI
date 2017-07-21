
package com.jfixby.r3.rana.red.async;

import com.jfixby.scarabei.api.taskman.Job;
import com.jfixby.scarabei.api.taskman.ProgressListener;
import com.jfixby.scarabei.api.taskman.TaskProgress;
import com.jfixby.scarabei.api.util.JUtils;
import com.jfixby.scarabei.api.util.StateSwitcher;

class LoadTaskEvent implements Job {

	private final RedLoadTask task;

	private final ProgressListener progress_listener;
	private TaskProgress task_progress;
	private final StateSwitcher<TASK_LOADER_STATE> state;
// private final RedUIManager tintoUIManager;

	public LoadTaskEvent (final RedLoadTask task, final ProgressListener ui_loader_listener
// , final RedUIManager tintoUIManager
	) {
		this.task = task;
		this.progress_listener = ui_loader_listener;
// this.tintoUIManager = tintoUIManager;
		this.state = JUtils.newStateSwitcher(TASK_LOADER_STATE.NEW);
	}

	@Override
	public void doStart () {
		this.state.expectState(TASK_LOADER_STATE.NEW);
		this.progress_listener.onLoaderBegin();
		this.task_progress = this.task.getProgress();
		this.progress_listener.onUpdateProgress(this.task_progress);
		this.state.switchState(TASK_LOADER_STATE.PROCESSING);
		this.task.launch();
	}

	@Override
	public boolean isDone () {
		this.state.doesNotExpectState(TASK_LOADER_STATE.NEW);
		if (this.state.currentState() == TASK_LOADER_STATE.PROCESSING) {
			this.progress_listener.onUpdateProgress(this.task_progress);
			if (this.task_progress.isDone()) {
// this.progress_listener.onLoaderEnd();
				this.state.switchState(TASK_LOADER_STATE.CLOSING);
			}
		}

		if (this.state.currentState() == TASK_LOADER_STATE.CLOSING) {
			if (this.progress_listener.isDoneListening()) {
// this.ui_loader_listener.onUILoaderDone();
				this.progress_listener.onLoaderEnd();
				return true;
			}
		}

		return false;
	}

	@Override
	public void doPush () throws Throwable {
	}
}
