package com.mmorpg.mir.model.resourcecheck.handle;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.beauty.resource.BeautyGirlResource;
import com.mmorpg.mir.model.resourcecheck.ResourceCheckHandle;
import com.mmorpg.mir.model.reward.model.RewardType;
import com.mmorpg.mir.model.reward.model.sample.RewardItemSample;
import com.mmorpg.mir.model.reward.model.sample.RewardSample;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;

@Component
public class BeautyGirlSkillCheck extends ResourceCheckHandle {

	@Static
	private Storage<String, BeautyGirlResource> beautyGirlStorage;

	@Static
	private Storage<String, RewardSample> rewardSampleStorage;

	@Override
	public Class<?> getResourceClass() {
		return BeautyGirlResource.class;
	}

	@Override
	public void check() {
		List<String> errorLog = new ArrayList<String>();
		for (BeautyGirlResource resource : beautyGirlStorage.getAll()) {
			for (int initSkill : resource.getInitSkill()) {
				if (!resource.containSkillId(initSkill)) {
					String message = String.format("BeautyGirlResource[%s]的初始技能[%d]没有在skillSelectorItemSamples定义",
							resource.getId(), initSkill);
					errorLog.add(message);
				}
			}
		}

		for (RewardSample rewardSample : rewardSampleStorage.getAll()) {
			for (RewardItemSample item : rewardSample.getRewardItems()) {
				if (item.getType() == RewardType.BEAUTY_SKILL) {
					String girlId = item.getCode();
					int skillId = item.getValue();
					BeautyGirlResource resource = beautyGirlStorage.get(girlId, true);
					if (!resource.containSkillId(skillId)) {
						String message = String.format(
								"奖励id:[%s]对应的BeautyGirlResource[%s]的技能skillSelectorItemSamples没有配置id:[%d]",
								rewardSample.getId(), resource.getId(), skillId);
						errorLog.add(message);
					}
				}
			}
		}

		for (BeautyGirlResource resource : beautyGirlStorage.getAll()) {
			if (resource.getMasterStats().length == 1) {
				String message = String.format("BeautyGirlResource[%s]的给主人永久加的属性数组长度不能为1", resource.getId());
				errorLog.add(message);
			}

			if (resource.getBeautyStats().length == 1) {
				String message = String.format("BeautyGirlResource[%s]的美人属性数组长度不能为1", resource.getId());
				errorLog.add(message);
			}

			if (resource.getMasterStats().length != resource.getBeautyStats().length) {
				String message = String.format("BeautyGirlResource[%s]的美人属性数组长度不等于主人属性,级数[%d]美人属性数组长度[%d]主人属性数组长度[%d]",
						resource.getId(), resource.getNeedSense().length, resource.getBeautyStats().length,
						resource.getMasterStats().length);
				errorLog.add(message);
			}
		}
	}

}
