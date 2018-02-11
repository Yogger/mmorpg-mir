package com.mmorpg.mir.model.warbook.reward;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.reward.model.RewardItem;
import com.mmorpg.mir.model.reward.model.RewardProvider;
import com.mmorpg.mir.model.reward.model.RewardType;
import com.mmorpg.mir.model.warbook.model.Warbook;

@Component
public class WarbookItemRewardProvider extends RewardProvider {

	@Override
	public RewardType getType() {
		return RewardType.WARBOOK_ITEM;
	}

	@Override
	public void withdraw(Player player, RewardItem rewardItem, ModuleInfo module) {
		Warbook warBook = player.getWarBook();
		warBook.addItemCount(rewardItem.getCode());
	}

}
