package com.mmorpg.mir.model.commonactivity.packet;

public class SM_Lucky_Draw_Recharge {
	private long payCount;

	private int drawCount;

	public static SM_Lucky_Draw_Recharge valueOf(long payCount, int drawCount) {
		SM_Lucky_Draw_Recharge sm = new SM_Lucky_Draw_Recharge();
		sm.payCount = payCount;
		sm.drawCount = drawCount;
		return sm;
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
