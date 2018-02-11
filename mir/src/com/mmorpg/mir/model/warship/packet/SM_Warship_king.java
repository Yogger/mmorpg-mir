package com.mmorpg.mir.model.warship.packet;

public class SM_Warship_king {

	private int leftCount;
	private int supoortCount;
	private int contemptCount;
	
	public static SM_Warship_king valueOf(int leftCount, int supportCount, int contemptCount) {
		SM_Warship_king sm = new SM_Warship_king();
		sm.leftCount = leftCount;
		sm.supoortCount = supportCount;
		sm.contemptCount = contemptCount;
		return sm;
	}

	public int getLeftCount() {
		return leftCount;
	}

	public void setLeftCount(int leftCount) {
		this.leftCount = leftCount;
	}

	public int getSupoortCount() {
		return supoortCount;
	}

	public void setSupoortCount(int supoortCount) {
		this.supoortCount = supoortCount;
	}

	public int getContemptCount() {
		return contemptCount;
	}

	public void setContemptCount(int contemptCount) {
		this.contemptCount = contemptCount;
	}
	
}
