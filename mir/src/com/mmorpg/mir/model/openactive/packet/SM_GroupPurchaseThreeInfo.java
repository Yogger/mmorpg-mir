package com.mmorpg.mir.model.openactive.packet;

import java.util.HashMap;
import java.util.HashSet;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.openactive.model.GroupPurchaseThree;
import com.mmorpg.mir.model.serverstate.ServerState;

public class SM_GroupPurchaseThreeInfo {
	// 各个档次参加团购人数
	private HashMap<String, Integer> attendAmount;
	// 已经领取过的档次
	private HashSet<String> rewarded;
	// 活动期间获得的元宝
	private long gold;

	public static SM_GroupPurchaseThreeInfo valueOf(Player player) {
		SM_GroupPurchaseThreeInfo result = new SM_GroupPurchaseThreeInfo();
		HashMap<String, Integer> rest = new HashMap<String, Integer>();
		if (ServerState.getInstance().getGroupPurchasePlayers3() == null) {
			result.attendAmount = rest;
			result.gold = 0L;
			result.rewarded = new HashSet<String>();
			return result;
		}
		GroupPurchaseThree groupPurchaseThree = ServerState.getInstance().getPlayerGroupPurchases3()
				.get(player.getObjectId());
		for (String key : ServerState.getInstance().getGroupPurchasePlayers3().keySet()) {
			rest.put(key, ServerState.getInstance().getGroupPurchasePlayers3().get(key).size());
		}
		if (groupPurchaseThree == null) {
			result.rewarded = new HashSet<String>();
			result.gold = 0;
		} else {
			result.rewarded = groupPurchaseThree.getRewarded();
			result.gold = groupPurchaseThree.getGoldAmount();
		}
		result.attendAmount = rest;
		return result;
	}

	public HashMap<String, Integer> getAttendAmount() {
		return attendAmount;
	}

	public void setAttendAmount(HashMap<String, Integer> attendAmount) {
		this.attendAmount = attendAmount;
	}

	public HashSet<String> getRewarded() {
		return rewarded;
	}

	public void setRewarded(HashSet<String> rewarded) {
		this.rewarded = rewarded;
	}

	public long getGold() {
		return gold;
	}

	public void setGold(long gold) {
		this.gold = gold;
	}

}
