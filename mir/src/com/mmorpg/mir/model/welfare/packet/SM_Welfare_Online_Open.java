package com.mmorpg.mir.model.welfare.packet;

import java.util.ArrayList;
import java.util.Map;

import com.mmorpg.mir.model.gameobjects.Player;

public class SM_Welfare_Online_Open {

	private long onlineTimeMillis; // 在线累计时间
	private ArrayList<Integer> rewardedList;// 已经领奖的档
	private Map<Integer,ArrayList<String>> rewardIdMap;//每一档领取到的奖励

	public static SM_Welfare_Online_Open valueOf(Player player,long tmpOnlineTimes) {
		SM_Welfare_Online_Open sm = new SM_Welfare_Online_Open();
		sm.setOnlineTimeMillis(tmpOnlineTimes);
		sm.setRewardedList(player.getWelfare().getOnlineReward().getRewardedList());
		sm.setRewardIdMap(player.getWelfare().getOnlineReward().getRewardIdMap());
		return sm;
	}

	public long getOnlineTimeMillis() {
		return onlineTimeMillis;
	}

	public void setOnlineTimeMillis(long onlineTimeMillis) {
		this.onlineTimeMillis = onlineTimeMillis;
	}

	public ArrayList<Integer> getRewardedList() {
		return rewardedList;
	}

	public void setRewardedList(ArrayList<Integer> rewardedList) {
		this.rewardedList = rewardedList;
	}

	public Map<Integer, ArrayList<String>> getRewardIdMap() {
		return rewardIdMap;
	}

	public void setRewardIdMap(Map<Integer, ArrayList<String>> rewardIdMap) {
		this.rewardIdMap = rewardIdMap;
	}

}
