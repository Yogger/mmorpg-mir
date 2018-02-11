package com.mmorpg.mir.model.soul.reward;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.reward.model.RewardItem;
import com.mmorpg.mir.model.reward.model.RewardProvider;
import com.mmorpg.mir.model.reward.model.RewardType;

@Component
public class SoulGrowItemRewardProvider extends RewardProvider {

	@Override
	public RewardType getType() {
		return RewardType.SOUL_GROW_ITEM;
	}

	@Override
	public void withdraw(Player player, RewardItem rewardItem, ModuleInfo module) {
		player.getSoul().addGrowItemCount(rewardItem.getCode());
	}

}
