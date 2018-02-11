package com.mmorpg.mir.model.utils;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class ThreadPoolManager {

	private static final long MAX_DELAY = TimeUnit.NANOSECONDS.toMillis(Long.MAX_VALUE - System.nanoTime()) / 2;

	private final ScheduledThreadPoolExecutor scheduledPool;

	private static final int THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors();

	private static final long TMAXIMUM_RUNTIME_IN_NANO_WITHOUT_WARNING = 1000000;

	/**
	 * @return ThreadPoolManager instance.
	 */
	private static final class SingletonHolder {
		private static final ThreadPoolManager INSTANCE = new ThreadPoolManager();
	}

	public static ThreadPoolManager getInstance() {
		return SingletonHolder.INSTANCE;
	}

	/**
	 * Constructor.
	 */
	private ThreadPoolManager() {
		scheduledPool = new ScheduledThreadPoolExecutor(THREAD_POOL_SIZE);
		scheduledPool.prestartAllCoreThreads();

		scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				purge();
			}
		}, 60000, 60000);
	}

	private final long validate(long delay) {
		return Math.max(0, Math.min(MAX_DELAY, delay));
	}

	// ===========================================================================================

	public final ScheduledFuture<?> schedule(Runnable r, long delay) {
		r = new ThreadPoolExecuteWrapper(r);
		delay = validate(delay);
		return new ScheduledFutureWrapper(scheduledPool.schedule(r, delay, TimeUnit.MILLISECONDS));
	}

	public final ScheduledFuture<?> scheduleWithName(Runnable r, long delay, String name) {
		r = new NamedThreadPoolExecuteWrapper(r, name);
		delay = validate(delay);
		return new ScheduledFutureWrapper(scheduledPool.schedule(r, delay, TimeUnit.MILLISECONDS));
	}

	public final ScheduledFuture<?> scheduleEffect(Runnable r, long delay) {
		return schedule(r, delay);
	}

	public final ScheduledFuture<?> scheduleGeneral(Runnable r, long delay) {
		return schedule(r, delay);
	}

	public final ScheduledFuture<?> scheduleAi(Runnable r, long delay) {
		return schedule(r, delay);
	}

	// ===========================================================================================

	public final ScheduledFuture<?> scheduleAtFixedRate(Runnable r, long delay, long period) {

		r = new ThreadPoolExecuteWrapper(r);
		delay = validate(delay);
		period = validate(period);

		return new ScheduledFutureWrapper(scheduledPool.scheduleAtFixedRate(r, delay, period, TimeUnit.MILLISECONDS));
	}

	public final ScheduledFuture<?> scheduleWithFixedDelay(Runnable r, long delay, long period) {

		r = new ThreadPoolExecuteWrapper(r);
		delay = validate(delay);
		period = validate(period);

		return new ScheduledFutureWrapper(scheduledPool.scheduleWithFixedDelay(r, delay, period, TimeUnit.MILLISECONDS));
	}

	public final ScheduledFuture<?> scheduleEffectAtFixedRate(Runnable r, long delay, long period) {
		return scheduleAtFixedRate(r, delay, period);
	}

	public final ScheduledFuture<?> scheduleGeneralAtFixedRate(Runnable r, long delay, long period) {
		return scheduleAtFixedRate(r, delay, period);
	}

	public final ScheduledFuture<?> scheduleAiAtFixedRate(Runnable r, long delay, long period) {
		return scheduleAtFixedRate(r, delay, period);
	}

	// ===========================================================================================

	public ScheduledFuture<?> scheduleTaskManager(Runnable r, long delay) {
		return schedule(r, delay);
	}

	public void purge() {
		scheduledPool.purge();
	}

	/**
	 * Shutdown all thread pools.
	 */
	public void shutdown() {

		scheduledPool.shutdown();

		try {
			awaitTermination(5000);

			scheduledPool.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
			scheduledPool.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);

			awaitTermination(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	private boolean awaitTermination(long timeoutInMillisec) throws InterruptedException {
		final long begin = System.currentTimeMillis();

		while (System.currentTimeMillis() - begin < timeoutInMillisec) {
			if (!scheduledPool.awaitTermination(10, TimeUnit.MILLISECONDS) && scheduledPool.getActiveCount() > 0)
				continue;

			return true;
		}

		return false;
	}

	private static class ThreadPoolExecuteWrapper extends ExecuteWrapper {
		private ThreadPoolExecuteWrapper(Runnable runnable) {
			super(runnable);
		}

		@Override
		protected long getMaximumRuntimeInMillisecWithoutWarning() {
			return TMAXIMUM_RUNTIME_IN_NANO_WITHOUT_WARNING;
		}
	}

	private static class NamedThreadPoolExecuteWrapper extends ThreadPoolExecuteWrapper {

		private final String name;

		public NamedThreadPoolExecuteWrapper(Runnable runnable, String name) {
			super(runnable);
			this.name = name;
		}

		@Override
		protected String getName() {
			return name;
		}
	}
}
