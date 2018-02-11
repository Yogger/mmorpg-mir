package com.mmorpg.mir.model.nickname.reward;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.nickname.manager.NicknameManager;
import com.mmorpg.mir.model.nickname.model.Nickname;
import com.mmorpg.mir.model.nickname.resource.NicknameResource;
import com.mmorpg.mir.model.reward.model.RewardItem;
import com.mmorpg.mir.model.reward.model.RewardProvider;
import com.mmorpg.mir.model.reward.model.RewardType;
import com.windforce.common.utility.DateUtils;

@Component
public class NicknameRewardsProvider extends RewardProvider {

	@Autowired
	private NicknameManager nicknameManager;

	private static final String DEPRECATED = "DEPRECATED";
	private static final String DEPRECATED_CRON = "DEPRECATED_CRON";

	@Override
	public RewardType getType() {
		return RewardType.NICKNAME;
	}

	@Override
	public void withdraw(Player player, RewardItem rewardItem, ModuleInfo module) {
		long deprecatedTime = 0;
		if (rewardItem.getParms() != null && rewardItem.getParms().containsKey(DEPRECATED_CRON)) {
			String deprecatedCron = rewardItem.getParms().get(DEPRECATED_CRON);
			deprecatedTime = DateUtils.getNextTime(deprecatedCron, new Date()).getTime() - System.currentTimeMillis(); 
		} else if (rewardItem.getParms() != null && rewardItem.getParms().containsKey(DEPRECATED)) {
			String deprecated = rewardItem.getParms().get(DEPRECATED);
			deprecatedTime = Long.valueOf(deprecated);
		}
		long time = (deprecatedTime == 0L? 0L: (deprecatedTime + System.currentTimeMillis()));
		Integer nameId = rewardItem.getAmount();
		NicknameResource nicknameResource = nicknameManager.getNicknameResources().get(nameId, true);
		Nickname nickname = nicknameResource.creatNickname(time);
		player.getNicknamePool().checkDeprecat(true);
		player.getNicknamePool().addNickname(nickname);
	}
}
