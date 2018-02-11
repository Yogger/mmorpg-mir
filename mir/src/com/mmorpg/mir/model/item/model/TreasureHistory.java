package com.mmorpg.mir.model.item.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.chat.model.show.object.ItemShow;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.item.AbstractItem;
import com.mmorpg.mir.model.item.Equipment;
import com.mmorpg.mir.model.item.config.ItemConfig;
import com.mmorpg.mir.model.item.core.ItemManager;
import com.mmorpg.mir.model.item.entity.TreasureHistoryEntity;
import com.mmorpg.mir.model.item.resource.ItemResource;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.reward.model.RewardItem;
import com.mmorpg.mir.model.reward.model.RewardType;
import com.windforce.common.utility.New;

public class TreasureHistory {
	/** 全服玩家记录 */
	private LinkedList<PlayerTreasureInfo> playerTreasureHistory;

	@Transient
	private transient TreasureHistoryEntity treasureHistoryEntity;

	public static TreasureHistory valueOf() {
		TreasureHistory result = new TreasureHistory();
		result.playerTreasureHistory = new LinkedList<PlayerTreasureInfo>();
		return result;
	}

	@JsonIgnore
	public void addPlayerHistory(Player player, Reward reward) {
		for (RewardItem rewardItem : reward.getItemsByType(RewardType.ITEM)) {
			ItemResource resource = ItemManager.getInstance().itemResources.get(rewardItem.getCode(), true);
			int limitQuality = ItemManager.getInstance().TREASURE_STORAGE_SERVER_COLLECT_QULITY_LIMIT.getValue();
			if (resource.getQuality() >= limitQuality) {
				AbstractItem[] items = ItemManager.getInstance().createItems(rewardItem.getCode(),
						rewardItem.getAmount());
				for (AbstractItem item : items) {
					
					if (item instanceof Equipment) {
						Equipment equip = (Equipment) item;
						if (rewardItem.getParms() == null)
							break;
						for (Map.Entry<String, String> entry : rewardItem.getParms().entrySet()) {
							if (entry.getKey().equals(EquipmentStatType.ELEMENT_STAT.name())) {
								equip.setElement(Integer.valueOf(entry.getValue()));
							}
							if (entry.getKey().equals(EquipmentStatType.SOUL_STAT.name())) {
								equip.addExtraStats(EquipmentStatType.SOUL_STAT.getValue(), entry.getValue().split("[|]"));
							}
							if (!ItemConfig.getInstance().DELETE_EQUIPMENT_PERFECT_STAT.getValue()) {
								if (entry.getKey().equals(EquipmentStatType.PERFECT_STAT.name())) {
									equip.addExtraStats(EquipmentStatType.PERFECT_STAT.getValue(), entry.getValue()
											.split("[|]"));
								}
							}
							if (entry.getKey().equals("ENHANCE_LEVEL")) {
								equip.setEnhanceLevel(Integer.valueOf(entry.getValue()));
							}
						}
					}
					
					PlayerTreasureInfo info = PlayerTreasureInfo.valueOf(player.getName(), item);
					playerTreasureHistory.addFirst(info);

					ItemShow show = new ItemShow();
					show.setOwner(player.getName());
					show.setKey(rewardItem.getCode());
					show.setItem(item);

					I18nUtils i18nUtils = I18nUtils.valueOf("20107");
					i18nUtils.addParm("name", I18nPack.valueOf(player.getName()));
					i18nUtils.addParm("country", I18nPack.valueOf(player.getCountry().getName()));
					i18nUtils.addParm("item", I18nPack.valueOf(show));
					ChatManager.getInstance().sendSystem(11001, i18nUtils, null);

					I18nUtils i18nUtils2 = I18nUtils.valueOf("309101");
					i18nUtils2.addParm("name", I18nPack.valueOf(player.getName()));
					i18nUtils2.addParm("country", I18nPack.valueOf(player.getCountry().getName()));
					i18nUtils2.addParm("item", I18nPack.valueOf(show));
					ChatManager.getInstance().sendSystem(0, i18nUtils2, null);
				}
			}
		}
		treasureHistoryEntity.update();
	}

	@JsonIgnore
	public ArrayList<PlayerTreasureInfo> getPlayerHistory() {
		// 个人探宝记录数
		int count = ItemManager.getInstance().TREASURE_STORAGE_SERVER_COLLECT_RECORD_COUNT.getValue();
		ArrayList<PlayerTreasureInfo> tempHistory = New.arrayList(playerTreasureHistory);
		int size = playerTreasureHistory.size();
		if (playerTreasureHistory.size() <= count) {
			return tempHistory;
		} else {
			ArrayList<PlayerTreasureInfo> list = new ArrayList<PlayerTreasureInfo>(playerTreasureHistory.subList(0,
					count));
			if (size > 2 * count) {
				for (PlayerTreasureInfo info : tempHistory) {
					if (!list.contains(info)) {
						playerTreasureHistory.remove(info);
					}
				}
				treasureHistoryEntity.update();
			}
			return list;
		}
	}

	public LinkedList<PlayerTreasureInfo> getPlayerTreasureHistory() {
		return playerTreasureHistory;
	}

	public void setPlayerTreasureHistory(LinkedList<PlayerTreasureInfo> playerTreasureHistory) {
		this.playerTreasureHistory = playerTreasureHistory;
	}

	@JsonIgnore
	public TreasureHistoryEntity getTreasureHistoryEntity() {
		return treasureHistoryEntity;
	}

	@JsonIgnore
	public void setTreasureHistoryEntity(TreasureHistoryEntity treasureHistoryEntity) {
		this.treasureHistoryEntity = treasureHistoryEntity;
	}

}
