package com.mmorpg.mir.model.artifact.reward;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.artifact.model.Artifact;
import com.mmorpg.mir.model.artifact.packet.SM_Artifact_Buff;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.reward.model.RewardItem;
import com.mmorpg.mir.model.reward.model.RewardProvider;
import com.mmorpg.mir.model.reward.model.RewardType;
import com.mmorpg.mir.model.utils.PacketSendUtility;

@Component
public class ArtifactBuffRewardProvider extends RewardProvider {

	@Override
	public RewardType getType() {
		return RewardType.ARTIFACT_BUFF;
	}

	@Override
	public void withdraw(Player player, RewardItem rewardItem, ModuleInfo module) {
		Artifact artifact = player.getArtifact();
		long addMillSecond = rewardItem.getAmount() * DateUtils.MILLIS_PER_SECOND;
		artifact.addBuffTime(addMillSecond);
		PacketSendUtility.sendPacket(player,
				SM_Artifact_Buff.valueOf(artifact.getBuffEndTime(), artifact.getAppLevel()));
	}
}
