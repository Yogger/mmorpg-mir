package com.mmorpg.mir.model.moduleopen.event;

import com.windforce.common.event.event.IEvent;

public class ModuleOpenEvent implements IEvent {

	private long playerId;

	private String moduleResourceId;
	
	public static ModuleOpenEvent valueOf(long playerId, String key) {
		ModuleOpenEvent event = new ModuleOpenEvent();
		event.playerId = playerId;
		event.moduleResourceId = key;
		return event;
	}

	@Override
	public long getOwner() {
		return playerId;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public String getModuleResourceId() {
		return moduleResourceId;
	}

	public void setModuleResourceId(String moduleResourceId) {
		this.moduleResourceId = moduleResourceId;
	}

}
