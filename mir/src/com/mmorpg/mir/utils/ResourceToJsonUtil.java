package com.mmorpg.mir.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.windforce.common.resource.StorageManager;

public class ResourceToJsonUtil {
	/** 默认的上下文配置名 */
	private static final String DEFAULT_APPLICATION_CONTEXT = "ResourceContext.xml";

	public static void main(String[] args) throws IOException {
		String targetDirPath = "resource/resource";
		if (args != null && args.length == 1) {
			targetDirPath = args[0];
		}
		File targetDir = new File(targetDirPath);

		System.out.println(targetDir.getAbsolutePath());

		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				DEFAULT_APPLICATION_CONTEXT);
		StorageManager storageManager = (StorageManager) applicationContext.getBean("resourceManager");
		FileUtils.cleanDirectory(targetDir);
		long startTime = System.currentTimeMillis();
		try {
			storageManager.writeJson(targetDir.getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		}

		long endTime = System.currentTimeMillis();
		FileUtils.copyDirectory(new File("resource/resourceExcel/map"), new File(targetDirPath + "\\map"));
		FileUtils
				.copyFile(new File("resource/resourceExcel/words_chat.jour"), new File(targetDirPath + "\\words_chat.jour"));
		FileUtils
				.copyFile(new File("resource/resourceExcel/words_role.jour"), new File(targetDirPath + "\\words_role.jour"));

		System.out.println("生成json格式配置表已完成！耗时：" + (endTime - startTime) + "ms");

		Runtime.getRuntime().exit(0);

	}
}
