package com.mmorpg.mir.model.boss.packet;

import java.util.HashSet;

import com.mmorpg.mir.model.gameobjects.Player;

public class SM_Boss_Coins_Buy {
	private HashSet<String> payedBossCoinsIds;

	public static SM_Boss_Coins_Buy valueOf(Player player) {
		SM_Boss_Coins_Buy sm = new SM_Boss_Coins_Buy();
		sm.payedBossCoinsIds = new HashSet<String>(player.getBossData()
				.getPayedBossCoinsIds());
		return sm;
	}

	public HashSet<String> getPayedBossCoinsIds() {
		return payedBossCoinsIds;
	}

	public void setPayedBossCoinsIds(HashSet<String> payedBossCoinsIds) {
		this.payedBossCoinsIds = payedBossCoinsIds;
	}

}
