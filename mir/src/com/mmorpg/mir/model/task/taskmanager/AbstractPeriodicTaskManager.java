package com.mmorpg.mir.model.task.taskmanager;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;

import com.mmorpg.mir.model.utils.ThreadPoolManager;

public abstract class AbstractPeriodicTaskManager extends AbstractLockManager implements Runnable {
	protected static final Logger log = Logger.getLogger(AbstractPeriodicTaskManager.class);

	private final int period;

	public AbstractPeriodicTaskManager(int period) {
		this.period = period;
	}

	@PostConstruct
	public void init() {
		ThreadPoolManager.getInstance().scheduleAtFixedRate(this, 1000, period);
	}

	@Override
	public abstract void run();
}
