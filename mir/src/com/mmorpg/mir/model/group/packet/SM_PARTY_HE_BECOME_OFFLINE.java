package com.mmorpg.mir.model.group.packet;

import com.mmorpg.mir.model.gameobjects.Player;

public class SM_PARTY_HE_BECOME_OFFLINE {

	public static SM_PARTY_HE_BECOME_OFFLINE valueOf(Player player) {
		SM_PARTY_HE_BECOME_OFFLINE sm = new SM_PARTY_HE_BECOME_OFFLINE();
		sm.playerId = player.getObjectId();
		return sm;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	private long playerId;

}
