
package com.jfixby.red.triplane.resources.fsbased;

import java.io.IOException;

import com.jfixby.cmns.api.assets.AssetID;
import com.jfixby.cmns.api.assets.Names;
import com.jfixby.cmns.api.collections.Collection;
import com.jfixby.cmns.api.collections.Collections;
import com.jfixby.cmns.api.collections.List;
import com.jfixby.cmns.api.collections.Map;
import com.jfixby.cmns.api.collections.Set;
import com.jfixby.cmns.api.file.File;
import com.jfixby.rana.api.pkg.PACKAGE_STATUS;
import com.jfixby.rana.api.pkg.PackageHandler;
import com.jfixby.rana.api.pkg.PackageSearchParameters;
import com.jfixby.rana.api.pkg.PackageSearchResult;
import com.jfixby.rana.api.pkg.fs.PackageDescriptor;

public class ResourceIndex {
	Set<PackageHandler> all_handlers = Collections.newSet();
	Map<AssetID, Set<PackageHandler>> handlers_by_asset_id = Collections.newMap();
	private final FileSystemBasedResource master;

	public void reset () {
		this.handlers_by_asset_id.clear();
		this.all_handlers.clear();
	}

	public void add (final PackageDescriptor descriptor, final File package_folder) throws IOException {

		final PackageHandlerImpl handler = new PackageHandlerImpl(package_folder, this);
		final String format_string = descriptor.format;
		handler.setFormat(format_string);
		handler.setVersion(descriptor.version);
		handler.setTimestamp(descriptor.timestamp());
		handler.setRootFileName(descriptor.root_file_name);

		for (int i = 0; i < descriptor.packed_assets.size(); i++) {
			final String name = descriptor.packed_assets.get(i);
			final AssetID element = Names.newAssetID(name);
			handler.descriptors.add(element);
		}

		for (int i = 0; i < descriptor.package_dependencies.size(); i++) {
			final String name = descriptor.package_dependencies.get(i);
			final AssetID element = Names.newAssetID(name);
			handler.dependencies.add(element);
		}

		// L.d("found package handler: " + handler);
		this.addHandler(handler);

	}

	private void addHandler (final PackageHandler handler) {
		for (int i = 0; i < handler.listPackedAssets().size(); i++) {
			final AssetID key = handler.listPackedAssets().getElementAt(i);
			Set<PackageHandler> list = this.handlers_by_asset_id.get(key);
			if (list == null) {
				list = Collections.newSet();
				this.handlers_by_asset_id.put(key, list);
			}
			list.add(handler);
			this.all_handlers.add(handler);
		}
	}

	public PackageSearchResult findPackages (final PackageSearchParameters search_params) {
		final RedPackageSearchResult result = new RedPackageSearchResult(search_params);
		Set<PackageHandler> handlers = null;
		if (search_params.isGetAllAssetsFlagActive()) {
			handlers = this.getAllHandlers();
		} else {
			final AssetID asset_id = search_params.getAssetId();
			handlers = this.filterHandlers(asset_id);
		}

		final int n = handlers.size();
		for (int i = 0; i < n; i++) {
			final PackageHandler handler = handlers.getElementAt(i);
			if (this.handler_fits(handler, search_params)) {
				result.add(handler);
			}
		}

		return result;
	}

	private Set<PackageHandler> getAllHandlers () {
		return this.all_handlers;
	}

	private Set<PackageHandler> filterHandlers (final AssetID asset_id) {
		Set<PackageHandler> handlers = this.handlers_by_asset_id.get(asset_id);
		if (handlers == null) {
			handlers = Collections.newSet();
		}
		return handlers;
	}

	private boolean handler_fits (final PackageHandler handler, final PackageSearchParameters search_params) {
		if (search_params.isGetAllAssetsFlagActive()) {
			return true;
		}

		// L.d("cheking: " + handler, search_params);

		// List<PackageFormat> accepted_formats = search_params
		// .acceptPackageFormat();
		final List<PACKAGE_STATUS> acccepted_statuses = search_params.acceptPackageStatus();

		if (!acccepted_statuses.contains(handler.getStatus())) {
			return false;
		}
		// if (!accepted_formats.contains(handler.getFormat())) {
		// return false;
		// }
		final AssetID asset_id = search_params.getAssetId();
		final Collection<AssetID> descriptors = handler.listPackedAssets();
		final boolean contains = descriptors.contains(asset_id);
		if (!contains) {
			// L.d("asset_id", asset_id);
			// descriptors.print("not found");
			return false;
		}

		return true;
	}

	public void print () {
		this.handlers_by_asset_id.print("index");
	}

	public ResourceIndex (final FileSystemBasedResource fileSystemBasedResource) {
		super();
		this.master = fileSystemBasedResource;

	}

	public long reReadTimeStamp (final PackageHandlerImpl packageHandlerImpl) {
		return this.master.reReadTimeStamp(packageHandlerImpl);
	}

}
