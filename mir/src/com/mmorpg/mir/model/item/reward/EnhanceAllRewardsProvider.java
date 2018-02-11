package com.mmorpg.mir.model.item.reward;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.log.LogManager;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectId;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectType;
import com.mmorpg.mir.model.item.Equipment;
import com.mmorpg.mir.model.item.config.ItemConfig;
import com.mmorpg.mir.model.item.core.ItemManager;
import com.mmorpg.mir.model.item.model.EquipmentStorageType;
import com.mmorpg.mir.model.item.packet.SM_Equipment_Update;
import com.mmorpg.mir.model.item.resource.ItemResource;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.reward.model.RewardItem;
import com.mmorpg.mir.model.reward.model.RewardProvider;
import com.mmorpg.mir.model.reward.model.RewardType;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.welfare.event.EnhanceEquipmentEvent;
import com.windforce.common.event.core.EventBusManager;

@Component
public class EnhanceAllRewardsProvider extends RewardProvider {

	@Override
	public RewardType getType() {
		return RewardType.EQUIPMENTS_ENHANCE;
	}

	@Override
	public void withdraw(Player player, RewardItem rewardItem, ModuleInfo module) {
		int maxLevel = ItemConfig.getInstance().getEquipmentMaxEnhanceLevel();
		for (Equipment equip : player.getEquipmentStorage().getEquipments()) {
			if (equip == null) {
				continue;
			}
			if (equip.getEnhanceLevel() >= maxLevel) {
				continue;
			}
			ItemResource resource = ItemManager.getInstance().getResource(rewardItem.getCode());
			if (equip.getEnhanceLevel() > resource.getEnhanceHigh()) {
				continue;
			}
			equip.upgradeEnhanceLevel();
			player.getGameStats().replaceModifiers(StatEffectId.valueOf(equip.getObjectId(), StatEffectType.EQUIPMENT),
					equip.getModifiers(), false);
			PacketSendUtility.sendPacket(player, SM_Equipment_Update.valueOf(player.getPack().getEnhanceLuckPoints(),
					player.getPack().getEnhanceLuckTime(), equip));
			LogManager.addEnhanceLog(player.getPlayerEnt().getAccountName(), player.getName(), player.getObjectId(),
					System.currentTimeMillis(), equip.getObjectId(), equip.getKey(), resource.getLevel(),
					equip.getEnhanceLevel());
		}
		ItemManager.getInstance().calculateStarSuit(player, false, EquipmentStorageType.PLAYER);
		player.getGameStats().recomputeStats();
		EventBusManager.getInstance().submit(EnhanceEquipmentEvent.valueOf(player));
	}

}
