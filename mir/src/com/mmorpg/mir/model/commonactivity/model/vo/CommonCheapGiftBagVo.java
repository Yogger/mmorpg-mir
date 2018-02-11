package com.mmorpg.mir.model.commonactivity.model.vo;

import java.util.HashMap;

public class CommonCheapGiftBagVo {
	private HashMap<Integer, String> rewarded;

	public static CommonCheapGiftBagVo valueOf(HashMap<Integer, String> rewarded) {
		CommonCheapGiftBagVo vo = new CommonCheapGiftBagVo();
		vo.rewarded = rewarded;
		return vo;
	}

	public HashMap<Integer, String> getRewarded() {
		return rewarded;
	}

	public void setRewarded(HashMap<Integer, String> rewarded) {
		this.rewarded = rewarded;
	}

}
