package com.mmorpg.mir.model.reward.model.sample;

import com.mmorpg.mir.model.reward.model.Reward;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

/**
 * 
 * 
 * @author Kuang Hao
 * @since v1.0 2013-2-21
 * 
 */
@Resource
public class RewardSample {
	@Id
	private String id;

	private RewardItemSample[] rewardItems;

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 创建奖励
	 * 
	 * @param formulaCtx
	 * @return
	 */
	public Reward createReward(Object formulaCtx) {
		Reward reward = Reward.valueOf();
		for (RewardItemSample sample : getRewardItems()) {
			if (sample.getValue() == 0 && sample.getFormula() == null) {
				continue;
			}
			reward.addRewardItem(sample.createRewardItem(formulaCtx));
		}
		return reward;
	}

	public String getId() {
		return id;
	}

	public RewardItemSample[] getRewardItems() {
		return rewardItems;
	}

	public void setRewardItems(RewardItemSample[] rewardItems) {
		this.rewardItems = rewardItems;
	}

}
