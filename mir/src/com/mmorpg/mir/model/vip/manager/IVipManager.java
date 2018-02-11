package com.mmorpg.mir.model.vip.manager;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.vip.resource.VipResource;
import com.windforce.common.resource.Storage;

public interface IVipManager {
	void rewardWeekReward(Player player, int sign);

	void rewardVipLevelReward(Player player, int level, int sign);

	void receiveTempVip(Player player, int sign);

	Storage<Integer, VipResource> getVipResource();

	VipResource getVipResource(int level);
}
