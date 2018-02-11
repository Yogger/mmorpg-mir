package com.mmorpg.mir.model.welfare.event;

import com.mmorpg.mir.model.commonactivity.model.RecollectType;
import com.mmorpg.mir.model.gameobjects.Player;
import com.windforce.common.event.event.IEvent;

/**
 * 国家战事 次数计数事件
 */
public class ClawDoneTodayNumEvent implements IEvent {

	private RecollectType type;
	
	private long playerId;
	
	public static ClawDoneTodayNumEvent valueOf(Player player, RecollectType type) {
		ClawDoneTodayNumEvent e = new ClawDoneTodayNumEvent();
		e.playerId = player.getObjectId();
		e.type = type;
		return e;
	}

	@Override
	public long getOwner() {
		return playerId;
	}

	public RecollectType getType() {
		return type;
	}

	public void setType(RecollectType type) {
		this.type = type;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

}
