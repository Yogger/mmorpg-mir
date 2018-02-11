package com.mmorpg.mir.model.country.packet;

public class SM_Country_Fete {
	private int type;

	public static SM_Country_Fete valueOf(int t) {
		SM_Country_Fete sm = new SM_Country_Fete();
		sm.type = t;
		return sm;
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
}
