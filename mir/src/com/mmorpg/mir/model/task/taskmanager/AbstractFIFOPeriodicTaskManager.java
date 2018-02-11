package com.mmorpg.mir.model.task.taskmanager;

import java.util.LinkedList;
import java.util.Queue;

import org.apache.log4j.Logger;

public abstract class AbstractFIFOPeriodicTaskManager<T> extends AbstractPeriodicTaskManager {
	protected static final Logger log = Logger.getLogger(AbstractFIFOPeriodicTaskManager.class);

	private final Queue<T> queue = new LinkedList<T>();

	private final Queue<T> activeTasks = new LinkedList<T>();

	public AbstractFIFOPeriodicTaskManager(int period) {
		super(period);
	}

	public final void add(T t) {
		writeLock();
		try {
			queue.add(t);
		} finally {
			writeUnlock();
		}
	}

	@Override
	public final void run() {
		writeLock();
		try {
			activeTasks.addAll(queue);
			queue.clear();
		} finally {
			writeUnlock();
		}

		for (T task; (task = activeTasks.poll()) != null;) {
			try {
				callTask(task);
			} catch (RuntimeException e) {
				log.error("消息定时推送任务报错！", e);
			}
		}
	}

	protected abstract void callTask(T task);

	protected abstract String getCalledMethodName();
}
