package com.mmorpg.mir.model.gang.packet;

public class SM_Set_AutoDeal {
	private boolean autoDeal;

	public static SM_Set_AutoDeal valueOf(boolean autoDeal) {
		SM_Set_AutoDeal sm = new SM_Set_AutoDeal();
		sm.autoDeal = autoDeal;
		return sm;
	}

	public boolean isAutoDeal() {
		return autoDeal;
	}

	public void setAutoDeal(boolean autoDeal) {
		this.autoDeal = autoDeal;
	}

}
