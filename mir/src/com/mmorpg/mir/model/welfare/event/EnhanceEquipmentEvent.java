package com.mmorpg.mir.model.welfare.event;

import com.mmorpg.mir.model.gameobjects.Player;
import com.windforce.common.event.event.IEvent;

/**
 * 装备强化
 * 
 * @author 37wan
 * 
 */
public class EnhanceEquipmentEvent implements IEvent {

	private long playerId;
	
	private int enhanceAllCount;

	public static IEvent valueOf(Player player) {
		EnhanceEquipmentEvent event = new EnhanceEquipmentEvent();
		event.setPlayerId(player.getObjectId());
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

	public int getEnhanceAllCount() {
		return enhanceAllCount;
	}

	public void setEnhanceAllCount(int enhanceAllCount) {
		this.enhanceAllCount = enhanceAllCount;
	}

}
