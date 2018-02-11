package com.mmorpg.mir.model.country.packet;

public class SM_Country_CallbackTank {

	private int tankId;

	public static SM_Country_CallbackTank valueOf(int tankId) {
		SM_Country_CallbackTank sm = new SM_Country_CallbackTank();
		sm.tankId = tankId;
		return sm;
	}

	public int getTankId() {
		return tankId;
	}

	public void setTankId(int tankId) {
		this.tankId = tankId;
	}

}
