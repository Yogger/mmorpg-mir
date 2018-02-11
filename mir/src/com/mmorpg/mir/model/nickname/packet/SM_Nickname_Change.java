package com.mmorpg.mir.model.nickname.packet;

import java.util.ArrayList;

import com.mmorpg.mir.model.gameobjects.Player;

public class SM_Nickname_Change {
	private long playerId;
	private ArrayList<Integer> nickNames;

	public static SM_Nickname_Change valueOf(Player player) {
		SM_Nickname_Change sm = new SM_Nickname_Change();
		sm.playerId = player.getObjectId();
		sm.nickNames = new ArrayList<Integer>(player.getNicknamePool().getEquipIds());
		return sm;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public ArrayList<Integer> getNickNames() {
		return nickNames;
	}

	public void setNickNames(ArrayList<Integer> nickNames) {
		this.nickNames = nickNames;
	}

}
