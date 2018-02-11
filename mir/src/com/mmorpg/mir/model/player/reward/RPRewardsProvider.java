package com.mmorpg.mir.model.player.reward;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.player.service.PlayerService;
import com.mmorpg.mir.model.reward.model.RewardItem;
import com.mmorpg.mir.model.reward.model.RewardProvider;
import com.mmorpg.mir.model.reward.model.RewardType;

@Component
public class RPRewardsProvider extends RewardProvider {
	@Autowired
	private PlayerService playerService;

	@Override
	public RewardType getType() {
		return RewardType.RP;
	}

	@Override
	public void withdraw(Player player, RewardItem rewardItem, ModuleInfo module) {
		playerService.useItemAddRP(player, rewardItem.getAmount());
	}

}
