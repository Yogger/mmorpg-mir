package com.mmorpg.mir.model.monsterriot.packet;

import java.util.Map;

public class SM_Riot_AddCount {
	private int rank;
	private int count;
	private Map<String, Integer> monsterStatus;
	private int round;
	
	public static SM_Riot_AddCount valueOf(int rank, int count, int round, Map<String, Integer> ret) {
		SM_Riot_AddCount sm = new SM_Riot_AddCount();
		sm.rank = rank;
		sm.count = count;
		sm.monsterStatus = ret;
		sm.round = round;
		return sm;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Map<String, Integer> getMonsterStatus() {
		return monsterStatus;
	}

	public void setMonsterStatus(Map<String, Integer> monsterStatus) {
		this.monsterStatus = monsterStatus;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

}
