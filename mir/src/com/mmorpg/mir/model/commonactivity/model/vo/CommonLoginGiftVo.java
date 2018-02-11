package com.mmorpg.mir.model.commonactivity.model.vo;

import java.util.HashSet;

public class CommonLoginGiftVo {
	private HashSet<String> rewarded;

	public static CommonLoginGiftVo valueOf(HashSet<String> rewarded) {
		CommonLoginGiftVo vo = new CommonLoginGiftVo();
		vo.rewarded = rewarded;
		return vo;
	}

	public HashSet<String> getRewarded() {
		return rewarded;
	}

	public void setRewarded(HashSet<String> rewarded) {
		this.rewarded = rewarded;
	}
}
