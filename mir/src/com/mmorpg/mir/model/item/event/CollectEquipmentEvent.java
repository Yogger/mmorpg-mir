package com.mmorpg.mir.model.item.event;

import com.mmorpg.mir.model.item.Equipment;
import com.windforce.common.event.event.IEvent;

public class CollectEquipmentEvent implements IEvent {
	
	private long playerId;
	
	private Equipment equip;
	
	public static CollectEquipmentEvent valueOf(long pid, Equipment equip) {
		CollectEquipmentEvent event = new CollectEquipmentEvent();
		event.playerId = pid;
		event.equip = equip;
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

	public Equipment getEquip() {
		return equip;
	}

	public void setEquip(Equipment equip) {
		this.equip = equip;
	}

}
