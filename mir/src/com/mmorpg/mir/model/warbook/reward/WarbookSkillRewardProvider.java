package com.mmorpg.mir.model.warbook.reward;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.reward.model.RewardItem;
import com.mmorpg.mir.model.reward.model.RewardProvider;
import com.mmorpg.mir.model.reward.model.RewardType;
import com.mmorpg.mir.model.warbook.WarbookConfig;
import com.mmorpg.mir.model.warbook.model.Warbook;
import com.mmorpg.mir.model.warbook.resource.WarbookResource;

@Component
public class WarbookSkillRewardProvider extends RewardProvider {

	@Autowired
	private WarbookConfig config;

	@Override
	public RewardType getType() {
		return RewardType.WARBOOK_SKILL;
	}

	@Override
	public void withdraw(Player player, RewardItem rewardItem, ModuleInfo module) {
		Integer skillId = Integer.parseInt(rewardItem.getCode());
		Warbook warBook = player.getWarBook();
		WarbookResource resource = config.getWarbookResourceByGrade(warBook.getGrade());
		if (resource.getSkillBoxCount() == 0 || warBook.isSkillLearned(skillId)) {
			return;
		}

		warBook.learnSkill(skillId);
	}
}
