package org.openecomp.portalapp.widget.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.openecomp.portalapp.widget.constant.WidgetConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnzipUtil {

	/**
	 * Size of the buffer to read/write data
	 */
	private static final int BUFFER_SIZE = 4096;
	private static final Logger logger = LoggerFactory.getLogger(UnzipUtil.class);

	/**
	 * Extracts a zip file specified by the zipFilePath to a directory specified by
	 * destDirectory (will be created if does not exists)
	 * 
	 * @param zipFilePath
	 *            path
	 * @param destDirectory
	 *            directory
	 * @param widgetName
	 *            name
	 * @return Map of contents
	 * @throws IOException
	 *             On error
	 */
	public Map<String, byte[]> unzip_db(String zipFilePath, String destDirectory, String widgetName)
			throws IOException {

		logger.debug("UnzipUtil.unzip_db: unzip widget file {}", widgetName);
		File destDir = new File(destDirectory);
		if (!destDir.exists())
			destDir.mkdir();
		ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
		ZipEntry entry = zipIn.getNextEntry();
		Map<String, byte[]> map = new HashMap<>();

		String[] requiredKeys = { WidgetConstant.WIDGET_CONTROLLER_LOCATION, WidgetConstant.WIDGET_MARKUP_LOCATION,
				WidgetConstant.WIDGET_STYLE_LOCATION };
		for (String k : requiredKeys)
			map.put(k, null);

		// iterates over entries in the zip file
		Stack<File> stack = new Stack<>();
		while (entry != null) {
			String filePath = destDirectory + File.separator + widgetName + File.separator
					+ entry.getName().substring(entry.getName().indexOf("/") + 1);
			final String entryShortName = entry.getName().substring(entry.getName().indexOf("/") + 1);
			logger.debug("UnzipUtil.unzip_db: file path {}, short name {}", filePath, entryShortName);
			if (!entry.isDirectory()) {
				// if the entry is a file, extracts it
				logger.debug("UnzipUtil.unzip_db: unzip and save widget file {}", filePath);
				stack.push(new File(filePath));
				extractFile(zipIn, filePath);
			} else {
				// if the entry is a directory, make the directory
				logger.debug("UnzipUtil.unzip_db: unzip and create widget folder {}", filePath);
				File dir = new File(filePath);
				stack.push(new File(filePath));
				dir.mkdir();
			}
			// Is this one we need?
			if (map.containsKey(entryShortName))
				map.put(entryShortName, Files.readAllBytes(Paths.get(filePath)));
			zipIn.closeEntry();
			entry = zipIn.getNextEntry();
		}
		zipIn.close();
		while (!stack.isEmpty())
			stack.pop().delete();

		for (String k : requiredKeys)
			if (!map.containsKey(k))
				logger.warn("UnzipUtil.unzip_db: no zip archive entry found for required key {}", k);

		return map;
	}

	/**
	 * Extracts a zip entry (file entry)
	 * 
	 * @param zipIn
	 * @param filePath
	 * @throws IOException
	 */
	private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
		byte[] bytesIn = new byte[BUFFER_SIZE];
		int read = 0;
		while ((read = zipIn.read(bytesIn)) != -1) {
			bos.write(bytesIn, 0, read);
		}
		bos.close();
	}
}