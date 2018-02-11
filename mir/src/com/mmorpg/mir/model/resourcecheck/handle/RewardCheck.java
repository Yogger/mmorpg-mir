package com.mmorpg.mir.model.resourcecheck.handle;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.copy.resource.CopyLadderRewardResource;
import com.mmorpg.mir.model.item.resource.ItemResource;
import com.mmorpg.mir.model.resourcecheck.ResourceCheckHandle;
import com.mmorpg.mir.model.reward.model.RewardType;
import com.mmorpg.mir.model.reward.model.sample.RewardItemSample;
import com.mmorpg.mir.model.reward.model.sample.RewardSample;
import com.mmorpg.mir.model.skill.resource.SkillResource;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;

@Component
public class RewardCheck extends ResourceCheckHandle {
	@Static
	private Storage<String, RewardSample> rewardSamples;

	@Static
	private Storage<String, ItemResource> itemResources;

	@Static
	private Storage<String, CopyLadderRewardResource> copyLadderRewardResources;
	
	@Static
	private Storage<Integer, SkillResource> skillResources;

	@Override
	public Class<?> getResourceClass() {
		return RewardSample.class;
	}

	@Override
	public void check() {
		for (RewardSample rs : rewardSamples.getAll()) {
			for (RewardItemSample ris : rs.getRewardItems()) {
				if (ris.getType() == RewardType.ITEM) {
					itemResources.get(ris.getCode(), true);
				} else if (ris.getType() == RewardType.SKILL || ris.getType() == RewardType.BUFF) {
					skillResources.get(Integer.valueOf(ris.getCode()), true);
				}
				// else if (ris.getType() == RewardType.EXP || ris.getType() ==
				// RewardType.COUNTRY_CURRENCY
				// || ris.getType() == RewardType.CURRENCY) {
				// if (ris.getValue() == 0 && ris.getFormula() == null) {
				// throw new RuntimeException(String.format(
				// "RewardSample id[%s] getValue[%s] getFormula[%s]！ ",
				// rs.getId(), ris.getValue(),
				// ris.getFormula()));
				// }
				// }

			}
		}

		for (CopyLadderRewardResource clrr : copyLadderRewardResources.getAll()) {
			RewardSample rs = rewardSamples.get(clrr.getRewardId(), false);
			if (rs == null) {
				throw new RuntimeException(String.format("CopyLadderRewardResource id[%s] getRewardId[%s]不存在！ ",
						clrr.getId(), clrr.getRewardId()));
			}
		}
		
	}
}
