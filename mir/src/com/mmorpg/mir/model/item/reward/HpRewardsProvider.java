package com.mmorpg.mir.model.item.reward;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.reward.model.RewardItem;
import com.mmorpg.mir.model.reward.model.RewardProvider;
import com.mmorpg.mir.model.reward.model.RewardType;

@Component
public class HpRewardsProvider extends RewardProvider {

	@Override
	public RewardType getType() {
		return RewardType.HP;
	}

	@Override
	public void withdraw(Player player, RewardItem rewardItem, ModuleInfo module) {
		player.getLifeStats().increaseHp(rewardItem.getAmount());
	}

}
