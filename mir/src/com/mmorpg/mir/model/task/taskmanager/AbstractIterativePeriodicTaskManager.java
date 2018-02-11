package com.mmorpg.mir.model.task.taskmanager;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author NB4L1
 */
public abstract class AbstractIterativePeriodicTaskManager<T> extends AbstractPeriodicTaskManager {
	private final Queue<T> startList = new LinkedList<T>();
	private final Queue<T> stopList = new LinkedList<T>();

	private final Queue<T> activeTasks = new LinkedList<T>();

	protected AbstractIterativePeriodicTaskManager(int period) {
		super(period);
	}

	public boolean hasTask(T task) {
		readLock();
		try {
			if (stopList.contains(task))
				return false;

			return activeTasks.contains(task) || startList.contains(task);
		} finally {
			readUnlock();
		}
	}

	public void startTask(T task) {
		writeLock();
		try {
			startList.add(task);

			stopList.remove(task);
		} finally {
			writeUnlock();
		}
	}

	public void stopTask(T task) {
		writeLock();
		try {
			stopList.add(task);

			startList.remove(task);
		} finally {
			writeUnlock();
		}
	}

	@Override
	public final void run() {
		writeLock();
		try {
			activeTasks.addAll(startList);
			activeTasks.removeAll(stopList);

			startList.clear();
			stopList.clear();
		} finally {
			writeUnlock();
		}

		for (T task : activeTasks) {
			try {
				callTask(task);
			} catch (RuntimeException e) {
				log.warn("", e);
			}
		}
	}

	protected abstract void callTask(T task);

	protected abstract String getCalledMethodName();
}
