package com.mmorpg.mir.model.welfare.event;

import com.mmorpg.mir.model.gameobjects.Player;
import com.windforce.common.event.event.IEvent;

/**
 * 太庙
 * 
 * @author 37wan
 * 
 */
public class TempleEvent implements IEvent {

	private long playerId;
	private boolean fail;

	public static IEvent valueOf(Player player, boolean fail) {
		TempleEvent event = new TempleEvent();
		event.setPlayerId(player.getObjectId());
		event.fail = fail;
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

	public boolean isFail() {
		return fail;
	}

	public void setFail(boolean fail) {
		this.fail = fail;
	}

}
