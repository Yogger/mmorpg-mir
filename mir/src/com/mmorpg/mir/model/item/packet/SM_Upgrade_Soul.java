package com.mmorpg.mir.model.item.packet;

import java.util.Map;

import com.mmorpg.mir.model.gameobjects.Player;

public class SM_Upgrade_Soul {
	private Map<Integer, Object> equipUpdate;

	private Map<Integer, Object> packUpdate;

	public static SM_Upgrade_Soul valueOf(Player player) {
		SM_Upgrade_Soul sm = new SM_Upgrade_Soul();
		sm.equipUpdate = player.getEquipmentStorage().collectUpdate();
		sm.packUpdate = player.getPack().collectUpdate();
		return sm;
	}

	public Map<Integer, Object> getEquipUpdate() {
		return equipUpdate;
	}

	public void setEquipUpdate(Map<Integer, Object> equipUpdate) {
		this.equipUpdate = equipUpdate;
	}

	public Map<Integer, Object> getPackUpdate() {
		return packUpdate;
	}

	public void setPackUpdate(Map<Integer, Object> packUpdate) {
		this.packUpdate = packUpdate;
	}

}
