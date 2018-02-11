package com.mmorpg.mir.model.rank.packet;

public class SM_Weak_Country {
	private boolean isInWeakCountry;
	
	public static SM_Weak_Country valueOf(boolean weak) {
		SM_Weak_Country sm = new SM_Weak_Country();
		sm.isInWeakCountry = weak;
		return sm;
	}

	public boolean isInWeakCountry() {
		return isInWeakCountry;
	}

	public void setInWeakCountry(boolean isInWeakCountry) {
		this.isInWeakCountry = isInWeakCountry;
	}

}
