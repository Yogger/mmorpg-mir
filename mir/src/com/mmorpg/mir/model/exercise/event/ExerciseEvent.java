package com.mmorpg.mir.model.exercise.event;

import com.windforce.common.event.event.IEvent;

public class ExerciseEvent implements IEvent {

	public static IEvent valueOf(long playerId) {
		ExerciseEvent event = new ExerciseEvent();
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