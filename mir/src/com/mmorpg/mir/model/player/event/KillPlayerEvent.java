package com.mmorpg.mir.model.player.event;

import com.windforce.common.event.event.IEvent;

public class KillPlayerEvent implements IEvent{

	private long playerId;
	
	private long killedPlayerId;
	
	private boolean killOtherCountryPlayer;
	
	public static KillPlayerEvent valueOf(long pid, long killedId, boolean isKillByOtherCountry) {
		KillPlayerEvent e = new KillPlayerEvent();
		e.playerId = pid;
		e.killedPlayerId = killedId;
		e.killOtherCountryPlayer = isKillByOtherCountry;
		return e;
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

	public long getKilledPlayerId() {
		return killedPlayerId;
	}

	public void setKilledPlayerId(long killedPlayerId) {
		this.killedPlayerId = killedPlayerId;
	}

	public boolean isKillOtherCountryPlayer() {
		return killOtherCountryPlayer;
	}

	public void setKillOtherCountryPlayer(boolean killOtherCountryPlayer) {
		this.killOtherCountryPlayer = killOtherCountryPlayer;
	}

}
