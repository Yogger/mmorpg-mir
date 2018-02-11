package com.mmorpg.mir.model.daily.manager;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.quest.model.Quest;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.reward.model.Reward;

@Component
public class DailyQuestManager implements IDailyQuestManager{
	

	private static DailyQuestManager INSTANCE;
	
	@PostConstruct
	void init() {
		INSTANCE = this;
	}
	
	public static DailyQuestManager getInstance() {
		return INSTANCE;
	}
	
	public void rewardDaily(Player player, Quest quest, String rewardChooserId) {
		// 自动发奖
		List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(quest,
				quest.getResource().getRewardChooserGroupId());
		Map<String, Object> params = RewardManager.getInstance().getRewardParams(player);
		Reward reward = RewardManager.getInstance().creatReward(player, rewardIds, params);
		RewardManager.getInstance().grantReward(player, reward, ModuleInfo.valueOf(ModuleType.QUEST, SubModuleType.DAILY_QUEST_REWARD));
    }
}
