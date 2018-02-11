package com.mmorpg.mir.model.horse.reward;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.horse.model.Horse;
import com.mmorpg.mir.model.horse.service.HorseService;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.reward.model.RewardItem;
import com.mmorpg.mir.model.reward.model.RewardProvider;
import com.mmorpg.mir.model.reward.model.RewardType;

@Component
public class HorseEnhanceRewardProvider extends RewardProvider {

	@Autowired
	private HorseService horseService;

	@Override
	public RewardType getType() {
		return RewardType.HORSE_ENHANCE;
	}

	@Override
	public void withdraw(Player player, RewardItem rewardItem, ModuleInfo module) {
		Horse horse = player.getHorse();
		horse.addEhanceStat(rewardItem.getCode());
		horseService.flushHorse(player, false, false, true);
	}

}
