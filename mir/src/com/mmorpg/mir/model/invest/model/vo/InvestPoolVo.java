package com.mmorpg.mir.model.invest.model.vo;

import java.util.HashMap;

import com.mmorpg.mir.model.invest.model.Invest;
import com.mmorpg.mir.model.invest.model.InvestPool;

public class InvestPoolVo {
	/** 各类投资信息 */
	private HashMap<Integer, Invest> invests;

	public static InvestPoolVo valueOf(InvestPool pool) {
		InvestPoolVo result = new InvestPoolVo();
		result.invests = pool.getInvests();
		return result;
	}

	public HashMap<Integer, Invest> getInvests() {
		return invests;
	}

	public void setInvests(HashMap<Integer, Invest> invests) {
		this.invests = invests;
	}

}
