package com.mmorpg.mir.model.item.reward;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.log.LogManager;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.item.AbstractItem;
import com.mmorpg.mir.model.item.Equipment;
import com.mmorpg.mir.model.item.AbstractItem.ItemState;
import com.mmorpg.mir.model.item.config.ItemConfig;
import com.mmorpg.mir.model.item.core.ItemManager;
import com.mmorpg.mir.model.item.event.CollectItemsEvent;
import com.mmorpg.mir.model.item.model.EquipmentStatType;
import com.mmorpg.mir.model.item.packet.SM_Packet_Update;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.reward.model.RewardItem;
import com.mmorpg.mir.model.reward.model.RewardProvider;
import com.mmorpg.mir.model.reward.model.RewardType;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.windforce.common.event.core.EventBusManager;
import com.windforce.common.utility.DateUtils;

@Component
public class ItemRewardsProvider extends RewardProvider {

	@Autowired
	private PlayerManager playerManager;

	@Autowired
	private ItemManager itemManager;

	@Override
	public RewardType getType() {
		return RewardType.ITEM;
	}

	@Override
	public void withdraw(Player player, RewardItem rewardItem, ModuleInfo module) {
		AbstractItem[] items = itemManager.createItems(rewardItem.getCode(), rewardItem.getAmount());
		boolean isToTreasurePack = false;
		if (rewardItem.getParms() != null && rewardItem.getParms().containsKey("TO_TREASURE_PACK")) {
			isToTreasurePack = true;
		}

		for (int i = 0; i < items.length; i++) {
			// 装备注入属性
			if (items[i] instanceof Equipment) {
				Equipment equip = (Equipment) items[i];
				if (rewardItem.getParms() == null)
					break;
				for (Map.Entry<String, String> entry : rewardItem.getParms().entrySet()) {
					if (entry.getKey().equals(EquipmentStatType.ELEMENT_STAT.name())) {
						equip.setElement(Integer.valueOf(entry.getValue()));
					}
					if (entry.getKey().equals(EquipmentStatType.SOUL_STAT.name())) {
						equip.addExtraStats(EquipmentStatType.SOUL_STAT.getValue(), entry.getValue().split("[|]"));
					}
					if (entry.getKey().equals(EquipmentStatType.GOD_STAT.name())) {
						equip.addExtraStats(EquipmentStatType.GOD_STAT.getValue(), entry.getValue());
					}
					if (!ItemConfig.getInstance().DELETE_EQUIPMENT_PERFECT_STAT.getValue()) {
						if (entry.getKey().equals(EquipmentStatType.PERFECT_STAT.name())) {
							equip.addExtraStats(EquipmentStatType.PERFECT_STAT.getValue(), entry.getValue()
									.split("[|]"));
						}
					}
					if (entry.getKey().equals(EquipmentStatType.COMMON_STAT.name())) {
						equip.addExtraStats(EquipmentStatType.COMMON_STAT.getValue(), entry.getValue());
					}

					if (entry.getKey().equals(EquipmentStatType.SUICIDE_TURN.name())) {
						equip.addExtraStats(EquipmentStatType.SUICIDE_TURN.getValue(), entry.getValue());
					}

					if (entry.getKey().equals("ENHANCE_LEVEL")) {
						equip.setEnhanceLevel(Integer.valueOf(entry.getValue()));
					}

					if (entry.getKey().equals("GRADE")) {
						equip.setGrade(Integer.valueOf(entry.getValue()));
					}

				}
			}
		}

		for (AbstractItem item : items) {
			if (rewardItem.getParms() != null) {
				// 过期道具
				String deprecatedCron = rewardItem.getParms().get(ItemState.DEPRECATED.name() + "_CRON");
				String deprecated = rewardItem.getParms().get(ItemState.DEPRECATED.name());
				if (deprecatedCron != null) {
					long delay = 0;
					if (deprecated != null) {
						delay = Long.valueOf(deprecated) * DateUtils.MILLIS_PER_MINUTE;
					}
					long nextTime = DateUtils.getNextTime(deprecatedCron, new Date()).getTime();
					item.setDeprecatedTime(nextTime + delay);
					item.checkDeprecated();
					// 会过期的道具，默认就绑定
					item.openState(ItemState.BIND.getMark());
				} else if (deprecated != null) {
					long deTime = 0;
					if (deprecated.contains("-")) {
						deTime = DateUtils.string2Date(deprecated, DateUtils.PATTERN_DATE_TIME).getTime();
					}
					if (deTime == 0) {
						deTime = Long.valueOf(deprecated) * DateUtils.MILLIS_PER_MINUTE;
						item.setDeprecatedTime(deTime + System.currentTimeMillis());
					} else {
						item.setDeprecatedTime(deTime);
					}
					item.checkDeprecated();
					// 会过期的道具，默认就绑定
					item.openState(ItemState.BIND.getMark());
				}

				String bind = rewardItem.getParms().get(ItemState.BIND.name());
				if (bind != null && bind.equals("1")) {// 锻造的默认是绑定的
					item.openState(ItemState.BIND.getMark());
				}
			}
			LogManager.addItemLog(player, System.currentTimeMillis(), module, 1, item.getSize(), item,
					player.getTotalItemSizeByKey(item.getKey()));
		}

		if (isToTreasurePack) {
			for (AbstractItem item : items) {
				player.getTreasureWareHouse().addCollectRecord(item);
			}
			player.getTreasureWareHouse().addItems(false, items);
			// PacketSendUtility.sendPacket(player,
			// SM_Treasure_Storage_Update.valueOf(player));
		} else {
			player.getPack().addItems(items);
			PacketSendUtility.sendPacket(player, SM_Packet_Update.valueOf(player));
			EventBusManager.getInstance().submit(CollectItemsEvent.valueOf(player, Arrays.asList(items)));
		}

		playerManager.updatePlayer(player);
	}
}
