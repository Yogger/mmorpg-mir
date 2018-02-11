package com.mmorpg.mir.model.welfare.event;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.purse.model.CurrencyType;
import com.windforce.common.event.event.IEvent;

/**
 * 消耗
 * 
 * @author 37wan
 * 
 */
public class CurrencyActionEvent implements IEvent {

	private long playerId;
	private CurrencyType type;
	private int value;
	private int activityValue;

	public static IEvent valueOf(Player player, CurrencyType type, int value) {
		CurrencyActionEvent event = new CurrencyActionEvent();
		event.setPlayerId(player.getObjectId());
		event.setType(type);
		event.setValue(value);
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

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public CurrencyType getType() {
		return type;
	}

	public void setType(CurrencyType type) {
		this.type = type;
	}

	public int getActivityValue() {
		return activityValue;
	}

	public void setActivityValue(int activityValue) {
		this.activityValue = activityValue;
	}

}
