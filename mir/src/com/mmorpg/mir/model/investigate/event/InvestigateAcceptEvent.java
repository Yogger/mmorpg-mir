package com.mmorpg.mir.model.investigate.event;

import com.mmorpg.mir.model.gameobjects.Player;
import com.windforce.common.event.event.IEvent;

public class InvestigateAcceptEvent implements IEvent{

	private long playerId;
	
	public static InvestigateAcceptEvent valueOf(Player player) {
		InvestigateAcceptEvent i = new InvestigateAcceptEvent();
		i.playerId = player.getObjectId();
		return i;
	}
	
	@Override
	public long getOwner() {
		return playerId;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

}
