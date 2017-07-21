package com.jfixby.rana.async;

import java.io.IOException;

import com.jfixby.rana.api.manager.AssetsManager;
import com.jfixby.scarabei.api.assets.ID;

class RedLoadTaskStep {
		private final ID scene_id;
		boolean procesed = false;

		public RedLoadTaskStep (final ID scene_id) {
			this.scene_id = scene_id;
		}

		public boolean isDone () {
			return this.procesed;
		}

		public void push () {
			try {
				AssetsManager.autoResolveAsset(this.scene_id);
				this.procesed = true;
			} catch (final IOException e) {
				e.printStackTrace();
				this.procesed = false;
			}
// this.procesed = true;
		}
	}