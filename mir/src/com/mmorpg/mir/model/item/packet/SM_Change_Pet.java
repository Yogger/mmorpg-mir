package com.mmorpg.mir.model.item.packet;

import com.mmorpg.mir.model.gameobjects.Player;

public class SM_Change_Pet {

	private long playerId;
	private short petTemplateId;
	
	public static SM_Change_Pet valueOf(Player player, short tId) {
		SM_Change_Pet sm = new SM_Change_Pet();
		sm.playerId = player.getObjectId();
		sm.petTemplateId = tId;
		return sm;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public short getPetTemplateId() {
		return petTemplateId;
	}

	public void setPetTemplateId(short petTemplateId) {
		this.petTemplateId = petTemplateId;
	}

}
