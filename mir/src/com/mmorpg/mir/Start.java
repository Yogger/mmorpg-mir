package com.mmorpg.mir;

import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.mmorpg.mir.common.session.FirewallManagerImpl;
import com.windforce.common.threadpool.IdentityEventExecutorGroup;
import com.windforce.common.utility.DateUtils;
import com.windforce.common.utility.JsonUtils;
import com.windforce.filter.firewall.FirewallManager;
import com.windforce.server.Wserver;

public class Start {

	private static final Logger logger = LoggerFactory.getLogger(Start.class);
	/** 默认的上下文配置名 */
	private static final String DEFAULT_APPLICATION_CONTEXT = "applicationContext.xml";

	public static void main(String[] args) {
		ClassPathXmlApplicationContext applicationContext = null;
		long start = System.nanoTime();
		try {
			IdentityEventExecutorGroup.init(24);
			applicationContext = new ClassPathXmlApplicationContext(DEFAULT_APPLICATION_CONTEXT);

			// 最后启动通讯组件
			Wserver wserver = applicationContext.getBean(Wserver.class);
			FirewallManager firewallManager = new FirewallManagerImpl();
			wserver.bind(firewallManager);
			System.gc();
		} catch (Throwable e) {
			String message = MessageFormatter.format("", e.getMessage()).getMessage();
			logger.error(message, e);
			Runtime.getRuntime().exit(-1);
		}
		applicationContext.registerShutdownHook();
		applicationContext.start();
		System.gc();
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
