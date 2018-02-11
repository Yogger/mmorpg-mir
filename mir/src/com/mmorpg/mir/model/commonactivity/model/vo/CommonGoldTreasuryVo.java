package com.mmorpg.mir.model.commonactivity.model.vo;

import java.util.Map;

import com.mmorpg.mir.model.commonactivity.model.CommonGoldTreasury;
import com.mmorpg.mir.model.commonactivity.model.GoldTreasuryLog;

public class CommonGoldTreasuryVo {
	private Map<Integer, GoldTreasuryLog> treasurys;
	
	public static CommonGoldTreasuryVo valueOf(CommonGoldTreasury treasury) {
		CommonGoldTreasuryVo vo = new CommonGoldTreasuryVo();
		vo.treasurys = treasury.getTreasurys();
		return vo;
	}

	public Map<Integer, GoldTreasuryLog> getTreasurys() {
		return treasurys;
	}

	public void setTreasurys(Map<Integer, GoldTreasuryLog> treasurys) {
		this.treasurys = treasurys;
	}
}
