package com.mmorpg.mir.model.item.event;

import com.windforce.common.event.event.IEvent;

public class ExtendsEquipmentEvent implements IEvent{
	
	private long playerId;

	public static ExtendsEquipmentEvent valueOf(long pid) {
		ExtendsEquipmentEvent e = new ExtendsEquipmentEvent();
		e.playerId = pid;
		return e;
	}
	
	@Override
    public long getOwner() {
		return playerId;
    }
	
}
