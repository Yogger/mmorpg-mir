package com.mmorpg.mir.model.commonactivity.model.vo;

import com.mmorpg.mir.model.commonactivity.model.LuckyDraw;

public class LuckyDrawVo {
	private long payCount;

	private int drawCount;

	public static LuckyDrawVo valueOf(LuckyDraw draw) {
		LuckyDrawVo vo = new LuckyDrawVo();
		vo.payCount = draw.getPayCount();
		vo.drawCount = draw.getDrawCount();
		return vo;
	}

	public long getPayCount() {
		return payCount;
	}

	public void setPayCount(long payCount) {
		this.payCount = payCount;
	}

	public int getDrawCount() {
		return drawCount;
	}

	public void setDrawCount(int drawCount) {
		this.drawCount = drawCount;
	}
}
