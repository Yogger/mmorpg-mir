package com.mmorpg.mir.model.item.packet;

import java.util.Map;

import com.mmorpg.mir.model.gameobjects.Player;

public class SM_Attach_SuicideState {
	private Map<Integer, Object> equipUpdate;
	private Map<Integer, Object> packUpdate;

	private boolean randSucc;

	public static SM_Attach_SuicideState valueOf(Player player, boolean randSucc) {
		SM_Attach_SuicideState result = new SM_Attach_SuicideState();
		result.equipUpdate = player.getEquipmentStorage().collectUpdate();
		result.packUpdate = player.getPack().collectUpdate();
		result.randSucc = randSucc;
		return result;
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

	public boolean isRandSucc() {
		return randSucc;
	}

	public void setRandSucc(boolean randSucc) {
		this.randSucc = randSucc;
	}

}
