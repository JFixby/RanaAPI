package com.jfixby.red.triplane.resources.fsbased;

import com.jfixby.cmns.api.assets.AssetID;
import com.jfixby.cmns.api.assets.Names;
import com.jfixby.cmns.api.collections.Collection;
import com.jfixby.cmns.api.collections.JUtils;
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
	Set<PackageHandler> all_handlers = JUtils.newSet();
	Map<AssetID, Set<PackageHandler>> handlers_by_asset_id = JUtils.newMap();
	private FileSystemBasedResource master;

	public void reset() {
		handlers_by_asset_id.clear();
		all_handlers.clear();
	}

	public void add(PackageDescriptor descriptor, File package_folder) {

		PackageHandlerImpl handler = new PackageHandlerImpl(package_folder, this);
		String format_string = descriptor.format;
		handler.setFormat(format_string);
		handler.setVersion(descriptor.version);
		handler.setTimestamp(descriptor.timestamp());
		handler.setRootFileName(descriptor.root_file_name);

		for (int i = 0; i < descriptor.packed_assets.size(); i++) {
			String name = descriptor.packed_assets.get(i);
			AssetID element = Names.newAssetID(name);
			handler.descriptors.add(element);
		}

		for (int i = 0; i < descriptor.package_dependencies.size(); i++) {
			String name = descriptor.package_dependencies.get(i);
			AssetID element = Names.newAssetID(name);
			handler.dependencies.add(element);
		}

		// L.d("found package handler: " + handler);
		addHandler(handler);

	}

	private void addHandler(PackageHandler handler) {
		for (int i = 0; i < handler.listPackedAssets().size(); i++) {
			AssetID key = handler.listPackedAssets().getElementAt(i);
			Set<PackageHandler> list = handlers_by_asset_id.get(key);
			if (list == null) {
				list = JUtils.newSet();
				handlers_by_asset_id.put(key, list);
			}
			list.add(handler);
			all_handlers.add(handler);
		}
	}

	public PackageSearchResult findPackages(PackageSearchParameters search_params) {
		RedPackageSearchResult result = new RedPackageSearchResult(search_params);
		Set<PackageHandler> handlers = null;
		if (search_params.isGetAllAssetsFlagActive()) {
			handlers = getAllHandlers();
		} else {
			AssetID asset_id = search_params.getAssetId();
			handlers = filterHandlers(asset_id);
		}

		int n = handlers.size();
		for (int i = 0; i < n; i++) {
			PackageHandler handler = handlers.getElementAt(i);
			if (handler_fits(handler, search_params)) {
				result.add(handler);
			}
		}

		return result;
	}

	private Set<PackageHandler> getAllHandlers() {
		return this.all_handlers;
	}

	private Set<PackageHandler> filterHandlers(AssetID asset_id) {
		Set<PackageHandler> handlers = handlers_by_asset_id.get(asset_id);
		if (handlers == null) {
			handlers = JUtils.newSet();
		}
		return handlers;
	}

	private boolean handler_fits(PackageHandler handler, PackageSearchParameters search_params) {
		if (search_params.isGetAllAssetsFlagActive()) {
			return true;
		}

		// L.d("cheking: " + handler, search_params);

		// List<PackageFormat> accepted_formats = search_params
		// .acceptPackageFormat();
		List<PACKAGE_STATUS> acccepted_statuses = search_params.acceptPackageStatus();

		if (!acccepted_statuses.contains(handler.getStatus())) {
			return false;
		}
		// if (!accepted_formats.contains(handler.getFormat())) {
		// return false;
		// }
		AssetID asset_id = search_params.getAssetId();
		Collection<AssetID> descriptors = handler.listPackedAssets();
		boolean contains = descriptors.contains(asset_id);
		if (!contains) {
			// L.d("asset_id", asset_id);
			// descriptors.print("not found");
			return false;
		}

		return true;
	}

	public void print() {
		this.handlers_by_asset_id.print("index");
	}

	public ResourceIndex(FileSystemBasedResource fileSystemBasedResource) {
		super();
		this.master = fileSystemBasedResource;

	}

	public void autoResolveAssets(Collection<AssetID> dependencies) {
		master.autoResolveAssets(dependencies);

	}

	public boolean autoResolveAssets() {
		return master.autoResolveAssets();
	}
}
