package com.mmorpg.mir.model.openactive.packet;

import com.mmorpg.mir.model.openactive.model.CountryFlagActive;

public class SM_CountryFlag_Status {

	private int count;

	public static SM_CountryFlag_Status valueOf(CountryFlagActive active) {
		SM_CountryFlag_Status result = new SM_CountryFlag_Status();
		result.count = active.getCount();
		return result;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
