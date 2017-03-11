package com.jfixby.rana.api.pkg.cache;

import com.jfixby.scarabei.api.taskman.TaskProgress;

public interface LoadTask {

	TaskProgress getProgress();

	void launch();

}
