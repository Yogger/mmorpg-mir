package com.mmorpg.mir.model.item.core;

import java.util.List;
import java.util.Map;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.mmorpg.mir.model.item.AbstractItem;
import com.mmorpg.mir.model.item.Equipment;
import com.mmorpg.mir.model.item.model.EquipmentStat;
import com.mmorpg.mir.model.item.model.EquipmentStorageType;
import com.mmorpg.mir.model.item.model.extrastat.EquipmentExtraStatHandle;
import com.mmorpg.mir.model.item.resource.CombiningResource;
import com.mmorpg.mir.model.item.resource.EquipmentComposeResource;
import com.mmorpg.mir.model.item.resource.EquipmentEnhanceResource;
import com.mmorpg.mir.model.item.resource.EquipmentRareResource;
import com.mmorpg.mir.model.item.resource.EquipmentSmeltResource;
import com.mmorpg.mir.model.item.resource.EquipmentSoulResource;
import com.mmorpg.mir.model.item.resource.ItemResource;
import com.mmorpg.mir.model.item.storage.ItemStorage;
import com.mmorpg.mir.model.item.storage.EquipmentStorage.EquipmentType;

public interface IItemManager {
	Map<EquipmentType, EquipmentType> getActivateMap();

	String getEquipmentRareChooserGroup(String quality);

	Map<EquipmentType, EquipmentType> getEffectActivateMap();

	void reload();

	ItemResource getResource(String key);

	EquipmentEnhanceResource getEquipEnhanceResource(Integer level);

	EquipmentComposeResource getEquipComposeResource(String key);

	public EquipmentRareResource getEquipRaraResource(String key);

	public EquipmentSoulResource getEquipmentSoulReousrce(String key);

	public EquipmentSmeltResource getEquipmentSmeltResource(Integer key);

	public String getItemResourceKeyByHash(int roleType, int equipmentType, int level, int quality, int specialType);

	public double getSmeltUseGoldRate();

	public int smeltConsumeGift(int count);

	public int smeltConsumeGold(int count);

	public int addSmeltValueByGift(int consumeValue, int level);

	public int addSmeltValueByGold(int consumeValue, int level);

	public int getItemLength(String key, int num);

	public <T extends AbstractItem> T[] createItems(String key, int num);

	public <T extends AbstractItem> T createItem(String key);

	public int getInitSize(byte type);

	public int getMaxSize(byte type);

	public long getReaminTime(ItemStorage storage, byte type);

	public int getAddPackPrice(int size, byte type);

	public int getAddPackExp(int index, byte type);

	public Stat replaceAddPackStat(int index, byte type);

	public int calcSmeltGoldActionValue(int n);

	public int calcSmeltGiftActionValue(int n);

	public int getEquipmentValue(int quality, int enhanceLevel);

	public void registerExtralStatHandle(EquipmentExtraStatHandle equipmentExtalStatHandle);

	public List<Stat> calculateStat(Player player, Equipment equip, EquipmentStat equipStat);

	public int calGoldConsumption(int totalAddValue);

	public Integer[] getDefenseEquipGroup();

	public Integer[] getAttackEquipGroup();

	public Integer[] getRoleEquipGroup();

	public void calOnAddSuitStat(Player player, boolean recompute);

	public void calculateSoulSuit(Player player, boolean recompute);

	public void calculateStarSuit(Player player, boolean recompute, EquipmentStorageType equipStorgeType);

	public void soulActivateChange(Player player, EquipmentType equipType, EquipmentStorageType equipStorageType);

	public Double getEnhanceRatio(int index);

	public void soulSelfActivateChange(Player player, EquipmentType equipType, EquipmentStorageType equipStorageType);

	public Class<?> getResourceClass();

	public CombiningResource getCombiningResource(String id);
}
