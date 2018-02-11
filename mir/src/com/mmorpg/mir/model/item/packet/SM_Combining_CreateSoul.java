package com.mmorpg.mir.model.item.packet;

import java.util.Map;

import com.mmorpg.mir.model.gameobjects.Player;

public class SM_Combining_CreateSoul {

	private int code;

	private Map<Integer, Object> packUpdate;

	public static SM_Combining_CreateSoul valueOf(Player player) {
		SM_Combining_CreateSoul sm = new SM_Combining_CreateSoul();
		sm.packUpdate = player.getPack().collectUpdate();
		return sm;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Map<Integer, Object> getPackUpdate() {
		return packUpdate;
	}

	public void setPackUpdate(Map<Integer, Object> packUpdate) {
		this.packUpdate = packUpdate;
	}

}
