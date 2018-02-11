package com.mmorpg.mir.model.welfare.packet;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.welfare.util.Util;

public class SM_Welfare_Sign_Ing {

	private int dayIndex;// 当前签到成功的事第几天
	private int totalSignedNum; // 累计签到的次数
	private int totalSignCount;

	public static SM_Welfare_Sign_Ing valueOf(Player player) {
		SM_Welfare_Sign_Ing sm = new SM_Welfare_Sign_Ing();
		sm.setDayIndex(Util.getInstance().getDay(player.getWelfare().getSign().getLastTime()));
		sm.totalSignedNum = player.getWelfare().getSign().getTotalSignNum();
		sm.totalSignCount = player.getWelfare().getSign().getTotalSignCount();
		return sm;
	}

	public int getDayIndex() {
		return dayIndex;
	}

	public void setDayIndex(int dayIndex) {
		this.dayIndex = dayIndex;
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
