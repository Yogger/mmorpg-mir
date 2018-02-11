package com.mmorpg.mir.model.commonactivity.model;

import java.util.List;
import java.util.Map;

import com.windforce.common.utility.New;

public class CommonGoldTreasury {
	/** Integer: 组号， Integer： 序列号， String: 已经抽到的奖励 */
	private Map<Integer, GoldTreasuryLog> treasurys;

	public static CommonGoldTreasury valueOf() {
		CommonGoldTreasury cgt = new CommonGoldTreasury();
		cgt.treasurys = New.hashMap();
		return cgt;
	}

	public Map<Integer, GoldTreasuryLog> getTreasurys() {
		return treasurys;
	}

	public void setTreasurys(Map<Integer, GoldTreasuryLog> treasurys) {
		this.treasurys = treasurys;
	}

	public void addLog(int groupId, int index, String rewardId) {
		if (!treasurys.containsKey(groupId)) {
			treasurys.put(groupId, GoldTreasuryLog.valueOf());
		}
		treasurys.get(groupId).addLog(index, rewardId);
	}

	public List<String> getGroupRewarded(int groupId) {
		if (!treasurys.containsKey(groupId)) {
			treasurys.put(groupId, GoldTreasuryLog.valueOf());
		}
		return treasurys.get(groupId).rewards();
	}

	public int getResetTimes(int groupId) {
		return treasurys.get(groupId).getResetTimes();
	}

	public void reset(int groupId) {
		treasurys.get(groupId).reset();
	}

	public int getTreasuryCount(int groupId) {
		return treasurys.get(groupId).getGroupLog().size();
	}

	public boolean hashDrwarded(int groupId, int index) {
		if(!treasurys.containsKey(groupId)){
			return false;
		}
		if(!treasurys.get(groupId).getGroupLog().containsKey(index)){
			return false;
		}
		return true;
	}
}
