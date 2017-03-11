package com.jfixby.rana.api.pkg.cache;

import com.jfixby.scarabei.api.taskman.TaskProgress;

public interface ProgressListener {

	void onLoaderBegin();

	void onUpdateProgress(TaskProgress task_progress);

	void onLoaderEnd();

	boolean isDoneListening();

}
