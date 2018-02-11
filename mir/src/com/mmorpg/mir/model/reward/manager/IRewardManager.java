package com.mmorpg.mir.model.reward.manager;

import java.util.List;
import java.util.Map;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.reward.model.RewardProvider;

public interface IRewardManager {
	Reward grantReward(Player player, List<String> rewardIds, ModuleInfo module, Map<String, Object> custom);

	Reward creatReward(Player player, String rewardId, Map<String, Object> custom);

	Reward creatReward(Player player, List<String> rewardIds, Map<String, Object> custom);

	Reward createRewardButNotMerge(Player player, List<String> rewardIds, Map<String, Object> custom);

	Reward grantReward(Player player, List<String> rewardIds, ModuleInfo module);

	Reward grantReward(Player player, String rewardId, ModuleInfo module);

	Reward grantReward(Player player, String rewardId, ModuleInfo module, Map<String, Object> custom);

	int calcRewardNeedPackSize(Reward reward);

	Reward grantReward(Player player, Reward reward, ModuleInfo module);

	Reward grantReward(Player player, Reward[] rewards, ModuleInfo module);

	void registerProvider(RewardProvider rewardProvider);
	
	public Map<String, Object> getRewardParams(Player player);
}
