package com.mmorpg.mir.model.fashion.reward;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.fashion.FashionConfig;
import com.mmorpg.mir.model.fashion.model.FashionPool;
import com.mmorpg.mir.model.fashion.resource.FashionResource;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.moduleopen.manager.ModuleOpenManager;
import com.mmorpg.mir.model.reward.model.RewardItem;
import com.mmorpg.mir.model.reward.model.RewardProvider;
import com.mmorpg.mir.model.reward.model.RewardType;

@Component
public class FashionRewardProvider extends RewardProvider {

	@Autowired
	private ModuleOpenManager moduleOpenManager;

	@Override
	public RewardType getType() {
		return RewardType.FASHION;
	}

	@Override
	public void withdraw(Player player, RewardItem rewardItem, ModuleInfo module) {
		if (!moduleOpenManager.isOpenByModuleKey(player, ModuleKey.FASHION)) {
			throw new ManagedException(ManagedErrorCode.MODULE_NOT_OPEN);
		}

		int fashionId = Integer.parseInt(rewardItem.getCode());
		FashionPool pool = player.getFashionPool();
		if (!pool.containFashion(fashionId)) {
			pool.gainFashion(fashionId);
		} else {
			FashionResource resource = FashionConfig.getInstance().fashionStorage.get(fashionId, true);
			pool.addExp(resource.getExp());
		}
	}
}
