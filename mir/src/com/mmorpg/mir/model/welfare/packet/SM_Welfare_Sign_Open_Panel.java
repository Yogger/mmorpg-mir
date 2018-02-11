package com.mmorpg.mir.model.welfare.packet;

import java.util.ArrayList;

import org.h2.util.New;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.welfare.util.Util;

public class SM_Welfare_Sign_Open_Panel {

	private ArrayList<Integer> signedDayList; // 已经签到的天
	private ArrayList<Integer> rewardedList; // 领奖
	private int totalSignedNum; // 累计签到多少次
	private int totalSignCount;// 累计签到次数

	public static SM_Welfare_Sign_Open_Panel valueOf(Player player) {
		SM_Welfare_Sign_Open_Panel sm = new SM_Welfare_Sign_Open_Panel();

		// 已经签到的天
		ArrayList<Integer> signedDayList = New.arrayList();
		for (Long signTime : player.getWelfare().getSign().getSignTimeList()) {
			if (Util.getInstance().isSameMonth(System.currentTimeMillis(), signTime)) {
				int day = Util.getInstance().getDay(signTime);
				signedDayList.add(new Integer(day));
			}
		}
		sm.setSignedDayList(signedDayList);

		// 普通领奖
		ArrayList<Integer> rewardedList = New.arrayList();
		for (int days : player.getWelfare().getSign().getRewardedList()) {
			rewardedList.add(new Integer(days));
		}
		sm.setRewardedList(rewardedList);
		sm.setTotalSignedNum(player.getWelfare().getSign().getTotalSignNum());
		sm.totalSignCount = player.getWelfare().getSign().getTotalSignCount();
		return sm;
	}

	public ArrayList<Integer> getSignedDayList() {
		return signedDayList;
	}

	public void setSignedDayList(ArrayList<Integer> signedDayList) {
		this.signedDayList = signedDayList;
	}

	public ArrayList<Integer> getRewardedList() {
		return rewardedList;
	}

	public void setRewardedList(ArrayList<Integer> rewardedList) {
		this.rewardedList = rewardedList;
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
