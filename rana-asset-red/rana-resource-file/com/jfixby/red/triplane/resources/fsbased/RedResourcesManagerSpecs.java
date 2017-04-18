
package com.jfixby.red.triplane.resources.fsbased;

import com.jfixby.rana.api.pkg.ResourceRebuildIndexListener;
import com.jfixby.scarabei.api.collections.Collection;
import com.jfixby.scarabei.api.collections.Collections;
import com.jfixby.scarabei.api.collections.Set;
import com.jfixby.scarabei.api.file.File;

public class RedResourcesManagerSpecs {

	private File assets_folder;
	final Set<RemoteBankSpecs> remotebanks = Collections.newSet();
	boolean readResourcesConfigFile = true;

	public void setAssetsFolder (final File assets_folder) {
		this.assets_folder = assets_folder;
	}

	public File getAssetFolder () {
		return this.assets_folder;
	}

	private File assets_cache_folder;

	public void setAssetsCacheFolder (final File assets_cache_folder) {
		this.assets_cache_folder = assets_cache_folder;
	}

	public File getAssetCacheFolder () {
		return this.assets_cache_folder;
	}

	public RemoteBankSpecs newRemoteBankSpecs () {
		return new RemoteBankSpecs();
	}

	public void addRemoteBank (final RemoteBankSpecs remote) {
		this.remotebanks.add(remote);
	}

	public Collection<RemoteBankSpecs> listRemoteBanks () {
		return this.remotebanks;
	}

	public ResourceRebuildIndexListener getListener () {
		return ResourceRebuildIndexListener.DEFAULT;
	}

	public void setReadResourcesConfigFile (final boolean readResourcesConfigFile) {
		this.readResourcesConfigFile = readResourcesConfigFile;
	}

	public boolean getReadResourcesConfigFile () {
		return this.readResourcesConfigFile;
	}

}
