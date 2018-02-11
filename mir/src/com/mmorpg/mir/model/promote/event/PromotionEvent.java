package com.mmorpg.mir.model.promote.event;

import com.mmorpg.mir.model.gameobjects.Player;
import com.windforce.common.event.event.IEvent;

public class PromotionEvent implements IEvent {

	private long playerId;
	
	private int stage;
	
	@Override
	public long getOwner() {
		return playerId;
	}

	public static PromotionEvent valueOf(Player player) {
		PromotionEvent event = new PromotionEvent();
		event.playerId = player.getObjectId();
		event.stage = player.getPromotion().getStage();
		return event;
	}
	
	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public int getStage() {
		return stage;
	}

	public void setStage(int stage) {
		this.stage = stage;
	}
	
}
