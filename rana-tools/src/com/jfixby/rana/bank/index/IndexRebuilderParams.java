
package com.jfixby.rana.bank.index;

import com.jfixby.cmns.api.collections.Collection;
import com.jfixby.cmns.api.collections.Collections;
import com.jfixby.cmns.api.collections.Set;
import com.jfixby.cmns.api.debug.Debug;
import com.jfixby.cmns.api.file.File;

public class IndexRebuilderParams {

	private File bankFolder;
	final Set<String> tanks = Collections.newSet();

	public void setBankFolder (final File bankFolder) {
		this.bankFolder = bankFolder;
	}

	public void addTankToIndex (final String string) {
		Debug.checkNull("tank name", string);
		this.tanks.add(string);
	}

	public File getBankFolder () {
		return this.bankFolder;
	}

	public Collection<String> listTanks () {
		return this.tanks;
	}

	public void addTanksToIndex (final Collection<String> tanksList) {
		this.tanks.addAll(tanksList);
	}

}
