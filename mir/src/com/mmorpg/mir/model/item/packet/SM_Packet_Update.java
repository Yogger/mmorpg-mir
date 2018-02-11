package com.mmorpg.mir.model.item.packet;

import java.util.Map;

import com.mmorpg.mir.model.gameobjects.Player;

public class SM_Packet_Update {

	private int code;
	private Map<Integer, Object> packUpdate;

	public Map<Integer, Object> getPackUpdate() {
		return packUpdate;
	}

	public void setPackUpdate(Map<Integer, Object> packUpdate) {
		this.packUpdate = packUpdate;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}


	public static SM_Packet_Update valueOf(Player player) {
		SM_Packet_Update result = new SM_Packet_Update();
		result.packUpdate = player.getPack().collectUpdate();
		return result;
	}
	
	public static SM_Packet_Update valueOf(Player player, int c) {
		SM_Packet_Update result = new SM_Packet_Update();
		result.packUpdate = player.getPack().collectUpdate();
		result.code = c;
		return result;
	}
}
