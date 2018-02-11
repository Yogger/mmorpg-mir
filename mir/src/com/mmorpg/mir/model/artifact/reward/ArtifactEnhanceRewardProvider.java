package com.mmorpg.mir.model.artifact.reward;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.item.core.ItemManager;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.reward.model.RewardItem;
import com.mmorpg.mir.model.reward.model.RewardProvider;
import com.mmorpg.mir.model.reward.model.RewardType;

@Component
public class ArtifactEnhanceRewardProvider  extends RewardProvider {

	@Override
	public RewardType getType() {
		return RewardType.ARTIFACT_ENHANCE;
	}

	@Override
	public void withdraw(Player player, RewardItem rewardItem, ModuleInfo module) {
		ItemManager.getInstance().getResource(rewardItem.getCode());
		player.getArtifact().addEhanceStat(rewardItem.getCode());
	}

}
