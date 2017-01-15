
package com.jfixby.r3.fokker.fs;

import java.io.IOException;

import com.jfixby.rana.api.pkg.fs.PackageDescriptor;
import com.jfixby.scarabei.api.base64.Base64;
import com.jfixby.scarabei.api.collections.Collections;
import com.jfixby.scarabei.api.collections.List;
import com.jfixby.scarabei.api.err.Err;
import com.jfixby.scarabei.api.file.FilesList;
import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.java.ByteArray;
import com.jfixby.scarabei.api.util.JUtils;
import com.jfixby.scarabei.api.util.path.RelativePath;

public class GdxAssetsFileSystemPacker {

	private static boolean compress = false;

	public static void index (final File input, final File output) throws IOException {
		final GdxAssetsFileSystemIndex index = new GdxAssetsFileSystemIndex();
		// index//
		final File index_file = input.child(GdxAssetsFileSystemIndex.INDEX_FILE_NAME);
		{

			final RelativePath path = JUtils.newRelativePath();
			indexFolder(index, input, path, index_file);

			// String string_data = Json.serializeToString(index);
			index_file.writeData(index);
			Collections.newList(index.index).print("index");

		}
		// pack
		final boolean collapse_folders = false;
		if (collapse_folders) {
			output.makeFolder();
			final List<GdxAssetsFileSystemIndexEntry> index_list = Collections.newList(index.index);
			for (int i = 0; i < index_list.size(); i++) {
				final GdxAssetsFileSystemIndexEntry entry_i = index_list.getElementAt(i);
				final RelativePath relative = JUtils.newRelativePath(entry_i.path);
				final File file_i = input.proceed(relative);
				if (!file_i.exists()) {
					Err.reportError("Index is corrupted: " + file_i);
				}
				if (file_i.isFile()) {
					if (index_file.equals(file_i)) {
						index_file.getFileSystem().copyFileToFolder(file_i, output);
						continue;
					}

					final String child_name = internalFileName(relative);
					final File file_o = output.child(child_name);
					file_i.getFileSystem().copyFileToFile(file_i, file_o);

					// applyBase64(file_o);

				}

			}
		} else {
			output.getFileSystem().copyFolderContentsToFolder(input, output);
		}
		fixTextures(output);

	}

	private static void fixTextures (final File output) throws IOException {
		final FilesList children = output.listDirectChildren();
		for (int i = 0; i < children.size(); i++) {
			final File child = children.getElementAt(i);
			if (child.isFolder()) {
				fixTextures(child);
			} else if (child.isFile()) {
				if (child.getName().endsWith(PackageDescriptor.PACKAGE_DESCRIPTOR_FILE_NAME)) {
					// String data = child.readToString();
					// data =
					// data.replaceAll(StandardPackageFormats.libGDX.Atlas +
					// ",",
					// StandardPackageFormats.libGDX.Atlas_GWT + ",");
					// data =
					// data.replaceAll(StandardPackageFormats.libGDX.Texture +
					// ",",
					// StandardPackageFormats.libGDX.Texture_GWT + ",");
					// child.writeString(data);
				}
				if (child.extensionIs("png")) {
					if (compress) {
// L.d("PNG", child);
// final long originalSize = child.getSize();
// IndexedCompressor.indexImage(child, child);
// final long compressedSize = child.getSize();
// L.d(" originalSize", (originalSize / 1024) + "Kb");
// L.d(" compressedSize", (compressedSize / 1024) + "Kb");
// L.d(" compression", FloatMath.roundToDigit(compressedSize * 100d / originalSize, 2) + "%");
					}
				}
			}
		}
	}

	private static void applyBase64 (final File file_o) throws IOException {
		final ByteArray data = file_o.readBytes();
		final String base64_string = Base64.encode(data);
		file_o.writeString(base64_string);
	}

	private static void indexFolder (final GdxAssetsFileSystemIndex index, final File root_folder, final RelativePath path,
		final File index_file) throws IOException {
		// L.d("folder", );
		index.registerFolder(path);

		// List<File> children = Collections.filter(root_folder.listChildren(),
		// file -> !file.getName().startsWith("."));
		final FilesList children = root_folder.listDirectChildren();
		for (int i = 0; i < children.size(); i++) {
			final File child_file = children.getElementAt(i);
			final String name = child_file.getName();
			if (child_file.isFolder()) {
				indexFolder(index, child_file, path.child(name), null);
			} else if (child_file.isFile()) {
				if (!child_file.equals(index_file) || true) {
					index.registerFile(path.child(name));
				}
			}
		}
	}

	public static String internalFileName (final RelativePath path) {
		// if (path.isRoot()) {
		// return MD5.md5String(path + SALT);
		// }
		// return MD5.md5String(path + SALT) + "." + path.getLastStep();
		// return MD5.md5String(path + SALT);
		// return path.toString().replaceAll("/", "#");
		return path.toString();
	}
}
