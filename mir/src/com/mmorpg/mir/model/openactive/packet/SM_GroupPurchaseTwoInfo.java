package com.mmorpg.mir.model.openactive.packet;

import java.util.HashMap;
import java.util.HashSet;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.openactive.model.GroupPurchaseTwo;
import com.mmorpg.mir.model.serverstate.ServerState;

public class SM_GroupPurchaseTwoInfo {
	// 各个档次参加团购人数
	private HashMap<String, Integer> attendAmount;
	// 已经领取过的档次
	private HashSet<String> rewarded;
	// 活动期间获得的元宝
	private long gold;

	public static SM_GroupPurchaseTwoInfo valueOf(Player player) {
		SM_GroupPurchaseTwoInfo result = new SM_GroupPurchaseTwoInfo();
		HashMap<String, Integer> rest = new HashMap<String, Integer>();
		if (ServerState.getInstance().getPlayerGroupPurchases2() == null) {
			result.attendAmount = rest;
			result.gold = 0L;
			result.rewarded = new HashSet<String>();
			return result;
		}
		GroupPurchaseTwo groupPurchaseTwo = ServerState.getInstance().getPlayerGroupPurchases2()
				.get(player.getObjectId());
		for (String key : ServerState.getInstance().getGroupPurchasePlayers2().keySet()) {
			rest.put(key, ServerState.getInstance().getGroupPurchasePlayers2().get(key).size());
		}
		if (groupPurchaseTwo == null) {
			result.rewarded = new HashSet<String>();
			result.gold = 0;
		} else {
			result.rewarded = groupPurchaseTwo.getRewarded();
			result.gold = groupPurchaseTwo.getGoldAmount();
		}
		result.attendAmount=rest;
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
