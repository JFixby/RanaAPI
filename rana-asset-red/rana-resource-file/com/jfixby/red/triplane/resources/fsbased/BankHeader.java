
package com.jfixby.red.triplane.resources.fsbased;

import com.jfixby.cmns.api.file.File;
import com.jfixby.rana.api.pkg.bank.BankHeaderInfo;

public class BankHeader {

	private final File bank_folder;

	public BankHeader (final BankHeaderInfo headerInfo, final File bank_folder) {
		this.bank_folder = bank_folder;

	}

	public File getRoot () {
		return this.bank_folder;
	}

	@Override
	public String toString () {
		return "Bank[" + this.bank_folder + "]";
	}

}
