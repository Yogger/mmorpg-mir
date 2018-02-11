package com.mmorpg.mir.model.country.packet;

public class SM_Country_Traitor_Num {

	private int count;
	
	public static SM_Country_Traitor_Num valueOf(int c) {
		SM_Country_Traitor_Num sm = new SM_Country_Traitor_Num();
		sm.count = c;
		return sm;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
}
