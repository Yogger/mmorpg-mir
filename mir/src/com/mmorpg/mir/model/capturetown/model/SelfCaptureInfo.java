package com.mmorpg.mir.model.capturetown.model;

import com.mmorpg.mir.model.gameobjects.Player;


public class SelfCaptureInfo {

	private long time;
	
	private long feats;
	
	private int type;
	
	private String enemyName;
	
	private int enemyCountry;
	
	private String townKeyFrom;
	
	private String townKeyTo;
	
	public static SelfCaptureInfo valueOf(int type, long feats, Player enemyPlayer, String from, String to) {
		SelfCaptureInfo info = new SelfCaptureInfo();
		info.time = System.currentTimeMillis();
		info.feats = feats;
		info.enemyCountry = enemyPlayer.getCountryValue();
		info.enemyName = enemyPlayer.getName();
		info.townKeyFrom = from;
		info.townKeyTo = to;
		info.type = type;
		return info;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public long getFeats() {
		return feats;
	}

	public void setFeats(long feats) {
		this.feats = feats;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getEnemyName() {
		return enemyName;
	}

	public void setEnemyName(String enemyName) {
		this.enemyName = enemyName;
	}

	public int getEnemyCountry() {
		return enemyCountry;
	}

	public void setEnemyCountry(int enemyCountry) {
		this.enemyCountry = enemyCountry;
	}

	public String getTownKeyFrom() {
		return townKeyFrom;
	}

	public void setTownKeyFrom(String townKeyFrom) {
		this.townKeyFrom = townKeyFrom;
	}

	public String getTownKeyTo() {
		return townKeyTo;
	}

	public void setTownKeyTo(String townKeyTo) {
		this.townKeyTo = townKeyTo;
	}

}
