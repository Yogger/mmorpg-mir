package com.mmorpg.mir.model.item.event;

import com.mmorpg.mir.model.gameobjects.Player;
import com.windforce.common.event.event.IEvent;

public class EquipEquipmentEvent implements IEvent {

	private long owner;
	
	private int enhanceAllCount;
	
	public static EquipEquipmentEvent valueOf(Player player) {
		EquipEquipmentEvent e = new EquipEquipmentEvent();
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

	public int getEnhanceAllCount() {
		return enhanceAllCount;
	}

	public void setEnhanceAllCount(int enhanceAllCount) {
		this.enhanceAllCount = enhanceAllCount;
	}

}
