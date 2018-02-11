package com.mmorpg.mir.model.artifact.reward;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.artifact.core.ArtifactService;
import com.mmorpg.mir.model.artifact.model.Artifact;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.reward.model.RewardItem;
import com.mmorpg.mir.model.reward.model.RewardProvider;
import com.mmorpg.mir.model.reward.model.RewardType;

@Component
public class ArtifactRewardProvider extends RewardProvider {

	@Autowired
	private ArtifactService artifactService;

	@Override
	public RewardType getType() {
		return RewardType.ARTIFACT;
	}

	@Override
	public void withdraw(Player player, RewardItem rewardItem, ModuleInfo module) {
		Artifact artifact = player.getArtifact();
		int level = rewardItem.getAmount();
		while (artifact.getLevel() < level) {
			if (artifact.getResource().getCount() == 0) {
				break;
			} else {
				artifact.addLevel();
			}
		}
		artifactService.flushAritfact(player, true, true);
	}

}
