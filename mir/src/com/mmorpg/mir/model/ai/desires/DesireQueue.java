package com.mmorpg.mir.model.ai.desires;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.PriorityQueue;

public class DesireQueue {

	protected PriorityQueue<Desire> queue;

	public synchronized Desire peek() {
		return queue != null ? queue.peek() : null;
	}

	public synchronized Desire poll() {
		if (queue != null) {
			return queue.poll();
		}

		return null;
	}

	public synchronized void addDesire(Desire desire) {
		// Lazy initialization of desire queue
		if (queue == null) {
			queue = new PriorityQueue<Desire>();
		}

		// Iterate over the list to find similiar desires
		Iterator<Desire> iterator = queue.iterator();
		while (iterator.hasNext()) {
			Desire iterated = iterator.next();

			// Find similiar desires by #equals method, they can be different
			// instances.
			if (desire.equals(iterated)) {
				// Remove the old desire from the list
				iterator.remove();

				// If current desire instance was not at the list - increase
				// it's power
				// by the value of another instance power
				// and after that add it to the list
				if (desire != iterated) {
					desire.increaseDesirePower(iterated.getDesirePower());
				}

				// Break iteration, desire list can't contain two same desires
				break;
			}
		}
		// finally add desire to the list
		queue.add(desire);
	}

	public synchronized boolean removeDesire(Desire desire) {
		return queue != null && queue.remove(desire);
	}

	public synchronized void iterateDesires(DesireIteratorHandler handler, DesireIteratorFilter... filters)
			throws ConcurrentModificationException {

		if (queue == null) {
			return;
		}

		Iterator<Desire> iterator = queue.iterator();
		outer: while (iterator.hasNext()) {
			Desire desire = iterator.next();

			if (filters != null && filters.length > 0) {
				for (DesireIteratorFilter filter : filters) {
					if (!filter.isOk(desire)) {
						continue outer;
					}
				}
			}

			handler.next(desire, iterator);
		}
	}

	/**
	 * Returns true if this desire list contains same desire. Desires are
	 * compared by {@link AbstractDesire#equals(Object)} method.
	 * 
	 * @param desire
	 *            what desire to search
	 * @return true if there is equal desire, false in other case.
	 */
	public synchronized boolean contains(Desire desire) {
		return queue.contains(desire);
	}

	/**
	 * Returns true if this npc has no any desires added
	 * 
	 * @return true if this npc has no any desires added
	 */
	public synchronized boolean isEmpty() {
		return queue == null || queue.isEmpty();
	}

	/**
	 * Clears all desires
	 */
	public synchronized void clear() {
		if (queue != null) {
			Desire desire = null;
			while ((desire = queue.poll()) != null)
				desire.onClear();
		}
	}

	/**
	 * Returns size of the desire list
	 * 
	 * @return size of remaining desires
	 */
	public synchronized int size() {
		return queue == null ? 0 : queue.size();
	}
}