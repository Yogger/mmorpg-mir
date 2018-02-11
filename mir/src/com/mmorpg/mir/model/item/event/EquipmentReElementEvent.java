package com.mmorpg.mir.model.item.event;

import com.mmorpg.mir.model.gameobjects.Player;
import com.windforce.common.event.event.IEvent;

public class EquipmentReElementEvent implements IEvent{

	private long owner;
	
	public static EquipmentReElementEvent valueOf(Player player) {
		EquipmentReElementEvent e = new EquipmentReElementEvent();
		e.owner = player.getObjectId();
		return e;
	}
	
	@Override
    public long getOwner() {
		return owner;
    }

	public void setOwner(long owner) {
    	this.owner = owner;
    }
	
}
