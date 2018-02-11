package com.mmorpg.mir.model.lifegrid.reward;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.log.LogManager;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.item.AbstractItem;
import com.mmorpg.mir.model.item.LifeGridItem;
import com.mmorpg.mir.model.item.core.ItemManager;
import com.mmorpg.mir.model.item.resource.ItemResource;
import com.mmorpg.mir.model.lifegrid.model.LifeGridStorage;
import com.mmorpg.mir.model.lifegrid.model.LifeStorageType;
import com.mmorpg.mir.model.lifegrid.packet.SM_LifeGrid_Storage_Update;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.reward.model.RewardItem;
import com.mmorpg.mir.model.reward.model.RewardProvider;
import com.mmorpg.mir.model.reward.model.RewardType;
import com.mmorpg.mir.model.utils.PacketSendUtility;

@Component
public class LifeGridItemRewardsProvider extends RewardProvider {

	@Autowired
	private ItemManager itemManager;

	@Override
	public RewardType getType() {
		return RewardType.LIFEGRID;
	}

	@Override
	public void withdraw(Player player, RewardItem rewardItem, ModuleInfo module) {
		AbstractItem[] items = itemManager.createItems(rewardItem.getCode(), rewardItem.getAmount());
		int toLifeGridStorageType = -1;
		LifeStorageType storageType = null;
		if (rewardItem.getParms() != null && rewardItem.getParms().containsKey("LIFEGRID_STORAGE_TYPE")) {
			toLifeGridStorageType = Integer.parseInt(rewardItem.getParms().get("LIFEGRID_STORAGE_TYPE"));
			storageType = LifeStorageType.typeOf(toLifeGridStorageType);
		}

		if (storageType != null) {
			List<LifeGridItem> lifeGridItems = new ArrayList<LifeGridItem>();
			for (AbstractItem i : items) {
				LifeGridItem lifeItem = (LifeGridItem) i;
				if (lifeItem.getResource().getLifeGridLevel() != 1 && !lifeItem.isSpecialOne()) {
					ItemResource lastResource = ItemManager.getInstance().getLastLevelLifeGridResource(lifeItem);
					lifeItem.setExp(lastResource.getNeedExp());
				}
				lifeGridItems.add((LifeGridItem) i);
				LogManager.addLifeGridItemLog(player, System.currentTimeMillis(), module, 1, storageType.getType(),
						lifeItem.getSize(), lifeItem, player.getLifeGridPool()
								.getTotalItemCountByKey(lifeItem.getKey()));
			}
			LifeGridStorage storage = null;
			if (storageType == LifeStorageType.PACK) {
				storage = player.getLifeGridPool().getPackStorage();
			} else if (storageType == LifeStorageType.HOUSE) {
				storage = player.getLifeGridPool().getHouseStorage();
			}
			storage.addItems(false, lifeGridItems);

			PacketSendUtility.sendPacket(player,
					SM_LifeGrid_Storage_Update.valueOf(toLifeGridStorageType, storage.collectUpdate()));
		}
	}

}
