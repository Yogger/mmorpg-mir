package com.mmorpg.mir.model.lifegrid.reward;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.lifegrid.model.LifeGridPool;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.moduleopen.manager.ModuleOpenManager;
import com.mmorpg.mir.model.reward.model.RewardItem;
import com.mmorpg.mir.model.reward.model.RewardProvider;
import com.mmorpg.mir.model.reward.model.RewardType;

@Component
public class LifeGridPointRewardProvider extends RewardProvider {

	@Autowired
	private ModuleOpenManager moduleOpenManager;

	@Override
	public RewardType getType() {
		return RewardType.LIFEGRID_POINT;
	}

	@Override
	public void withdraw(Player player, RewardItem rewardItem, ModuleInfo module) {
		if (!moduleOpenManager.isOpenByModuleKey(player, ModuleKey.LIFEGRID)) {
			throw new ManagedException(ManagedErrorCode.MODULE_NOT_OPEN);
		}
		LifeGridPool pool = player.getLifeGridPool();
		int value = rewardItem.getAmount();
		pool.addPoint(module, value);
	}
}
