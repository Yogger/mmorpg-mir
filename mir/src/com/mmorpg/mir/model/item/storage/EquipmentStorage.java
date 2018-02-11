package com.mmorpg.mir.model.item.storage;

import java.util.BitSet;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectId;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectType;
import com.mmorpg.mir.model.item.AbstractItem;
import com.mmorpg.mir.model.item.Equipment;
import com.mmorpg.mir.model.item.ItemType;
import com.mmorpg.mir.model.item.NullItem;
import com.mmorpg.mir.model.item.PetItem;
import com.mmorpg.mir.model.item.config.ItemConfig;
import com.mmorpg.mir.model.item.core.ItemManager;
import com.mmorpg.mir.model.item.resource.PetResource;
import com.windforce.common.utility.DateUtils;

public class EquipmentStorage {

	public static final int DEFAULT_SIZE = EquipmentType.values().length - 4;

	public static final int HORSE_SIZE = 4;

	private Equipment[] equipments;
	private int smeltLevel;
	private int smeltValue;
	private PetItem petItem;

	/** 当天熔炼获得的经验值 (这里包括了玩家装备和坐骑装备，以后所有的装备熔炼值都放在这里了（2016-3-30）) */
	private transient long smeltExp;
	/** 最近一次熔炼 */
	private transient long lastSmeltRefreshTime;
	/** 装备强化的最小次数 */
	private transient Map<String, Integer> enhanceCount;

	@JsonIgnore
	private transient BitSet mark;

	/** 全身强化历史最大等级 **/
	private transient int starHistoryLevel;

	// beta19 强化等级提升
	private transient boolean resetEnhanceLevel;

	/** 全身强化等级 **/
	@JsonIgnore
	private transient int suitStarLevel;

	public void initEquipmentStat(Player player) {
		for (Equipment equip : equipments) { // 装备
			if (equip != null) {
				player.getGameStats().addModifiers(StatEffectId.valueOf(equip.getObjectId(), StatEffectType.EQUIPMENT),
						equip.getModifiers(), false);
				equip.addEquipmentExtraStats(player, false);
			}
		}
		initPetStats(player, false);
		ItemManager.getInstance().calOnAddSuitStat(player, false);
		/*
		 * player.getGameStats().addModifiers(Equipment.SMELT_STAT,
		 * ItemManager.getInstance
		 * ().getEquipmentSmeltResource(smeltLevel).getAllStats(), false);
		 */
		refresh();
	}

	@JsonIgnore
	public void initStat(Player player) {
		for (Equipment equip : equipments) { // 装备
			if (equip != null) {
				player.getGameStats().addModifiers(StatEffectId.valueOf(equip.getObjectId(), StatEffectType.EQUIPMENT),
						equip.getModifiers(), false);
				equip.addEquipmentExtraStats(player, false);
			}
		}
		refresh();
	}

	@JsonIgnore
	public void refresh() {
		// refresh the count
		if (!DateUtils.isToday(new Date(lastSmeltRefreshTime))) {
			lastSmeltRefreshTime = System.currentTimeMillis();
			smeltExp = 0;
		}
	}

	public static EquipmentStorage valueOf() {
		return new EquipmentStorage();
	}

	public static EquipmentStorage valueOfHorse() {
		return new EquipmentStorage(HORSE_SIZE);
	}

	public EquipmentStorage() {
		this(DEFAULT_SIZE);
	}

	public EquipmentStorage(int size) {
		equipments = new Equipment[size];
		mark = new BitSet(size);
		enhanceCount = new HashMap<String, Integer>();
	}

	@JsonIgnore
	public Equipment getByGuid(long guid) {
		for (Equipment equipment : equipments) {
			if (equipment != null && equipment.getObjectId().longValue() == guid) {
				return equipment;
			}
		}
		return null;
	}

	public Equipment[] getEquipments() {
		return equipments;
	}

	public void setEquipments(Equipment[] equipments) {
		this.equipments = equipments;
	}

	public Equipment getEquip(int ordinal) {
		if (ordinal < 0 || ordinal >= equipments.length)
			return null;
		return equipments[ordinal];
	}

	public Equipment getEquip(EquipmentType type) {
		return equipments[type.getStorageIndex()];
	}

	public boolean hasEquip(EquipmentType type) {
		return equipments[type.getStorageIndex()] != null;
	}

	@JsonIgnore
	public BitSet getMark() {
		return mark;
	}

	public void setMark(BitSet mark) {
		this.mark = mark;
	}

	public Equipment equip(Equipment equipment) {
		int index = equipment.getEquipmentType().getStorageIndex();
		Equipment result = equipments[index];
		equipments[index] = equipment;
		mark.set(index);
		return result;
	}

	public Equipment unEquip(EquipmentType type) {
		int index = type.getStorageIndex();
		Equipment result = equipments[index];
		equipments[index] = null;
		if (result != null) {
			mark.set(index);
		}
		return result;
	}

	public Map<Integer, Object> collectUpdate() {
		HashMap<Integer, Object> result = new LinkedHashMap<Integer, Object>();
		for (int i = 0, j = mark.length(); i < j; i++) {
			if (mark.get(i)) {
				if (equipments[i] == null) {
					result.put(i, NullItem.getInstance());
				} else {
					result.put(i, equipments[i]);
				}
			}
			mark.set(i, false);
		}
		return result;
	}

	public void markByEquipmentType(EquipmentType type) {
		int index = type.getStorageIndex();
		mark.set(index);
	}

	public int getSmeltLevel() {
		return smeltLevel;
	}

	public void setSmeltLevel(int smeltLevel) {
		this.smeltLevel = smeltLevel;
	}

	public int getSmeltValue() {
		return smeltValue;
	}

	public void setSmeltValue(int smeltValue) {
		this.smeltValue = smeltValue;
	}

	@JsonIgnore
	public long addSmeltExp(long addCount) {
		this.smeltExp = smeltExp + addCount;
		return smeltExp;
	}

	public long getSmeltExp() {
		return smeltExp;
	}

	public void setSmeltExp(long smeltExp) {
		this.smeltExp = smeltExp;
	}

	public long getLastSmeltRefreshTime() {
		return lastSmeltRefreshTime;
	}

	public void setLastSmeltRefreshTime(long lastSmeltRefreshTime) {
		this.lastSmeltRefreshTime = lastSmeltRefreshTime;
	}

	public Map<String, Integer> getEnhanceCount() {
		return enhanceCount;
	}

	public void setEnhanceCount(Map<String, Integer> enhanceCount) {
		this.enhanceCount = enhanceCount;
	}

	@JsonIgnore
	public int getSuitStarLevel() {
		return suitStarLevel;
	}

	@JsonIgnore
	public void setSuitStarLevel(int suitStarLevel) {
		this.suitStarLevel = suitStarLevel;
	}

	@JsonIgnore
	public boolean updateSuitStarLevel(int suitStar) {
		if (suitStarLevel != suitStar) {
			suitStarLevel = suitStar;
			if (starHistoryLevel < suitStar) {
				starHistoryLevel = suitStar;
				return true;
			}
		}
		return false;
	}

	public int getStarHistoryLevel() {
		return starHistoryLevel;
	}

	public void setStarHistoryLevel(int starHistoryLevel) {
		this.starHistoryLevel = starHistoryLevel;
	}

	public boolean isResetEnhanceLevel() {
		return resetEnhanceLevel;
	}

	public void setResetEnhanceLevel(boolean resetEnhanceLevel) {
		this.resetEnhanceLevel = resetEnhanceLevel;
	}

	public PetItem getPetItem() {
		return petItem;
	}

	public void setPetItem(PetItem petItem) {
		this.petItem = petItem;
	}

	@JsonIgnore
	public synchronized void changePetItem(PetItem expect, PetItem update) {
		if (petItem == null && expect == null) {
			setPetItem(update);
		} else if (petItem != null && expect != null && petItem.getKey().equals(expect.getKey())) {
			setPetItem(update);
		}
	}

	public static enum EquipmentType {
		/** 武器 **/
		WEAPON(0, 0),
		/** 项链 **/
		NECKLACE(1, 1),
		/** 玉佩 **/
		JADE(2, 2),
		/** 左戒指 **/
		LEFTRING(3, 3),
		/** 右戒指 **/
		RIGHTRING(4, 4),
		/** 头盔 **/
		HAT(5, 5),
		/** 衣服 **/
		CLOTHES(6, 6),
		/** 腰带 **/
		BELT(7, 7),
		/** 裤子 **/
		PANTS(8, 8),
		/** 鞋子 **/
		SHOES(9, 9),
		/** 左护腕 **/
		LEFTCUFF(10, 10),
		/** 右护腕 **/
		RIGHTCUFF(11, 11),
		/** 马鞍 */
		SADDLE(13, 0),
		/** 马镫 */
		STIRRUP(14, 1),
		/** 蹄铁 */
		HORSESHOE(15, 2),
		/** 缰绳 */
		REIN(16, 3);

		public static EquipmentType valueOf(int v) {
			for (EquipmentType type : values()) {
				if (type.getIndex() == v) {
					return type;
				}
			}
			throw new RuntimeException(" no match type of EquipmentType[" + v + "]");
		}

		private int index;
		/** 在装备栏的位置 */
		private int storageIndex;

		private EquipmentType(int index, int storageIndex) {
			this.index = index;
			this.storageIndex = storageIndex;
		}

		public int getIndex() {
			return this.index;
		}

		public int getStorageIndex() {
			return storageIndex;
		}

		public void setStorageIndex(int storageIndex) {
			this.storageIndex = storageIndex;
		}

	}

	@JsonIgnore
	public synchronized PetItem callPet(AbstractItem petItem) {
		PetItem before = this.petItem;
		setPetItem((PetItem) petItem);
		return before;
	}

	@JsonIgnore
	public void startPetTask(Player owner) {
		if (petItem != null) {
			petItem.startDeprecatedFuture(owner, false);
		}
	}

	@JsonIgnore
	public void stopPetTask(Player owner) {
		if (petItem != null) {
			petItem.stopDeprecatedFuture();
		}
	}

	public boolean hasPet() {
		return petItem != null && !petItem.isDeprecated();
	}

	@JsonIgnore
	public PetResource getPetResource() {
		if (hasPet()) {
			return ItemConfig.getInstance().petResources.get(petItem.getKey(), true);
		}
		return null;
	}

	public void initPetStats(Player owner, boolean recompute) {
		if (hasPet()) {
			owner.getGameStats().addModifiers(
					StatEffectId.valueOf(ItemType.PETITEM.name(), StatEffectType.ITEM_EFFECT),
					getPetResource().getStats(), recompute);
		}
	}

}
