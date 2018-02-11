package com.mmorpg.mir.model.group.packet;

import com.mmorpg.mir.model.gameobjects.Player;

public class SM_PARTY_HE_BECOME_ONLINE {

	public static SM_PARTY_HE_BECOME_ONLINE valueOf(Player player) {
		SM_PARTY_HE_BECOME_ONLINE sm = new SM_PARTY_HE_BECOME_ONLINE();
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
