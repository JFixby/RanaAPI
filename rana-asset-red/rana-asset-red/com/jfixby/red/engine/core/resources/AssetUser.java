package com.jfixby.red.engine.core.resources;

import com.jfixby.cmns.api.util.JUtils;
import com.jfixby.rana.api.asset.AssetsConsumer;

public class AssetUser {

	@Override
	public String toString() {
		return consumer.toString();
	}

	@Override
	public int hashCode() {
		return consumer.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AssetUser other = (AssetUser) obj;
		if (consumer == null) {
			if (other.consumer != null)
				return false;
		} else if (consumer != other.consumer)
			return false;
		return true;
	}

	public final AssetsConsumer consumer;

	public AssetUser(AssetsConsumer consumer) {
		JUtils.checkNull("consumer", consumer);
		this.consumer = consumer;
	}
}
