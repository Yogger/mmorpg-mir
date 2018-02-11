package com.mmorpg.mir.model.invest.packet;

import com.mmorpg.mir.model.system.packet.CM_System_Sign;

public class CM_Invest_Buy extends CM_System_Sign {
	/** 购买的投资类型 */
	private int investType;

	public int getInvestType() {
		return investType;
	}

	public void setInvestType(int investType) {
		this.investType = investType;
	}

}
