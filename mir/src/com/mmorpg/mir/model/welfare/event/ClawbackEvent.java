package com.mmorpg.mir.model.welfare.event;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.welfare.model.ClawbackEnum;
import com.windforce.common.event.event.IEvent;

public class ClawbackEvent implements IEvent {

	private long playerId;
	
	private ClawbackEnum type;
	
	private int count;
	
	public static ClawbackEvent valueOf(Player player, ClawbackEnum type) {
		ClawbackEvent e = new ClawbackEvent();
		e.playerId = player.getObjectId();
		e.type  = type;
		e.count = player.getWelfare().getWelfareHistory().getClawbackNum(type);
		return e;
	}
	
	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	@Override
	public long getOwner() {
		return playerId;
	}

	public ClawbackEnum getType() {
		return type;
	}

	public void setType(ClawbackEnum type) {
		this.type = type;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
