package com.mmorpg.mir.model.kingofwar.packet.vo;

import com.mmorpg.mir.model.kingofwar.model.PlayerWarInfo;

public class PlayerRankInfoVO {
	private String name;
	private String server;
	private int rank;
	private int points;
	private int country;
	private String gangName;
	private int killCount;
	private int role;

	public static PlayerRankInfoVO valueOf(PlayerWarInfo playerWarInfo) {
		PlayerRankInfoVO info = new PlayerRankInfoVO();
		info.name = playerWarInfo.getPlayer().getName();
		info.server = playerWarInfo.getPlayer().getPlayerEnt().getServer();
		info.rank = playerWarInfo.getRank();
		info.points = playerWarInfo.getPoints();
		info.country = playerWarInfo.getPlayer().getCountryValue();
		info.gangName = playerWarInfo.getPlayer().getGang() != null ? playerWarInfo.getPlayer().getGang().getName()
				: null;
		info.role = playerWarInfo.getPlayer().getRole();
		info.killCount = playerWarInfo.getAllKillCount();
		return info;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public int getCountry() {
		return country;
	}

	public void setCountry(int country) {
		this.country = country;
	}

}
