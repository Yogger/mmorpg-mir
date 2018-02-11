package com.mmorpg.mir.model.horse.reward;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.horse.model.Horse;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.reward.model.RewardItem;
import com.mmorpg.mir.model.reward.model.RewardProvider;
import com.mmorpg.mir.model.reward.model.RewardType;

@Component
public class HorseGrowItemRewardProvider extends RewardProvider {

	@Override
	public RewardType getType() {
		return RewardType.HORSE_GROW_ITEM;
	}

	@Override
	public void withdraw(Player player, RewardItem rewardItem, ModuleInfo module) {
		Horse horse = player.getHorse();
		horse.addGrowItemCount(rewardItem.getCode());
	}

}
