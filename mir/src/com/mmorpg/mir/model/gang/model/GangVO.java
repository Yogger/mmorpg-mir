package com.mmorpg.mir.model.gang.model;

import java.util.ArrayList;

import com.mmorpg.mir.model.gang.model.log.GangLog;

public class GangVO {
	private long id;
	private String name;
	private String info;
	private ArrayList<MemberVO> vos;
	private long battle;
	private String country;
	private ArrayList<GangLog> logs;
	private String server;
	private byte autoDeal;
	private long lastModifyInfoTime;
	private int rank;
	private long joinChatTimeMills;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<MemberVO> getVos() {
		return vos;
	}

	public void setVos(ArrayList<MemberVO> vos) {
		this.vos = vos;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public long getBattle() {
		return battle;
	}

	public void setBattle(long battle) {
		this.battle = battle;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public ArrayList<GangLog> getLogs() {
		return logs;
	}

	public void setLogs(ArrayList<GangLog> logs) {
		this.logs = logs;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public byte getAutoDeal() {
		return autoDeal;
	}

	public void setAutoDeal(byte autoDeal) {
		this.autoDeal = autoDeal;
	}

	public long getLastModifyInfoTime() {
		return lastModifyInfoTime;
	}

	public void setLastModifyInfoTime(long lastModifyInfoTime) {
		this.lastModifyInfoTime = lastModifyInfoTime;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public long getJoinChatTimeMills() {
		return joinChatTimeMills;
	}

	public void setJoinChatTimeMills(long joinChatTimeMills) {
		this.joinChatTimeMills = joinChatTimeMills;
	}
	
}
