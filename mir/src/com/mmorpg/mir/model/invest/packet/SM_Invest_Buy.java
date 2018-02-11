package com.mmorpg.mir.model.invest.packet;

import com.mmorpg.mir.model.invest.model.Invest;

public class SM_Invest_Buy {

	private int type;

	private Invest invest;

	public static SM_Invest_Buy valueOf(int type, Invest invest) {
		SM_Invest_Buy result = new SM_Invest_Buy();
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

	public Invest getInvest() {
		return invest;
	}

	public void setInvest(Invest invest) {
		this.invest = invest;
	}

}
