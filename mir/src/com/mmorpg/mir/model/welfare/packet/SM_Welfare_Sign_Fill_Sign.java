package com.mmorpg.mir.model.welfare.packet;

import com.mmorpg.mir.model.welfare.util.Util;

/** 补签 */
public class SM_Welfare_Sign_Fill_Sign {

	private int fillSignDayIndex; // 补签的日子
	private int totalSignedNum; // 累计签到次数
	private int totalSignCount;

	public static SM_Welfare_Sign_Fill_Sign valueOf(long fillSignTimeMillis, int totalSigned, int totalSignCount) {
		SM_Welfare_Sign_Fill_Sign sm = new SM_Welfare_Sign_Fill_Sign();
		sm.setFillSignDayIndex(Util.getInstance().getDay(fillSignTimeMillis));
		sm.totalSignedNum = totalSigned;
		sm.totalSignCount = totalSignCount;
		return sm;
	}

	public int getFillSignDayIndex() {
		return fillSignDayIndex;
	}

	public void setFillSignDayIndex(int fillSignDayIndex) {
		this.fillSignDayIndex = fillSignDayIndex;
	}

	public int getTotalSignedNum() {
		return totalSignedNum;
	}

	public void setTotalSignedNum(int totalSignedNum) {
		this.totalSignedNum = totalSignedNum;
	}

	public int getTotalSignCount() {
		return totalSignCount;
	}

	public void setTotalSignCount(int totalSignCount) {
		this.totalSignCount = totalSignCount;
	}

}
