package com.mmorpg.mir.model.welfare.packet;

import java.util.ArrayList;
import java.util.Map;

import org.h2.util.New;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.welfare.model.ActiveEnum;

public class SM_Welfare_Active_Open_Panel {

	private Map<Integer, Integer> activeValueMap;// 执行进度
	private Map<Integer, Integer> activeValueStatusMap;// 执行状态
	private ArrayList<Integer> activeValueRewardList;// 已经领奖的状态
	private int activeValue;//玩家当前的活跃值

	public static SM_Welfare_Active_Open_Panel valueOf(Player player) {
		SM_Welfare_Active_Open_Panel sm = new SM_Welfare_Active_Open_Panel();
		Map<Integer, Integer> activeValueMap = New.hashMap();
		Map<Integer, Integer> activeValueStatusMap = New.hashMap();
		ArrayList<Integer> activeValueRewardList = New
				.arrayList(player.getWelfare().getActiveValue().getRewardedList());
		for (ActiveEnum e : ActiveEnum.values()) {
			activeValueMap.put(e.getEventId(), player.getWelfare().getActiveValue().getExeNum(e));
			activeValueStatusMap.put(e.getEventId(), player.getWelfare().getActiveValue().getExeStatus(e));
		}
		sm.setActiveValueMap(activeValueMap);
		sm.setActiveValueStatusMap(activeValueStatusMap);
		sm.setActiveValueRewardList(activeValueRewardList);
		sm.setActiveValue(player.getWelfare().getActiveValue().getActiveValue());
		return sm;
	}

	public Map<Integer, Integer> getActiveValueMap() {
		return activeValueMap;
	}

	public void setActiveValueMap(Map<Integer, Integer> activeValueMap) {
		this.activeValueMap = activeValueMap;
	}

	public Map<Integer, Integer> getActiveValueStatusMap() {
		return activeValueStatusMap;
	}

	public void setActiveValueStatusMap(Map<Integer, Integer> activeValueStatusMap) {
		this.activeValueStatusMap = activeValueStatusMap;
	}

	public ArrayList<Integer> getActiveValueRewardList() {
		return activeValueRewardList;
	}

	public void setActiveValueRewardList(ArrayList<Integer> activeValueRewardList) {
		this.activeValueRewardList = activeValueRewardList;
	}

	public int getActiveValue() {
		return activeValue;
	}

	public void setActiveValue(int activeValue) {
		this.activeValue = activeValue;
	}

}
