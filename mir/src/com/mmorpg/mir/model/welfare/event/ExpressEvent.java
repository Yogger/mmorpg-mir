package com.mmorpg.mir.model.welfare.event;

import com.mmorpg.mir.model.gameobjects.Player;
import com.windforce.common.event.event.IEvent;

/**
 * 运镖
 * 
 * @author 37wan
 * 
 */
public class ExpressEvent implements IEvent {

	private long playerId;

	private String exprssId;

	public static IEvent valueOf(Player player, String expressId) {
		ExpressEvent event = new ExpressEvent();
		event.setPlayerId(player.getObjectId());
		event.exprssId = expressId;
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

	public String getExprssId() {
		return exprssId;
	}

	public void setExprssId(String exprssId) {
		this.exprssId = exprssId;
	}

}
