package com.mmorpg.mir.model.utils;

import org.apache.log4j.Logger;

import com.mmorpg.mir.jmx.JourMBean;

public class ExecuteWrapper implements Runnable {
	private static final Logger log = Logger.getLogger(ExecuteWrapper.class);

	private final Runnable runnable;

	public ExecuteWrapper(Runnable runnable) {
		this.runnable = runnable;
	}

	@Override
	public final void run() {
		ExecuteWrapper.execute(runnable, getMaximumRuntimeInMillisecWithoutWarning(), getName());
	}

	protected String getName() {
		return runnable.getClass().getName();
	}

	protected long getMaximumRuntimeInMillisecWithoutWarning() {
		return Long.MAX_VALUE;
	}

	public static void execute(Runnable runnable, long maximumRuntimeInMillisecWithoutWarning, String name) {
		long begin = System.nanoTime();
		try {
			runnable.run();
		} catch (RuntimeException e) {
			log.error("Exception in a Runnable execution:", e);
		} finally {
			long use = System.nanoTime() - begin;

			boolean over = false;
			if (use > maximumRuntimeInMillisecWithoutWarning) {
				over = true;
			}

			JourMBean.getInstance().addTask(name, use, over);
		}
	}
}