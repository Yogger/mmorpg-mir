package com.mmorpg.mir.model.beauty.packet;

public class SM_Beauty_Fight {
	private String fightGirlId;

	private long lastFightTime;

	public static SM_Beauty_Fight valueOf(String fightGirlId, long lastFightTime) {
		SM_Beauty_Fight result = new SM_Beauty_Fight();
		result.fightGirlId = fightGirlId;
		result.lastFightTime = lastFightTime;
		return result;
	}

	public String getFightGirlId() {
		return fightGirlId;
	}

	public void setFightGirlId(String fightGirlId) {
		this.fightGirlId = fightGirlId;
	}

	public long getLastFightTime() {
		return lastFightTime;
	}

	public void setLastFightTime(long lastFightTime) {
		this.lastFightTime = lastFightTime;
	}

}
