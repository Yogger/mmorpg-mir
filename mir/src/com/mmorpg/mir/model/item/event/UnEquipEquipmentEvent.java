package com.mmorpg.mir.model.item.event;

import com.mmorpg.mir.model.gameobjects.Player;
import com.windforce.common.event.event.IEvent;

public class UnEquipEquipmentEvent implements IEvent{

	private long playerId;
	
	public static UnEquipEquipmentEvent valueOf(Player player) {
		UnEquipEquipmentEvent e = new UnEquipEquipmentEvent();
		e.playerId = player.getObjectId();
		return e;
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

}
