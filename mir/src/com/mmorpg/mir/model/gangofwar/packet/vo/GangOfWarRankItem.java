package com.mmorpg.mir.model.gangofwar.packet.vo;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gangofwar.model.PlayerGangWarInfo;

public class GangOfWarRankItem {
	private int rank;
	private String server;
	private String name;
	private String gangName;
	private String gangServer;
	private int role;
	private int killCount;
	private int battleScore;

	public static GangOfWarRankItem valueOf(int rank, Player player, PlayerGangWarInfo playerGangWarInfo) {
		GangOfWarRankItem item = new GangOfWarRankItem();
		item.rank = rank;
		item.server = player.getPlayerEnt().getServer();
		item.name = player.getName();
		item.gangName = playerGangWarInfo.getGangName();
		item.gangServer = playerGangWarInfo.getGangServer();
		item.killCount = playerGangWarInfo.getTotalKill();
		item.role = player.getRole();
		item.battleScore = player.getGameStats().calcBattleScore();
		return item;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGangName() {
		return gangName;
	}

	public void setGangName(String gangName) {
		this.gangName = gangName;
	}

	public int getKillCount() {
		return killCount;
	}

	public void setKillCount(int killCount) {
		this.killCount = killCount;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public int getBattleScore() {
		return battleScore;
	}

	public void setBattleScore(int battleScore) {
		this.battleScore = battleScore;
	}

	public String getGangServer() {
		return gangServer;
	}

	public void setGangServer(String gangServer) {
		this.gangServer = gangServer;
	}

}
