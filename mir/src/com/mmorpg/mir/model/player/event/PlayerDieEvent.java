package com.mmorpg.mir.model.player.event;

import com.windforce.common.event.event.IEvent;

public class PlayerDieEvent implements IEvent {

	private long playerId;
	
	private long attackerId;
	
	private boolean killedByOtherCountryPlayer;
	
	private int extra;
	
	public boolean isKilledByPlayer() {
		return killedByOtherCountryPlayer;
	}

	public void setKilledByPlayer(boolean killedByPlayer) {
		this.killedByOtherCountryPlayer = killedByPlayer;
	}

	public long getAttackerId() {
		return attackerId;
	}

	public void setAttackerId(long attackerId) {
		this.attackerId = attackerId;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	@Override
	public long getOwner() {
		return playerId;
	}
	
	public static PlayerDieEvent valueOf(long pid, long attackerId, boolean killedByPlayer) {
		PlayerDieEvent event = new PlayerDieEvent();
		event.playerId = pid;
		event.attackerId = attackerId;
		event.killedByOtherCountryPlayer = killedByPlayer;
		return event;
	}

	public int getExtra() {
    	return extra;
    }

	public void setExtra(int extra) {
    	this.extra = extra;
    }

}
