package com.mmorpg.mir.model.item.reward;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.item.Equipment;
import com.mmorpg.mir.model.item.core.ItemManager;
import com.mmorpg.mir.model.item.resource.EquipmentSmeltResource;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.reward.model.RewardItem;
import com.mmorpg.mir.model.reward.model.RewardProvider;
import com.mmorpg.mir.model.reward.model.RewardType;

@Component
public class SmeltRewardsProvider extends RewardProvider {

	@Autowired
	private ItemManager itemManager;
	
	@Override
	public RewardType getType() {
		return RewardType.SMELT;
	}

	@Override
	public void withdraw(Player player, RewardItem rewardItem, ModuleInfo module) {
		int totalAddValue = rewardItem.getAmount();
		// 将熔炼值加到玩家身上
		int level = player.getEquipmentStorage().getSmeltLevel();
		int smeltValue = player.getEquipmentStorage().getSmeltValue();
		EquipmentSmeltResource resource = itemManager.getEquipmentSmeltResource(level);
		if (resource.getNextLevel() == 0) {
			return;
		}

		while (totalAddValue + smeltValue >= resource.getLevelMax() && (resource.getLevelMax() != 0)) {
			totalAddValue -= (resource.getLevelMax() - smeltValue);
			if ((level = resource.getNextLevel()) != 0) {
				resource = itemManager.getEquipmentSmeltResource(level);
			} else {
				break;
			}
			smeltValue = 0;
		}
		if (resource.getLevelMax() != 0) {
			smeltValue += totalAddValue;
		} else {
			smeltValue = 0;
		}

		player.getEquipmentStorage().setSmeltLevel(level);
		player.getEquipmentStorage().setSmeltValue(smeltValue);
		player.getGameStats().replaceModifiers(Equipment.SMELT_STAT, resource.getAllStats(), true);
	}


}
