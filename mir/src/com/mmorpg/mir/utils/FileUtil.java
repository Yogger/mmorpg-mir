package com.mmorpg.mir.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public final class FileUtil {

	public static boolean createFile(File file) throws Exception {
		if (!file.exists()) {
			mkdir(file.getParentFile());
		}
		return file.createNewFile();
	}

	public static void mkdir(File file) {
		if (!file.getParentFile().exists()) {
			mkdir(file.getParentFile());
		}
		file.mkdir();
	}

	private static void doWithFile(final File file, List<File> files) throws Exception {
		if (file == null) {
			return;
		}
		if (file.isHidden()) {
			return;
		}
		if (file.isDirectory()) {
			for (File temp : file.listFiles()) {
				doWithFile(temp, files);
			}
		} else {
			BufferedReader br = null;
			try {
				br = new BufferedReader(new FileReader(file));
				String line = null;
				while ((line = br.readLine()) != null) {
					if (line.equals("@DynamicAnno")) {
						files.add(file);
						break;
					}
				}
			} finally {
				br.close();
			}
		}
	}

	private static void copyFile(File src, File des) throws Exception {
		FileChannel in = null;
		FileChannel out = null;
		try {
			in = (new FileInputStream(src)).getChannel();
			out = (new FileOutputStream(des)).getChannel();
			in.transferTo(0, in.size(), out);
		} finally {
			in.close();
			out.close();
		}
	}

	private static String getCopyFilePath(File file, File srcFile, File desFile) {
		String fp = file.getAbsolutePath();
		String srcp = srcFile.getAbsolutePath();
		String desp = desFile.getAbsolutePath();
		return desp + fp.substring(srcp.length());
	}

	public static void main(String[] args) {

		try {
			File srcDir = new File(args[0]);
			File desDir = new File(args[1]);

			List<File> files = new ArrayList<File>(10);

			doWithFile(srcDir, files);

			for (File file : files) {
				System.out.println("发现一个脚本化管理器 : " + file.getCanonicalPath());
				String copyFilePath = getCopyFilePath(file, srcDir, desDir);
				System.out.println("脚本生成位置 : " + copyFilePath);
				File copyFile = new File(copyFilePath);
				createFile(copyFile);
				copyFile(file, copyFile);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
