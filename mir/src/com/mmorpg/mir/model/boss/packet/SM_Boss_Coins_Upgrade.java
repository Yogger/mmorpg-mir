package com.mmorpg.mir.model.boss.packet;

import com.mmorpg.mir.model.gameobjects.Player;

public class SM_Boss_Coins_Upgrade {
	private String bossCoinsLevel;
	
	public static SM_Boss_Coins_Upgrade valueOf(Player player) {
		SM_Boss_Coins_Upgrade sm = new SM_Boss_Coins_Upgrade();
		sm.bossCoinsLevel = player.getBossData().getBossCoinsLevel();
		return sm;
	}

	public String getBossCoinsLevel() {
		return bossCoinsLevel;
	}

	public void setBossCoinsLevel(String bossCoinsLevel) {
		this.bossCoinsLevel = bossCoinsLevel;
	}

}
