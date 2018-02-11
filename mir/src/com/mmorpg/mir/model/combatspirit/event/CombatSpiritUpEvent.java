package com.mmorpg.mir.model.combatspirit.event;

import com.windforce.common.event.event.IEvent;

public class CombatSpiritUpEvent implements IEvent {

	private long playerId;
	private int type;
	private String resourceId;

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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public static IEvent valueOf(Long objectId, String combatResourceId,
			int combatSpiritType) {
		CombatSpiritUpEvent event = new CombatSpiritUpEvent();
		event.playerId = objectId;
		event.resourceId = combatResourceId;
		event.type = combatSpiritType;
		return event;
	}

}
