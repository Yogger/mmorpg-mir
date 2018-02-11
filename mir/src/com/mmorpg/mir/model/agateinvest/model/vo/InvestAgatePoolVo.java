package com.mmorpg.mir.model.agateinvest.model.vo;

import java.util.HashMap;

import com.mmorpg.mir.model.agateinvest.model.InvestAgate;
import com.mmorpg.mir.model.agateinvest.model.InvestAgatePool;

public class InvestAgatePoolVo {
	/** 各类投资信息 */
	private HashMap<Integer, InvestAgate> invests;

	public static InvestAgatePoolVo valueOf(InvestAgatePool pool) {
		InvestAgatePoolVo result = new InvestAgatePoolVo();
		result.invests = pool.getInvests();
		return result;
	}

	public HashMap<Integer, InvestAgate> getInvests() {
		return invests;
	}

	public void setInvests(HashMap<Integer, InvestAgate> invests) {
		this.invests = invests;
	}

}
