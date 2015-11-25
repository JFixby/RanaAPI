package com.jfixby.red.triplane.resources.fsbased;

import java.util.Comparator;

import com.jfixby.cmns.api.collections.Collection;
import com.jfixby.cmns.api.collections.List;
import com.jfixby.cmns.api.log.L;
import com.jfixby.cmns.api.util.JUtils;
import com.jfixby.rana.api.pkg.PackageHandler;
import com.jfixby.rana.api.pkg.PackageSearchParameters;
import com.jfixby.rana.api.pkg.PackageSearchResult;

public class RedPackageSearchResult implements PackageSearchResult {

	final List<PackageHandler> list = JUtils.newList();
	boolean is_sorted = false;
	private Comparator<PackageHandler> default_comparator = new Comparator<PackageHandler>() {
		@Override
		public int compare(PackageHandler o1, PackageHandler o2) {
			long ts1 = o1.getVersion().getTimeStamp();
			long ts2 = o2.getVersion().getTimeStamp();
			return Long.compare(ts1, ts2);
		}
	};
	private PackageSearchParameters search_params;;

	public RedPackageSearchResult(PackageSearchParameters search_params) {
		this.search_params = search_params;
	}

	@Override
	public PackageHandler getBest() {
		if (this.size() == 0) {
			throw new Error("PackageSearchResult is empty");
		}
		if (!is_sorted) {
			this.sort(default_comparator);
			is_sorted = true;
		}
		if (this.size() > 1) {
//			this.list.print("options");
		}

		return list.getLast();
	}

	@Override
	public boolean isEmpty() {
		return list.size() == 0;
	}

	@Override
	public void sort(Comparator<PackageHandler> comparator) {
		this.list.sort(comparator);
	}

	public void add(PackageSearchResult result_i) {
		for (int i = 0; i < result_i.size(); i++) {
			PackageHandler handler = result_i.getEntry(i);
			this.add(handler);
		}
	}

	public void add(PackageHandler handler) {
		list.add(handler);
		is_sorted = false;
	}

	@Override
	public PackageHandler getEntry(int i) {
		return this.list.getElementAt(i);
	}

	@Override
	public int size() {
		return this.list.size();
	}

	@Override
	public void print() {
		L.d("---PackageSearchResult----------------");
		search_params.print();
		this.list.print("packages");
	}

	@Override
	public Collection<PackageHandler> list() {
		return list;
	}

}
