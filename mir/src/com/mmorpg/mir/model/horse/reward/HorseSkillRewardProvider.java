package com.mmorpg.mir.model.horse.reward;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.horse.packet.SM_Horse_Learn_Skill;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.reward.model.RewardItem;
import com.mmorpg.mir.model.reward.model.RewardProvider;
import com.mmorpg.mir.model.reward.model.RewardType;
import com.mmorpg.mir.model.skill.SkillEngine;
import com.mmorpg.mir.model.skill.model.Skill;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.windforce.common.utility.RandomUtils;

@Component
public class HorseSkillRewardProvider extends RewardProvider {

	@Override
	public RewardType getType() {
		return RewardType.HORSE_SKILL;
	}

	@Override
	public void withdraw(Player player, RewardItem rewardItem, ModuleInfo module) {
		Integer skillId = Integer.valueOf(rewardItem.getCode());
		Map<Integer, Integer> skillArr = player.getHorse().getLearnedSkills();
		if (skillArr.containsValue(skillId) || player.getHorse().getResource().getSkillBoxCount() == 0) {
			return;
		}
		int index = RandomUtils.nextInt(player.getHorse().getResource().getSkillBoxCount());
		Integer before = skillArr.put(index, skillId);
		if (before != null) {
			player.getEffectController().removeEffect(before);
		}
		Skill skill = SkillEngine.getInstance().getSkill(null, skillId,
				player.getObjectId(), 0, 0, player, null);
		skill.noEffectorUseSkill();
		PacketSendUtility.sendPacket(player, SM_Horse_Learn_Skill.valueOf(index, skillId));
	}

	public static void main(String[] args) {
		System.out.println(RandomUtils.nextInt(0));
	}
}
