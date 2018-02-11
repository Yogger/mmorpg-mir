package com.mmorpg.mir.model.skill.reward;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.reward.model.RewardItem;
import com.mmorpg.mir.model.reward.model.RewardProvider;
import com.mmorpg.mir.model.reward.model.RewardType;
import com.mmorpg.mir.model.skill.manager.SkillManager;

@Component
public class SkillRewardsProvider extends RewardProvider {

	@Autowired
	private PlayerManager playerManager;

	@Override
	public RewardType getType() {
		return RewardType.SKILL;
	}
 
	@Override
	public void withdraw(Player player, RewardItem rewardItem, ModuleInfo module) {

		int skillId = Integer.valueOf(rewardItem.getCode());
		if (player.getSkillList().isContainSkill(skillId)) {
			return;
		}
		player.getSkillList().addRewardSkill(SkillManager.getInstance().getResource(skillId));

		playerManager.updatePlayer(player);

	}
}
