package com.mmorpg.mir.model.temple.packet;

import com.mmorpg.mir.model.temple.model.TempleHistory;

public class SM_Temple_AccpetQuest {
	private int country;

	private int count;

	public static SM_Temple_AccpetQuest valueOf(TempleHistory templeHistory) {
		SM_Temple_AccpetQuest sm = new SM_Temple_AccpetQuest();
		sm.country = templeHistory.getQuestCountry();
		sm.count = templeHistory.getCount();
		return sm;
	}

	public int getCountry() {
		return country;
	}

	public void setCountry(int country) {
		this.country = country;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
