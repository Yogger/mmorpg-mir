package com.mmorpg.mir.model.warship.packet;

public class SM_Warship_Add_Exp {

	private int addExp;

	public static SM_Warship_Add_Exp valueOf(int add) {
		SM_Warship_Add_Exp sm = new SM_Warship_Add_Exp();
		sm.addExp = add;
		return sm;
	}
	
	public int getAddExp() {
		return addExp;
	}

	public void setAddExp(int addExp) {
		this.addExp = addExp;
	}
	
}
