package com.mmorpg.mir.model.horse.reward;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.horse.model.Horse;
import com.mmorpg.mir.model.kingofwar.manager.KingOfWarManager;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.reward.model.RewardItem;
import com.mmorpg.mir.model.reward.model.RewardProvider;
import com.mmorpg.mir.model.reward.model.RewardType;

@Component
public class HorseIllutionRewardProvider extends RewardProvider {

	@Override
	public RewardType getType() {
		return RewardType.HORSE_ILLUTION;
	}

	@Override
	public void withdraw(Player player, RewardItem rewardItem, ModuleInfo module) {
		Horse horse = player.getHorse();
		boolean foreverActive = false;
		if ("0".equals(rewardItem.getCode())) {
			foreverActive = true;
		}
		long overTime = System.currentTimeMillis() + rewardItem.getAmount();
		if (overTime > KingOfWarManager.getInstance().getNextKingOfWarTime()) {
			overTime = KingOfWarManager.getInstance().getNextKingOfWarTime();
		}
		horse.active(player, overTime, foreverActive);
	}
}
