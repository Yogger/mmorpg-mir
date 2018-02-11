package com.mmorpg.mir.model.warship.packet;

public class SM_Warhip_End {

	private long totalExp;
	private long totalQi;

	public static SM_Warhip_End valueOf(long exp, long qi) {
		SM_Warhip_End sm = new SM_Warhip_End();
		sm.totalExp = exp;
		sm.totalQi = qi;
		return sm;
	}

	public long getTotalExp() {
		return totalExp;
	}

	public void setTotalExp(long totalExp) {
		this.totalExp = totalExp;
	}

	public long getTotalQi() {
		return totalQi;
	}

	public void setTotalQi(long totalQi) {
		this.totalQi = totalQi;
	}

}
