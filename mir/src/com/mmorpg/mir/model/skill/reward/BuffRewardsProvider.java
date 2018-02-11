package com.mmorpg.mir.model.skill.reward;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.reward.model.RewardItem;
import com.mmorpg.mir.model.reward.model.RewardProvider;
import com.mmorpg.mir.model.reward.model.RewardType;
import com.mmorpg.mir.model.skill.SkillEngine;
import com.mmorpg.mir.model.skill.model.Skill;

@Component
public class BuffRewardsProvider extends RewardProvider {

	@Override
	public RewardType getType() {
		return RewardType.BUFF;
	}

	@Override
	public void withdraw(Player player, RewardItem rewardItem, ModuleInfo module) {
		for (int i = 0; i < rewardItem.getAmount(); i++) {
			Skill skill = SkillEngine.getInstance().getSkill(null, Integer.valueOf(rewardItem.getCode()),
					player.getObjectId(), 0, 0, player, null);
			skill.noEffectorUseSkill();
		}
	}
}
