package com.mmorpg.mir.model.exercise.event;

import com.windforce.common.event.event.IEvent;

public class ExerciseStartEvent implements IEvent{
	
	public static IEvent valueOf(long playerId) {
		ExerciseStartEvent event = new ExerciseStartEvent();
		event.playerId = playerId;
		return event;
	}

	private long playerId;

	public long getOwner() {
		return playerId;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

}
