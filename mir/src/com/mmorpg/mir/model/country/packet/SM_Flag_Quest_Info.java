package com.mmorpg.mir.model.country.packet;

import java.util.Map;

public class SM_Flag_Quest_Info {

	private Map<Integer, Integer> map;

	private long currentHp;

	private long maxHp;

	public static SM_Flag_Quest_Info valueOf(Map<Integer, Integer> m, long currentHp, long maxHp) {
		SM_Flag_Quest_Info sm = new SM_Flag_Quest_Info();
		sm.map = m;
		sm.currentHp = currentHp;
		sm.maxHp = maxHp;
		return sm;
	}

	public Map<Integer, Integer> getMap() {
		return map;
	}

	public void setMap(Map<Integer, Integer> map) {
		this.map = map;
	}

	public long getCurrentHp() {
		return currentHp;
	}

	public void setCurrentHp(int currentHp) {
		this.currentHp = currentHp;
	}

	public long getMaxHp() {
		return maxHp;
	}

	public void setMaxHp(int maxHp) {
		this.maxHp = maxHp;
	}

}
