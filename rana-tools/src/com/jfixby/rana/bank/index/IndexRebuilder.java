
package com.jfixby.rana.bank.index;

import java.io.IOException;

import com.jfixby.cmns.api.collections.Collection;
import com.jfixby.cmns.api.debug.Debug;
import com.jfixby.cmns.api.err.Err;
import com.jfixby.cmns.api.file.ChildrenList;
import com.jfixby.cmns.api.file.File;
import com.jfixby.cmns.api.file.FileInputStream;
import com.jfixby.cmns.api.file.FileOutputStream;
import com.jfixby.cmns.api.json.Json;
import com.jfixby.cmns.api.json.JsonString;
import com.jfixby.cmns.api.log.L;
import com.jfixby.rana.api.pkg.bank.BankHeaderInfo;
import com.jfixby.rana.api.pkg.bank.BankIndex;
import com.jfixby.rana.api.pkg.fs.PackageDescriptor;

public class IndexRebuilder {

	public static void rebuild (final IndexRebuilderParams rebuilderParams) throws IOException {
		final File bankFolder = rebuilderParams.getBankFolder();
		Debug.checkNull("bankFolder", bankFolder);
		Debug.checkTrue("bankFolder is folder", bankFolder.isFolder());
		if (!(bankFolder.isFolder() && bankFolder.child(BankHeaderInfo.FILE_NAME).exists())) {
			Err.reportError("is not bank: " + bankFolder + " bank header is missing: " + BankHeaderInfo.FILE_NAME);
		}
		L.d("bank folder", bankFolder);

		final Collection<String> tanks = rebuilderParams.listTanks();
		for (final String tankName : tanks) {
			final File tankFolder = bankFolder.child(tankName);
			L.d("   indexing", tankName);
		}
	}

	private static void indexTank (final File tank) throws IOException {
		final File headerFile = tank.child(BankHeaderInfo.FILE_NAME);
		final File indexFile = tank.child(BankIndex.FILE_NAME);
		final File indexDebugFile = tank.child(BankIndex.FILE_NAME_DEBUG);
		final BankIndex index = new BankIndex();
		final ChildrenList folders = tank.listDirectChildren(file -> {
			try {
				if (file.isFile()) {
					return false;
				}

				final File bankHeader = file.child(PackageDescriptor.PACKAGE_DESCRIPTOR_FILE_NAME);
				if (!bankHeader.exists()) {
					return false;
				}
				return true;

			} catch (final IOException e) {
				e.printStackTrace();
			}
			return false;
		});

		for (final File packageFolder : folders) {
			final File descriptorFile = packageFolder.child(PackageDescriptor.PACKAGE_DESCRIPTOR_FILE_NAME);
			final PackageDescriptor descriptor = descriptorFile.readData(PackageDescriptor.class);
			final String packageFolderName = packageFolder.getName();
			L.d("package found", packageFolderName);
			index.add(packageFolderName, descriptor);
		}
		{
			final FileOutputStream os = indexFile.newOutputStream();
			L.d("writing", indexFile);
			os.open();
			BankIndex.serializeGZIPJava(index, os);
			os.close();
		}

		{
			final FileOutputStream os = indexDebugFile.newOutputStream();
			L.d("writing", indexDebugFile);
			os.open();
			BankIndex.serializeJson(index, os);
			os.close();
		}
		{
			final FileInputStream is = indexFile.newInputStream();
			is.open();
			final BankIndex indexCheck = BankIndex.deSerializeGZIPJava(is);
			is.close();
			final JsonString j1 = Json.serializeToString(indexCheck);
			final JsonString j2 = Json.serializeToString(index);
			Debug.checkTrue("failed serialization", j1.equals(j2));

		}

	}

}
