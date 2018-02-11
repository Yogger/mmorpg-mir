package com.mmorpg.mir.model.capturetown.model;

import com.mmorpg.mir.model.gameobjects.Player;

public class CountryCaptureInfo {
	
	private long time;
	
	private String attackerName;
	
	private int attackerCountry;
	
	private String defenderName;
	
	private int defenderCountry;
	
	private String key;
	
	public static CountryCaptureInfo valueOf(Player attacker, Player enemy, String key) {
		CountryCaptureInfo cc = new CountryCaptureInfo();
		cc.time = System.currentTimeMillis();
		cc.attackerCountry = attacker.getCountryValue();
		cc.attackerName = attacker.getName();
		cc.defenderCountry = enemy.getCountryValue();
		cc.defenderName = enemy.getName();
		cc.key = key;
		return cc;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getAttackerName() {
		return attackerName;
	}

	public void setAttackerName(String attackerName) {
		this.attackerName = attackerName;
	}

	public int getAttackerCountry() {
		return attackerCountry;
	}

	public void setAttackerCountry(int attackerCountry) {
		this.attackerCountry = attackerCountry;
	}

	public String getDefenderName() {
		return defenderName;
	}

	public void setDefenderName(String defenderName) {
		this.defenderName = defenderName;
	}

	public int getDefenderCountry() {
		return defenderCountry;
	}

	public void setDefenderCountry(int defenderCountry) {
		this.defenderCountry = defenderCountry;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
