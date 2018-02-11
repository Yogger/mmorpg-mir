package com.mmorpg.mir.model.fashion.reward;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.fashion.model.FashionPool;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.moduleopen.manager.ModuleOpenManager;
import com.mmorpg.mir.model.reward.model.RewardItem;
import com.mmorpg.mir.model.reward.model.RewardProvider;
import com.mmorpg.mir.model.reward.model.RewardType;

@Component
public class FashionExpRewardProvider extends RewardProvider {

	@Autowired
	private ModuleOpenManager moduleOpenManager;

	@Override
	public RewardType getType() {
		return RewardType.FASHION_EXP;
	}

	@Override
	public void withdraw(Player player, RewardItem rewardItem, ModuleInfo module) {
		if (!moduleOpenManager.isOpenByModuleKey(player, ModuleKey.FASHION)) {
			throw new ManagedException(ManagedErrorCode.MODULE_NOT_OPEN);
		}
		FashionPool pool = player.getFashionPool();
		pool.addExp(rewardItem.getAmount());
	}

}
