package com.mmorpg.mir.model.agateinvest.packet;

import com.mmorpg.mir.model.agateinvest.model.InvestAgate;

public class SM_Agate_Invest_Buy {

	private int type;

	private InvestAgate invest;

	public static SM_Agate_Invest_Buy valueOf(int type, InvestAgate invest) {
		SM_Agate_Invest_Buy result = new SM_Agate_Invest_Buy();
		result.type = type;
		result.invest = invest;
		return result;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public InvestAgate getInvest() {
		return invest;
	}

	public void setInvest(InvestAgate invest) {
		this.invest = invest;
	}

}
