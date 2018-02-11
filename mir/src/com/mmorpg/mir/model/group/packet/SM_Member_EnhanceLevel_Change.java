package com.mmorpg.mir.model.group.packet;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.item.model.EquipmentStorageType;

public class SM_Member_EnhanceLevel_Change {

	private long playerId;
	private int enhanceLevel;
	private int equipStorageType;

	public static SM_Member_EnhanceLevel_Change valueOf(Player player, EquipmentStorageType equipStorageType) {
		SM_Member_EnhanceLevel_Change sm = new SM_Member_EnhanceLevel_Change();
		sm.playerId = player.getObjectId();
		sm.enhanceLevel = equipStorageType.getEquipmentStorage(player).getSuitStarLevel();
		sm.equipStorageType = equipStorageType.getWhere();
		return sm;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public int getEnhanceLevel() {
		return enhanceLevel;
	}

	public void setEnhanceLevel(int enhanceLevel) {
		this.enhanceLevel = enhanceLevel;
	}

	public int getEquipStorageType() {
		return equipStorageType;
	}

	public void setEquipStorageType(int equipStorageType) {
		this.equipStorageType = equipStorageType;
	}

}
