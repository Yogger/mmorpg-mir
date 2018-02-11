package com.mmorpg.mir.model.gang.model;

public class GangSimpleVO {
	private long id;
	private String name;
	private int size;
	private String masterName;
	private long battlePoints;
	private byte autoDeal;
	private int rank;
	private long masterId;
	private int masterLevel;
	private byte online;
	private long createTime;
	private int country;

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

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getMasterName() {
		return masterName;
	}

	public void setMasterName(String masterName) {
		this.masterName = masterName;
	}

	public long getBattlePoints() {
		return battlePoints;
	}

	public void setBattlePoints(long battlePoints) {
		this.battlePoints = battlePoints;
	}

	public byte getAutoDeal() {
		return autoDeal;
	}

	public void setAutoDeal(byte autoDeal) {
		this.autoDeal = autoDeal;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public long getMasterId() {
		return masterId;
	}

	public void setMasterId(long masterId) {
		this.masterId = masterId;
	}

	public int getMasterLevel() {
		return masterLevel;
	}

	public void setMasterLevel(int masterLevel) {
		this.masterLevel = masterLevel;
	}

	public byte getOnline() {
		return online;
	}

	public void setOnline(byte online) {
		this.online = online;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public int getCountry() {
		return country;
	}

	public void setCountry(int country) {
		this.country = country;
	}

}
