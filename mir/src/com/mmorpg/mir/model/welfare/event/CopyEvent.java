package com.mmorpg.mir.model.welfare.event;

import com.mmorpg.mir.model.copy.model.CopyType;
import com.mmorpg.mir.model.gameobjects.Player;
import com.windforce.common.event.event.IEvent;

/**
 * 进入副本 - 续展边疆
 * 
 * @author 37wan
 * 
 */
public class CopyEvent implements IEvent {

	private long playerId;
	private String copyId;
	private CopyType copyType;

	public static IEvent valueOf(Player player, String copyId, CopyType copyType) {
		CopyEvent event = new CopyEvent();
		event.setPlayerId(player.getObjectId());
		event.setCopyId(copyId);
		event.setCopyType(copyType);
		return event;
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

	public String getCopyId() {
		return copyId;
	}

	public void setCopyId(String copyId) {
		this.copyId = copyId;
	}

	public CopyType getCopyType() {
		return copyType;
	}

	public void setCopyType(CopyType copyType) {
		this.copyType = copyType;
	}

}
