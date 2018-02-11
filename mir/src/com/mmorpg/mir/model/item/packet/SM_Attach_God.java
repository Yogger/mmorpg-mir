package com.mmorpg.mir.model.item.packet;

import java.util.Map;

import com.mmorpg.mir.model.gameobjects.Player;

public class SM_Attach_God {

	private int code;
	
	private Map<Integer, Object> equipUpdate;
	private Map<Integer, Object> packUpdate;
	
	private boolean randSucc;
	
	public static SM_Attach_God valueOf(Player player, boolean randSucc) {
		SM_Attach_God sm = new SM_Attach_God();
		sm.equipUpdate = player.getEquipmentStorage().collectUpdate();
		sm.packUpdate = player.getPack().collectUpdate();
		sm.randSucc = randSucc;
		return sm;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Map<Integer, Object> getEquipUpdate() {
		return equipUpdate;
	}

	public void setEquipUpdate(Map<Integer, Object> equipUpdate) {
		this.equipUpdate = equipUpdate;
	}

	public boolean isRandSucc() {
		return randSucc;
	}

	public void setRandSucc(boolean randSucc) {
		this.randSucc = randSucc;
	}

	public Map<Integer, Object> getPackUpdate() {
		return packUpdate;
	}

	public void setPackUpdate(Map<Integer, Object> packUpdate) {
		this.packUpdate = packUpdate;
	}

}
