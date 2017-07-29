
package com.jfixby.r3.rana.red.pkg.bank;

import java.io.IOException;

import com.jfixby.r3.rana.api.pkg.io.BankIndex;
import com.jfixby.scarabei.api.io.GZipInputStream;
import com.jfixby.scarabei.api.io.GZipOutputStream;
import com.jfixby.scarabei.api.io.IO;
import com.jfixby.scarabei.api.io.InputStream;
import com.jfixby.scarabei.api.io.OutputStream;
import com.jfixby.scarabei.api.java.ByteArray;
import com.jfixby.scarabei.api.json.Json;
import com.jfixby.scarabei.api.json.JsonString;
import com.jfixby.scarabei.api.strings.Strings;

public class BankIndexPacker {
	public static void serializeGZIPJava (final BankIndex index, final OutputStream os) throws IOException {
		final GZipOutputStream gzip = IO.newGZipStream(os);
		gzip.open();
		IO.serialize(index, gzip);
		gzip.close();
	}

	public static void serializeJson (final BankIndex index, final OutputStream os) throws IOException {
		final JsonString data = Json.serializeToString(index);
		final byte[] bytes = data.toString().getBytes();
		os.write(bytes);
	}

	public static BankIndex deSerializeJson (final InputStream is) throws IOException {
		final ByteArray data = is.readAll();
		final String raw_json_string = Strings.newString(data);
		final JsonString string = Json.newJsonString(raw_json_string);
		return Json.deserializeFromString(BankIndex.class, string);
	}

	public static BankIndex deSerializeGZIPJava (final InputStream is) throws IOException {
		final GZipInputStream gzip = IO.newGZipStream(is);
		gzip.open();
		final BankIndex index = IO.deserialize(BankIndex.class, gzip);
		gzip.close();
		return index;
	}
}
