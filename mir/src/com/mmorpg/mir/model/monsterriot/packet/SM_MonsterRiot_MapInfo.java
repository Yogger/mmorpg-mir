package com.mmorpg.mir.model.monsterriot.packet;

import java.util.Map;

public class SM_MonsterRiot_MapInfo {
	private int rank;
	private long damage;
	private Map<String, Integer> monsterStatus;
	private byte push;
	private int round;
	
	public static SM_MonsterRiot_MapInfo valueOf(int rank, long damage, int round, Map<String, Integer> ret) {
		SM_MonsterRiot_MapInfo sm = new SM_MonsterRiot_MapInfo();
		sm.rank = rank;
		sm.damage = damage;
		sm.round = round;
		sm.monsterStatus = ret;
		return sm;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public long getDamage() {
		return damage;
	}

	public void setDamage(long damage) {
		this.damage = damage;
	}

	public Map<String, Integer> getMonsterStatus() {
		return monsterStatus;
	}

	public void setMonsterStatus(Map<String, Integer> monsterStatus) {
		this.monsterStatus = monsterStatus;
	}

	public byte getPush() {
		return push;
	}

	public void setPush(byte push) {
		this.push = push;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

}
