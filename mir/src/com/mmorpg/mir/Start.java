package com.mmorpg.mir;

import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.windforce.common.utility.DateUtils;
import com.windforce.common.utility.JsonUtils;

public class Start {

	private static final Logger logger = LoggerFactory.getLogger(Start.class);
	/** 默认的上下文配置名 */
	private static final String DEFAULT_APPLICATION_CONTEXT = "applicationContext.xml";

	public static void main(String[] args) {
		ClassPathXmlApplicationContext applicationContext = null;
		long start = System.nanoTime();
		try {
			applicationContext = new ClassPathXmlApplicationContext(DEFAULT_APPLICATION_CONTEXT);
			// SpawnManager.getInstance().spawnAll();
			// CountryManager.getInstance().initAll();
			// BossManager.getInstance().initAll();
			// ExerciseManager.getInstance().spawnAll();
			// KingOfWarManager.getInstance().initSculptures();
			// WarshipManager.getInstance().spawnAll();
			// QuestEventManager.getInstance().reload();
			// SpringComponentStation.getNicknameManager().reload();
			// WorldRankManager.getInstance().initYesterDayRank();
			// MonsterRiotManager.getInstance().initAll();
			// CountryCopyManager.getInstance().initAll();
			// ResourceCheck.instance.checkAllResource();
			// PlayerReliveManager.getInstance().initAll();
			// GasCopyManager.getInstance().initialGasCopyMapTask();
			// MergeActiveManager.getInstance().initAll();
			// ServerState.getInstance().initAll();
			// ClientCenterSessionManager.getInstance().init();
			// AssassinManager.getInstance().initAll();
			// MinisterFeteManager.getInstance().initAll();
			// ServerConfigValue.versionMd5 = JsonUtils.object2String(args);
			System.gc();
		} catch (Exception e) {
			String message = MessageFormatter.format("", e.getMessage()).getMessage();
			logger.error(message, e);
			Runtime.getRuntime().exit(-1);
		}
		applicationContext.registerShutdownHook();
		applicationContext.start();
		System.gc();
		//
		// FirewallFilter firewallFilter =
		// applicationContext.getBean(FirewallFilter.class);
		// firewallFilter.blockAll();
		// ConsoleManager.getInstance().block();
		String message1 = MessageFormatter
				.format("服务器已经启动 - [{}]", DateUtils.date2String(new Date(), DateUtils.PATTERN_DATE_TIME)).getMessage();
		logger.error(message1);
		String message2 = MessageFormatter.format("服务器当前时区 - [{}]", TimeZone.getDefault()).getMessage();
		logger.error(message2);
		String message3 = MessageFormatter.format("服务器MD5 - [{}]", JsonUtils.object2String(args)).getMessage();
		logger.error(message3);
		long end = System.nanoTime() - start;
		logger.error("消耗了 " + TimeUnit.NANOSECONDS.toMillis(end) + " ms (╯﹏╰)");

		while (applicationContext.isActive()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				if (logger.isDebugEnabled()) {
					logger.debug("服务器主线程被非法打断", e);
				}
			}
		}

		while (applicationContext.isRunning()) {
			Thread.yield();
		}
		String message = MessageFormatter
				.format("服务器已经关闭 - [{}]", DateUtils.date2String(new Date(), DateUtils.PATTERN_DATE_TIME)).getMessage();
		logger.error(message);
		Runtime.getRuntime().exit(0);
	}
}
