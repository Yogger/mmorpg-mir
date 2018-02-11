package com.mmorpg.mir.model.country.model;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.manager.PlayerManager;

public class TraitorPlayer {

	private long playerId;
	private String name;
	private long battlePower;
	private long endTime;
	
	public static final TraitorPlayer valueOf(Player player, long endTime) {
		TraitorPlayer p = new TraitorPlayer();
		p.playerId = player.getObjectId();
		if (player.getGameStats().statSize() == 0) {
			PlayerManager.getInstance().resetPlayerGameStats(player);
			player.getGameStats().addModifiers(CountryFlag.COUNTRY_FLAG, player.getCountry().getCountryFlag().getResource().getPlayerStats());
		}
		p.battlePower = player.getGameStats().calcBattleScore();
		p.name = player.getName();
		p.endTime = endTime;
		return p;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public long getBattlePower() {
		return battlePower;
	}

	public void setBattlePower(long battlePower) {
		this.battlePower = battlePower;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
