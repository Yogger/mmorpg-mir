package com.mmorpg.mir.model.ai;

import java.util.concurrent.Future;

import org.cliffc.high_scale_lib.NonBlockingHashMap;

import com.mmorpg.mir.model.ai.desires.Desire;
import com.mmorpg.mir.model.ai.desires.DesireQueue;
import com.mmorpg.mir.model.ai.desires.impl.CounterBasedDesireFilter;
import com.mmorpg.mir.model.ai.desires.impl.GeneralDesireIteratorHandler;
import com.mmorpg.mir.model.ai.event.Event;
import com.mmorpg.mir.model.ai.event.IEventHandler;
import com.mmorpg.mir.model.ai.state.AIState;
import com.mmorpg.mir.model.ai.state.IStateHandler;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.utils.ThreadPoolManager;

public abstract class AI implements Runnable {

	/**
	 * Dummy ai that has no any event handlers
	 */
	private static final DummyAi dummyAi = new DummyAi();

	protected NonBlockingHashMap<Event, IEventHandler> eventHandlers = new NonBlockingHashMap<Event, IEventHandler>();
	protected NonBlockingHashMap<AIState, IStateHandler> stateHandlers = new NonBlockingHashMap<AIState, IStateHandler>();

	protected Creature owner;

	protected AIState aiState = AIState.NONE;

	protected DesireQueue desireQueue = new DesireQueue();

	protected boolean isStateChanged = false;

	private Future<?> aiTask;

	private int interval = 1000;

	/**
	 * 
	 * @param event
	 *            The event that needs to be handled
	 */
	public synchronized void handleEvent(Event event) {
		if (event != Event.DIED && event != Event.DELETE && owner.getLifeStats() != null
				&& owner.getLifeStats().isAlreadyDead())
			return;

		IEventHandler eventHandler = eventHandlers.get(event);
		if (eventHandler != null) {
			eventHandler.handleEvent(event, this);
		}

	}

	/**
	 * @return owner of this AI
	 */
	public Creature getOwner() {
		return owner;
	}

	/**
	 * of the AI
	 * 
	 * @param owner
	 *            the owner of the AI
	 */
	public void setOwner(Creature owner) {
		this.owner = owner;
	}

	/**
	 * @return the aiState
	 */
	public AIState getAiState() {
		return aiState;
	}

	/**
	 * 
	 * @param eventHandler
	 */
	protected void addEventHandler(IEventHandler eventHandler) {
		this.eventHandlers.put(eventHandler.getEvent(), eventHandler);
	}

	protected void addStateHandler(IStateHandler stateHandler) {
		this.stateHandlers.put(stateHandler.getState(), stateHandler);
	}

	/**
	 * @param aiState
	 *            the aiState to set
	 */
	public synchronized void setAiState(AIState aiState) {
		if (this.aiState == AIState.DELETE)
			return;
		if (owner.getLifeStats() != null && owner.getLifeStats().isAlreadyDead()) {
			if (aiState != AIState.NONE && aiState != AIState.DELETE) {
				return;
			}
		}
		if (this.aiState != aiState) {
			this.aiState = aiState;
			isStateChanged = true;
		}
	}

	public void analyzeState() {
		isStateChanged = false;
		IStateHandler stateHandler = stateHandlers.get(aiState);
		if (stateHandler != null) {
			stateHandler.handleState(aiState, this);
		}
	}

	@Override
	public void run() {
		desireQueue.iterateDesires(new GeneralDesireIteratorHandler(this), new CounterBasedDesireFilter());
		// TODO: move to home
		boolean change = isStateChanged;
		if (desireQueue.isEmpty() || change) {
			analyzeState();
		}

		if (change && aiState == AIState.MOVINGTOHOME) {
			run();
		}
	}

	public void clearDesires() {
		this.desireQueue.clear();
	}

	public void addDesire(Desire desire) {
		this.desireQueue.addDesire(desire);
	}

	public int desireQueueSize() {
		return desireQueue.isEmpty() ? 0 : desireQueue.size();
	}

	public synchronized void schedule() {
		if (!isScheduled()) {
			aiTask = ThreadPoolManager.getInstance().scheduleWithFixedDelay(this, interval, interval);
		}
	}

	public synchronized void stop() {
		if (isScheduled()) {
			aiTask.cancel(true);
			aiTask = null;
		}
	}

	private boolean isScheduled() {
		return aiTask != null && !aiTask.isCancelled();
	}

	/**
	 * @return the dummyai
	 */
	public static DummyAi dummyAi() {
		return dummyAi;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

}
