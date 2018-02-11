package com.mmorpg.mir.model.openactive.packet;

import java.util.HashMap;
import java.util.HashSet;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.openactive.OpenActiveConfig;
import com.mmorpg.mir.model.openactive.model.GroupPurchase;
import com.mmorpg.mir.model.serverstate.ServerState;

public class SM_GroupPurchaseInfo {
	// 各个档次参加团购人数
	private HashMap<String, Integer> attendAmount;
	// 已经领取过的档次
	private HashSet<String> rewarded;
	// 活动期间获得的元宝
	private long gold;

	public static SM_GroupPurchaseInfo valueOf(Player player) {
		SM_GroupPurchaseInfo result = new SM_GroupPurchaseInfo();
		HashMap<String, Integer> rest = new HashMap<String, Integer>();
		boolean timeVerify = OpenActiveConfig.getInstance().getGroupPurchaseTimeConds().verify(null);
		if (timeVerify) {
			if (ServerState.getInstance().getGroupPurchasePlayers() != null) {
				for (String key : ServerState.getInstance().getGroupPurchasePlayers().keySet()) {
					rest.put(key, ServerState.getInstance().getGroupPurchasePlayers().get(key).size());
				}
			}
			result.attendAmount = rest;
			if (ServerState.getInstance().getPlayerGroupPurchases() == null) {
				result.rewarded = new HashSet<String>();
				result.gold = 0;
				return result;
			}
			GroupPurchase groupPurchase = ServerState.getInstance().getPlayerGroupPurchases().get(player.getObjectId());
			if (groupPurchase == null) {
				result.rewarded = new HashSet<String>();
				result.gold = 0;
			} else {
				result.rewarded = groupPurchase.getRewarded();
				result.gold = groupPurchase.getGoldAmount();
			}
		} else {
			result.attendAmount = rest;
			result.gold = 0;
			result.rewarded = new HashSet<String>();
		}
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
