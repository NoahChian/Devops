package org.iii.ideas.catering_service.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.apache.log4j.Logger;

public class ZipUtils {

	private static Logger logger = Logger.getLogger(ZipUtils.class);

	public static boolean unzip(String zipFilePath, String targetPath) {
		logger.info("unzip file:" + targetPath);
		File file = new File(zipFilePath);
		boolean result = false;
		result = unpack(file, targetPath);
		logger.info("unzip done file:" + targetPath);
		return result;
	}

	/**
	 * 该方法是直接通过ZipFile的entries()方法拿到包含所有的ZipEntry对象的Enumeration对象， 接下来的操作和上面的是一样的
	 * 
	 * @param zipFile
	 * @throws IOException
	 */
	public static boolean unpack(File file, String targetPath) {
		boolean result = true;
		Charset charset = Charset.forName("Big5");
		File target = null;
		int len = 0;
		try {
			try (ZipFile zipFile = new ZipFile(file, charset)) {
				try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(file), charset)) {
					ZipEntry zipEntry = null;
					Path targetFilePath = null;
					while ((zipEntry = zipInputStream.getNextEntry()) != null) {
						targetFilePath = Paths.get(targetPath, zipEntry.getName());
						if (zipEntry.getName().endsWith("/")) {
							Files.createDirectories(targetFilePath);
							continue;
						}

						target = new File(targetFilePath.toString());
						try (OutputStream os = new FileOutputStream(target)) {
							try (InputStream is = zipFile.getInputStream(zipEntry)) {
								len = 0;
								while ((len = is.read()) != -1) {
									os.write(len);
								}
								// is.close();
							}
							// os.close();
						}
						// zipInputStream.close();
					}
				}
			}
			// zipFile.close();
		} catch (Exception e) {
			logger.error(e);
			result = false;
		}

		return result;
	}
}
