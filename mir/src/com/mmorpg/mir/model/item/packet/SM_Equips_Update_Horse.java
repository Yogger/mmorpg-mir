package com.mmorpg.mir.model.item.packet;

import java.util.Map;

import com.mmorpg.mir.model.gameobjects.Player;

public class SM_Equips_Update_Horse {
	private String resultKey;
	private Map<Integer, Object> equipUpdate;
	private Map<Integer, Object> packUpdate;

	public static SM_Equips_Update_Horse valueOf(Player player, String resultKey) {
		SM_Equips_Update_Horse result = new SM_Equips_Update_Horse();
		result.resultKey = resultKey;
		result.equipUpdate = player.getHorseEquipmentStorage().collectUpdate();
		result.packUpdate = player.getPack().collectUpdate();
		return result;
	}

	public String getResultKey() {
		return resultKey;
	}

	public void setResultKey(String resultKey) {
		this.resultKey = resultKey;
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
