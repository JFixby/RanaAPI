
package com.jfixby.r3.rana.red.async;

import com.jfixby.scarabei.api.taskman.Job;

class UpdateProgressJob implements Job {

	private final RedLoadTask redLoadTask;

	public UpdateProgressJob (final RedLoadTask redLoadTask) {
		this.redLoadTask = redLoadTask;
	}

	@Override
	public void doStart () throws Throwable {
	}

	@Override
	public void doPush () throws Throwable {

		for (int i = 0; i < this.redLoadTask.assets_to_load_list.size(); i++) {
			final RedLoadTaskStep step = this.redLoadTask.assets_to_load_list.getElementAt(i);
			if (!step.isDone()) {
				step.push();
				break;
			}
		}

		int done = 0;
		for (int i = 0; i < this.redLoadTask.assets_to_load_list.size(); i++) {
			final RedLoadTaskStep step = this.redLoadTask.assets_to_load_list.getElementAt(i);
			if (step.isDone()) {
				done++;
			}
		}
		this.redLoadTask.progress.updateProcessed(done);
		if (done == this.redLoadTask.progress.getTotalSteps()) {
			this.redLoadTask.progress.setIsDone();
		}

	}

	@Override
	public boolean isDone () {
		return this.redLoadTask.progress.isDone();
	}
};
