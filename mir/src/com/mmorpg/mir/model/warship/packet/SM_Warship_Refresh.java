package com.mmorpg.mir.model.warship.packet;

public class SM_Warship_Refresh {

	private int currentSelect;

	public static SM_Warship_Refresh valueOf(int currentSelect) {
		SM_Warship_Refresh sm = new SM_Warship_Refresh();
		sm.currentSelect = currentSelect;
		return sm;
	}
	
	public int getCurrentSelect() {
		return currentSelect;
	}

	public void setCurrentSelect(int currentSelect) {
		this.currentSelect = currentSelect;
	}

}
