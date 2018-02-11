package com.mmorpg.mir.model.welfare.event;

import com.mmorpg.mir.model.gameobjects.Player;
import com.windforce.common.event.event.IEvent;

/**
 * 微端登录
 * 
 * @author 37wan
 * 
 */
public class WeiLoginEvent implements IEvent {

	private long playerId;

	public static IEvent valueOf(Player player) {
		WeiLoginEvent event = new WeiLoginEvent();
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

}
