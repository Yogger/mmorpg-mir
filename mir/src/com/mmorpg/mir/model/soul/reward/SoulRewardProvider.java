package com.mmorpg.mir.model.soul.reward;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.reward.model.RewardItem;
import com.mmorpg.mir.model.reward.model.RewardProvider;
import com.mmorpg.mir.model.reward.model.RewardType;
import com.mmorpg.mir.model.soul.core.SoulService;
import com.mmorpg.mir.model.soul.model.Soul;

@Component
public class SoulRewardProvider extends RewardProvider {

	@Autowired
	private SoulService soulService;

	@Override
	public RewardType getType() {
		return RewardType.SOUL;
	}

	@Override
	public void withdraw(Player player, RewardItem rewardItem, ModuleInfo module) {
		Soul soul = player.getSoul();
		int level = rewardItem.getAmount();
		while (soul.getLevel() < level) {
			if (soul.getResource().getCount() == 0) {
				break;
			} else {
				soul.setLevel(soul.getLevel() + 1);
			}
		}
		soulService.flushSoul(player, true, true);
	}
}
