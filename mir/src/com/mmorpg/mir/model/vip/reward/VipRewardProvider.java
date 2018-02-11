package com.mmorpg.mir.model.vip.reward;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.reward.model.RewardItem;
import com.mmorpg.mir.model.reward.model.RewardProvider;
import com.mmorpg.mir.model.reward.model.RewardType;

@Component
public class VipRewardProvider extends RewardProvider {

	@Autowired
	private PlayerManager playerManager;

	@Override
	public RewardType getType() {
		return RewardType.VIP;
	}

	@Override
	public void withdraw(Player player, RewardItem rewardItem, ModuleInfo module) {
		int amount = rewardItem.getAmount();
		if (amount <= 0) {
			throw new IllegalArgumentException("奖励vip配置参数非法，数量不能为负值！");
		}

		player.getVip().addGrowth(player.getObjectId(), amount);
		playerManager.updatePlayer(player);
	}

}
