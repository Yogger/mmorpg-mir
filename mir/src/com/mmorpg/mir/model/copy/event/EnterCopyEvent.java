package com.mmorpg.mir.model.copy.event;

import com.windforce.common.event.event.IEvent;

public class EnterCopyEvent implements IEvent {

	public static final String EVENT_NAME = "copy:enter_copy";

	public static IEvent valueOf(long playerId, int mapId, int instanceId) {
		EnterCopyEvent event = new EnterCopyEvent();
		event.playerId = playerId;
		event.mapId = mapId;
		event.instanceId = instanceId;
		return event;
	}

	private long playerId;
	private int mapId;
	private int instanceId;

	public long getOwner() {
		return playerId;
	}

	public String getName() {
		return EVENT_NAME;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public int getMapId() {
		return mapId;
	}

	public void setMapId(int mapId) {
		this.mapId = mapId;
	}

	public int getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(int instanceId) {
		this.instanceId = instanceId;
	}

}