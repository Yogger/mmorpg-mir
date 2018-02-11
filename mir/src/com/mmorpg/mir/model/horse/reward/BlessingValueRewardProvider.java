package com.mmorpg.mir.model.horse.reward;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.artifact.core.ArtifactManager;
import com.mmorpg.mir.model.artifact.core.ArtifactService;
import com.mmorpg.mir.model.artifact.resource.ArtifactResource;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.horse.manager.HorseManager;
import com.mmorpg.mir.model.horse.resource.HorseResource;
import com.mmorpg.mir.model.horse.service.HorseService;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.reward.model.RewardItem;
import com.mmorpg.mir.model.reward.model.RewardProvider;
import com.mmorpg.mir.model.reward.model.RewardType;
import com.mmorpg.mir.model.soul.core.SoulManager;
import com.mmorpg.mir.model.soul.core.SoulService;
import com.mmorpg.mir.model.soul.resource.SoulResource;

@Component
public class BlessingValueRewardProvider extends RewardProvider {

	@Autowired
	private HorseService horseService;

	@Autowired
	private SoulService soulService;

	@Autowired
	private ArtifactService artifactService;

	@Override
	public RewardType getType() {
		return RewardType.BLESSING_VALUE;
	}

	@Override
	public void withdraw(Player player, RewardItem rewardItem, ModuleInfo module) {
//		float rate = Float.parseFloat(rewardItem.getParms().get("RATE"));
//		if ("HORSE".equals(rewardItem.getCode())) {
//			HorseResource resource = HorseManager.getInstance().getHorseResource(player.getHorse().getGrade());
//			int amount = (int) (rate * resource.getMaxBless());
//			horseService.useBlessItem(player, amount);
//		} else if ("SOUL".equals(rewardItem.getCode())) {
//			SoulResource resource = SoulManager.getInstance().getSoulResource(player.getSoul().getLevel());
//			int amount = (int) (rate * resource.getMaxBless());
//			soulService.useBlessItem(player, amount);
//		} else if ("ARTIFACT".equals(rewardItem.getCode())) {
//			ArtifactResource resource = ArtifactManager.getInstance().getArtifactResource(
//					player.getArtifact().getLevel());
//			int amount = (int) (rate * resource.getMaxBless());
//			artifactService.useBlessItem(player, amount);
//		}
	}
}
