package com.mmorpg.mir.model.item.model;

import com.mmorpg.mir.model.item.AbstractItem;

public class PlayerTreasureInfo {
	/** 玩家名字 */
	private String playerName;
	/** 收集到的装备 */
	private AbstractItem item;

	public static PlayerTreasureInfo valueOf(String playerName, AbstractItem item) {
		PlayerTreasureInfo result = new PlayerTreasureInfo();
		result.playerName = playerName;
		result.item = item;
		return result;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public AbstractItem getItem() {
		return item;
	}

	public void setItem(AbstractItem item) {
		this.item = item;
	}

}
