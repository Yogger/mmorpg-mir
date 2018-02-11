package com.mmorpg.mir.model.lifegrid.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.log.LogManager;
import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectId;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectType;
import com.mmorpg.mir.model.item.AbstractItem;
import com.mmorpg.mir.model.item.LifeGridItem;
import com.mmorpg.mir.model.item.core.ItemManager;
import com.mmorpg.mir.model.item.resource.ItemResource;
import com.mmorpg.mir.model.lifegrid.LifeGridConfig;
import com.mmorpg.mir.model.lifegrid.model.LifeGridPool;
import com.mmorpg.mir.model.lifegrid.model.LifeGridStorage;
import com.mmorpg.mir.model.lifegrid.model.LifeStorageType;
import com.mmorpg.mir.model.lifegrid.model.LifeGridStorage.QueryDevourResult;
import com.mmorpg.mir.model.lifegrid.packet.SM_LifeGrid_AddEquipStorageSize;
import com.mmorpg.mir.model.lifegrid.packet.SM_LifeGrid_Convert;
import com.mmorpg.mir.model.lifegrid.packet.SM_LifeGrid_Devour;
import com.mmorpg.mir.model.lifegrid.packet.SM_LifeGrid_DevourAll;
import com.mmorpg.mir.model.lifegrid.packet.SM_LifeGrid_Drop;
import com.mmorpg.mir.model.lifegrid.packet.SM_LifeGrid_Equip;
import com.mmorpg.mir.model.lifegrid.packet.SM_LifeGrid_ExchangePack;
import com.mmorpg.mir.model.lifegrid.packet.SM_LifeGrid_OperateLock;
import com.mmorpg.mir.model.lifegrid.packet.SM_LifeGrid_Take;
import com.mmorpg.mir.model.lifegrid.packet.SM_LifeGrid_Take_All;
import com.mmorpg.mir.model.lifegrid.packet.SM_LifeGrid_UnEquip;
import com.mmorpg.mir.model.lifegrid.resource.LifeGridConvertResource;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.moduleopen.manager.ModuleOpenManager;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.utils.PacketSendUtility;

@Component
public class LifeGridManager {

	@Autowired
	private ModuleOpenManager moduleOpenManager;

	@Autowired
	private RewardManager rewardManager;

	@Autowired
	private LifeGridConfig config;

	private LifeGridItem checkIsLifeGridItem(AbstractItem item) {
		if (item == null || (!(item instanceof LifeGridItem))) {
			throw new ManagedException(ManagedErrorCode.NOT_LIFEGRID_ITEM);
		}
		return (LifeGridItem) item;
	}

	public SM_LifeGrid_Take takeFromLifeGridStorage(Player player, int index) {
		if (player == null) {
			throw new ManagedException(ManagedErrorCode.NO_ROLE);
		}

		if (!moduleOpenManager.isOpenByModuleKey(player, ModuleKey.LIFEGRID)) {
			throw new ManagedException(ManagedErrorCode.MODULE_NOT_OPEN);
		}
		LifeGridStorage lifeHouseStorage = player.getLifeGridPool().getHouseStorage();
		LifeGridStorage lifePackStorage = player.getLifeGridPool().getPackStorage();

		AbstractItem item = lifeHouseStorage.getItemByIndex(index);
		if (null == item) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}

		if (lifePackStorage.isFull()) {
			throw new ManagedException(ManagedErrorCode.LIFE_GRID_PACK_NOT_ENOUGH_SIZE);
		}
		checkIsLifeGridItem(item);

		exchange(player, lifeHouseStorage, lifePackStorage, index);

		SM_LifeGrid_Take result = SM_LifeGrid_Take.valueOf(lifePackStorage.collectUpdate(),
				lifeHouseStorage.collectUpdate());
		return result;
	}

	public SM_LifeGrid_Take_All takeAllFromLifeGridStorage(Player player, Set<Integer> indexs) {
		if (player == null) {
			throw new ManagedException(ManagedErrorCode.NO_ROLE);
		}

		if (!moduleOpenManager.isOpenByModuleKey(player, ModuleKey.LIFEGRID)) {
			throw new ManagedException(ManagedErrorCode.MODULE_NOT_OPEN);
		}

		LifeGridStorage lifeHouseStorage = player.getLifeGridPool().getHouseStorage();
		LifeGridStorage lifePackStorage = player.getLifeGridPool().getPackStorage();

		if (lifePackStorage.isFull()) {
			throw new ManagedException(ManagedErrorCode.LIFE_GRID_PACK_NOT_ENOUGH_SIZE);
		}

		Set<Integer> packIndexs = new TreeSet<Integer>();
		int emptySize = lifePackStorage.getEmptySize();

		int i = 0;
		for (Integer v : indexs) {
			i++;
			if (i > emptySize) {
				break;
			}
			packIndexs.add(v);

		}

		exchange(player, lifeHouseStorage, lifePackStorage, packIndexs);

		SM_LifeGrid_Take_All result = SM_LifeGrid_Take_All.valueOf(lifePackStorage.collectUpdate(),
				lifeHouseStorage.collectUpdate());
		return result;
	}

	private void exchange(Player player, LifeGridStorage from, LifeGridStorage to, Set<Integer> indexs) {
		for (int index : indexs) {
			exchange(player, from, to, index);
		}
	}

	private void exchange(Player player, LifeGridStorage from, LifeGridStorage to, int index) {
		LifeGridItem item = from.removeItemByIndex(index);
		if (item != null) {
			to.addItems(false, item);
			LogManager.addLifeGridItemLog(player, System.currentTimeMillis(), ModuleInfo.valueOf(ModuleType.LIFEGRID,
					SubModuleType.LIFEGRID_TAKE), -1, LifeStorageType.HOUSE.getType(), item.getSize(), item, player
					.getLifeGridPool().getTotalItemCountByKey(item.getKey()));

			LogManager.addLifeGridItemLog(player, System.currentTimeMillis(), ModuleInfo.valueOf(ModuleType.LIFEGRID,
					SubModuleType.LIFEGRID_TAKE), 1, LifeStorageType.PACK.getType(), item.getSize(), item, player
					.getLifeGridPool().getTotalItemCountByKey(item.getKey()));
		}

	}

	public SM_LifeGrid_OperateLock operateLock(Player player, LifeStorageType type, int index) {
		if (!moduleOpenManager.isOpenByModuleKey(player, ModuleKey.LIFEGRID)) {
			throw new ManagedException(ManagedErrorCode.MODULE_NOT_OPEN);
		}

		SM_LifeGrid_OperateLock result = player.getLifeGridPool().operateLock(type, index);
		return result;
	}

	/**
	 * 交换命格位置
	 * 
	 * @param player
	 * @param fromType
	 * @param toType
	 * @param fromIndex
	 * @param toIndex
	 * @return
	 */
	public SM_LifeGrid_ExchangePack exchangePack(Player player, LifeStorageType fromType, LifeStorageType toType,
			int fromIndex, int toIndex) {
		if (!moduleOpenManager.isOpenByModuleKey(player, ModuleKey.LIFEGRID)) {
			throw new ManagedException(ManagedErrorCode.MODULE_NOT_OPEN);
		}
		SM_LifeGrid_ExchangePack result = null;
		LifeGridPool pool = player.getLifeGridPool();
		if (fromType == toType) {
			LifeGridStorage storage = pool.getStorageByType(fromType);
			if (storage.getItemByIndex(toIndex) != null) {
				throw new ManagedException(ManagedErrorCode.LIFE_GRID_TARGET_POSITION_HAS_ITEM);
			}
			if (storage.getItemByIndex(fromIndex) == null) {
				throw new ManagedException(ManagedErrorCode.ERROR_MSG);
			}

			storage.exchage(fromIndex, toIndex);
			result = SM_LifeGrid_ExchangePack.valueOfSameType(fromType, toType, storage.collectUpdate());
		} else {
			if (fromType == LifeStorageType.EQUIP && toType != LifeStorageType.PACK) {
				throw new ManagedException(ManagedErrorCode.ERROR_MSG);
			}
			LifeGridStorage fromStorage = pool.getStorageByType(fromType);
			LifeGridStorage toStorage = pool.getStorageByType(toType);

			if (fromStorage.getItemByIndex(fromIndex) == null) {
				throw new ManagedException(ManagedErrorCode.ERROR_MSG);
			}
			if (toStorage.getItemByIndex(toIndex) != null) {
				throw new ManagedException(ManagedErrorCode.LIFE_GRID_TARGET_POSITION_HAS_ITEM);
			}

			LifeGridItem item = fromStorage.getItemByIndex(fromIndex);
			if (fromType != LifeStorageType.EQUIP && toType == LifeStorageType.EQUIP) {
				if (item.isSpecialOne()) {
					throw new ManagedException(ManagedErrorCode.LIFE_GRID_SPECIAL_ITEM_CAN_NOT_EQUIP);
				}
				if (toStorage.checkIsExistSameType(item.getLifeGridType())) {
					throw new ManagedException(ManagedErrorCode.LIFE_GRID_EQUIP_SAME_TYPE_ITEM);
				}
				player.getGameStats().addModifiers(StatEffectId.valueOf(item.getObjectId(), StatEffectType.EQUIPMENT),
						item.getResource().getStats());

			} else if (fromType == LifeStorageType.EQUIP && toType != LifeStorageType.EQUIP) {
				player.getGameStats().endModifiers(StatEffectId.valueOf(item.getObjectId(), StatEffectType.EQUIPMENT),
						true);
			}
			item = fromStorage.removeItemByIndex(fromIndex);
			toStorage.addItemByIndex(toIndex, item);

			if(item!=null){
				LogManager.addLifeGridItemLog(player, System.currentTimeMillis(),
						ModuleInfo.valueOf(ModuleType.LIFEGRID, SubModuleType.LIFEGRID_EXCHANGE), -1, fromType.getType(),
						item.getSize(), item, player.getLifeGridPool().getTotalItemCountByKey(item.getKey()));
				
				LogManager.addLifeGridItemLog(player, System.currentTimeMillis(),
						ModuleInfo.valueOf(ModuleType.LIFEGRID, SubModuleType.LIFEGRID_EXCHANGE), 1, toType.getType(),
						item.getSize(), item, player.getLifeGridPool().getTotalItemCountByKey(item.getKey()));
			}
			result = SM_LifeGrid_ExchangePack.valueOf(fromType, toType, fromStorage.collectUpdate(),
					toStorage.collectUpdate());
		}
		return result;
	}

	public void addEquipPackSize(Player player) {
		LifeGridPool pool = player.getLifeGridPool();
		int extendSize = config.getExtendSize(player.getLevel(), pool.getEquipStorage().getLastOpenLevel());
		if (extendSize == 0) {
			return;
		}

		pool.getEquipStorage().extendSize(player.getLevel(), extendSize);

		PacketSendUtility.sendPacket(player,
				SM_LifeGrid_AddEquipStorageSize.valueOf(pool.getEquipStorage().getTotalSize()));
	}

	/***
	 * 装备命格
	 * 
	 * @param player
	 * @param index
	 * @return
	 */
	public SM_LifeGrid_Equip equip(Player player, int index) {
		if (!moduleOpenManager.isOpenByModuleKey(player, ModuleKey.LIFEGRID)) {
			throw new ManagedException(ManagedErrorCode.MODULE_NOT_OPEN);
		}

		LifeGridPool pool = player.getLifeGridPool();

		LifeGridStorage lifePackStorage = pool.getPackStorage();
		LifeGridItem item = lifePackStorage.getItemByIndex(index);
		checkIsLifeGridItem(item);

		LifeGridStorage lifeEquipStorage = pool.getEquipStorage();

		if (lifeEquipStorage.checkIsExistSameType(item.getLifeGridType())) {
			throw new ManagedException(ManagedErrorCode.LIFE_GRID_EQUIP_SAME_TYPE_ITEM);
		}

		if (item.isSpecialOne()) {
			throw new ManagedException(ManagedErrorCode.LIFE_GRID_SPECIAL_ITEM_CAN_NOT_EQUIP);
		}

		if (lifeEquipStorage.isFull()) {
			throw new ManagedException(ManagedErrorCode.LIFE_GRID_EQUIPPACK_NOT_ENOUGH_SIZE);
		}

		lifeEquipStorage.equip(item);
		lifePackStorage.removeItemByIndex(index);

		LogManager.addLifeGridItemLog(player, System.currentTimeMillis(),
				ModuleInfo.valueOf(ModuleType.LIFEGRID, SubModuleType.LIFEGRID_EQUIP), -1,
				LifeStorageType.PACK.getType(), item.getSize(), item,
				player.getLifeGridPool().getTotalItemCountByKey(item.getKey()));

		LogManager.addLifeGridItemLog(player, System.currentTimeMillis(),
				ModuleInfo.valueOf(ModuleType.LIFEGRID, SubModuleType.LIFEGRID_EQUIP), 1,
				LifeStorageType.EQUIP.getType(), item.getSize(), item,
				player.getLifeGridPool().getTotalItemCountByKey(item.getKey()));

		ItemResource resource = item.getResource();
		player.getGameStats().addModifiers(StatEffectId.valueOf(item.getObjectId(), StatEffectType.EQUIPMENT),
				resource.getStats(), true);
		SM_LifeGrid_Equip result = SM_LifeGrid_Equip.valueOf(pool.getPackStorage().collectUpdate(), pool
				.getEquipStorage().collectUpdate());
		return result;

	}

	/**
	 * 脱下命格
	 * 
	 * @param player
	 * @param index
	 * @return
	 */
	public SM_LifeGrid_UnEquip upEquip(Player player, int index) {
		if (!moduleOpenManager.isOpenByModuleKey(player, ModuleKey.LIFEGRID)) {
			throw new ManagedException(ManagedErrorCode.MODULE_NOT_OPEN);
		}

		LifeGridPool pool = player.getLifeGridPool();
		LifeGridStorage lifeEquipStorage = pool.getEquipStorage();

		if (lifeEquipStorage.getItemByIndex(index) == null) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}

		LifeGridStorage lifePackStorage = pool.getPackStorage();

		if (lifePackStorage.isFull()) {
			throw new ManagedException(ManagedErrorCode.LIFE_GRID_PACK_NOT_ENOUGH_SIZE);
		}

		LifeGridItem item = lifeEquipStorage.unEquip(index);
		lifePackStorage.addItems(item);

		LogManager.addLifeGridItemLog(player, System.currentTimeMillis(),
				ModuleInfo.valueOf(ModuleType.LIFEGRID, SubModuleType.LIFEGRID_UNEQUIP), -1,
				LifeStorageType.EQUIP.getType(), item.getSize(), item,
				player.getLifeGridPool().getTotalItemCountByKey(item.getKey()));

		LogManager.addLifeGridItemLog(player, System.currentTimeMillis(),
				ModuleInfo.valueOf(ModuleType.LIFEGRID, SubModuleType.LIFEGRID_UNEQUIP), 1,
				LifeStorageType.PACK.getType(), item.getSize(), item,
				player.getLifeGridPool().getTotalItemCountByKey(item.getKey()));

		player.getGameStats().endModifiers(StatEffectId.valueOf(item.getObjectId(), StatEffectType.EQUIPMENT), true);

		SM_LifeGrid_UnEquip result = SM_LifeGrid_UnEquip.valueOf(pool.getPackStorage().collectUpdate(), pool
				.getEquipStorage().collectUpdate());

		return result;
	}

	/**
	 * 吞噬
	 * 
	 * @param player
	 * @param fromStorageType
	 *            被吞噬命格来源
	 * @param toStorageType
	 *            吞噬命格来源
	 * @param fromIndex
	 * @param toIndex
	 * @return
	 */
	public SM_LifeGrid_Devour devour(Player player, LifeStorageType fromStorageType, LifeStorageType toStorageType,
			int fromIndex, int toIndex) {
		if (!moduleOpenManager.isOpenByModuleKey(player, ModuleKey.LIFEGRID)) {
			throw new ManagedException(ManagedErrorCode.MODULE_NOT_OPEN);
		}

		LifeGridPool pool = player.getLifeGridPool();
		LifeGridStorage fromStorage = pool.getStorageByType(fromStorageType);
		LifeGridStorage toStorage = pool.getStorageByType(toStorageType);

		if (fromStorage.getItemByIndex(fromIndex) == null) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}

		if (toStorage.getItemByIndex(toIndex) == null) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}

		LifeGridItem fromItem = fromStorage.getItemByIndex(fromIndex);
		if (fromItem == null) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		if (fromItem.isLock()) {
			throw new ManagedException(ManagedErrorCode.LIFE_GRID_ITEM_LOCK);
		}

		if (fromItem.isMaxLevel() && !fromItem.isSpecialOne()) {
			throw new ManagedException(ManagedErrorCode.LIFE_GRID_ITEM_MAX_LEVEL);
		}

		LifeGridItem toItem = toStorage.getItemByIndex(toIndex);
		if (toItem == null) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}

		if (toItem.isMaxLevel()) {
			throw new ManagedException(ManagedErrorCode.LIFE_GRID_ITEM_MAX_LEVEL);
		}

		if (toItem.isSpecialOne()) {
			throw new ManagedException(ManagedErrorCode.LIFE_GRID_SPECIAL_ITEM_CAN_NOT_BE_DEVOURED);
		}

		List<ItemResource> sameTypeResources = ItemManager.getInstance().getSameTypeLifeGridResource(
				toItem.getResource().getQuality(), toItem.getResource().getLifeGridType());

		int addExp = toItem.getExp() + fromItem.getExp() + fromItem.getResource().getBaseExp();
		int curLevel = toItem.getResource().getLifeGridLevel();
		int newLevel = curLevel;
		for (ItemResource resource : sameTypeResources) {
			if (resource.getLifeGridLevel() < curLevel) {
				continue;
			}
			if (resource.isLifeGridMaxLevel()) {
				break;
			}
			if (resource.getNeedExp() <= addExp) {
				newLevel += 1;
			}
		}
		ItemResource r = getTargetLevelResource(newLevel, sameTypeResources);

		if (fromStorageType == LifeStorageType.EQUIP) {
			// 正在装备
			LifeGridItem unEquipItem = fromStorage.unEquip(fromIndex);
			int actuallyAdd = unEquipItem.getResource().getBaseExp() + unEquipItem.getExp();
			LogManager.addLifeGridItemLog(
					player,
					System.currentTimeMillis(),
					ModuleInfo.valueOf(ModuleType.LIFEGRID, SubModuleType.DEVOUR_LIFEGRID_ITEM, r.getKey() + "#"
							+ actuallyAdd + "#" + addExp), -1, fromStorageType.getType(), unEquipItem.getSize(),
					unEquipItem, player.getLifeGridPool().getTotalItemCountByKey(unEquipItem.getKey()));
			player.getGameStats().endModifiers(
					StatEffectId.valueOf(unEquipItem.getObjectId(), StatEffectType.EQUIPMENT), false);

		}
		LifeGridItem fromRemoveItem = fromStorage.removeItemByIndex(fromIndex);

		int actualyAdd = fromItem.getResource().getBaseExp() + fromItem.getExp();
		if (fromRemoveItem != null) {
			LogManager.addLifeGridItemLog(
					player,
					System.currentTimeMillis(),
					ModuleInfo.valueOf(ModuleType.LIFEGRID, SubModuleType.DEVOUR_LIFEGRID_ITEM, r.getKey() + "#"
							+ actualyAdd + "#" + addExp), -1, fromStorageType.getType(), fromRemoveItem.getSize(),
					fromRemoveItem, player.getLifeGridPool().getTotalItemCountByKey(fromRemoveItem.getKey()));
		}

		if (toStorageType == LifeStorageType.EQUIP) {
			LifeGridItem toRemoveItem = toStorage.unEquip(toIndex);
			LogManager.addLifeGridItemLog(
					player,
					System.currentTimeMillis(),
					ModuleInfo.valueOf(ModuleType.LIFEGRID, SubModuleType.DEVOUR_LIFEGRID_ITEM, r.getKey() + "#"
							+ actualyAdd + "#" + addExp), -1, toStorageType.getType(), toRemoveItem.getSize(),
					toRemoveItem, player.getLifeGridPool().getTotalItemCountByKey(toRemoveItem.getKey()));
			player.getGameStats().endModifiers(StatEffectId.valueOf(toItem.getObjectId(), StatEffectType.EQUIPMENT),
					false);
		}
		LifeGridItem toRemoveItem = toStorage.removeItemByIndex(toIndex);
		if (toRemoveItem != null) {
			LogManager.addLifeGridItemLog(
					player,
					System.currentTimeMillis(),
					ModuleInfo.valueOf(ModuleType.LIFEGRID, SubModuleType.DEVOUR_LIFEGRID_ITEM, r.getKey() + "#"
							+ actualyAdd + "#" + addExp), -1, toStorageType.getType(), toRemoveItem.getSize(),
					toRemoveItem, player.getLifeGridPool().getTotalItemCountByKey(toRemoveItem.getKey()));
		}

		LifeGridItem newItem = ItemManager.getInstance().createItem(r.getKey());
		newItem.setExp(addExp);
		if (toItem.isLock()) {
			// 原来吞噬者是锁定的
			newItem.openLock();
		}

		toStorage.addItemByIndex(toIndex, newItem);
		LogManager.addLifeGridItemLog(player, System.currentTimeMillis(), ModuleInfo.valueOf(ModuleType.LIFEGRID,
				SubModuleType.DEVOUR_LIFEGRID_ITEM, r.getKey() + "#" + actualyAdd + "#" + addExp), 1, toStorageType
				.getType(), newItem.getSize(), newItem,
				player.getLifeGridPool().getTotalItemCountByKey(newItem.getKey()));

		if (toStorageType == LifeStorageType.EQUIP) {
			player.getGameStats().addModifiers(StatEffectId.valueOf(newItem.getObjectId(), StatEffectType.EQUIPMENT),
					r.getStats(), false);
		}
		player.getGameStats().recomputeStats();

		SM_LifeGrid_Devour result = null;
		if (fromStorageType == toStorageType) {
			result = SM_LifeGrid_Devour.valueOfSameSource(fromStorageType, toStorageType, fromStorage.collectUpdate());
		} else {
			result = SM_LifeGrid_Devour.valueOf(fromStorageType, toStorageType, fromStorage.collectUpdate(),
					toStorage.collectUpdate());
		}
		return result;
	}

	private ItemResource getTargetLevelResource(int level, List<ItemResource> resources) {
		for (ItemResource r : resources) {
			if (r.getLifeGridLevel() == level) {
				return r;
			}
		}
		return null;
	}

	public SM_LifeGrid_DevourAll devourAll(Player player, LifeStorageType type) {
		if (!moduleOpenManager.isOpenByModuleKey(player, ModuleKey.LIFEGRID)) {
			throw new ManagedException(ManagedErrorCode.MODULE_NOT_OPEN);
		}

		if (type == LifeStorageType.EQUIP) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}

		LifeGridPool pool = player.getLifeGridPool();
		LifeGridStorage storage = pool.getStorageByType(type);

		QueryDevourResult queryResult = storage.getDevourItem();
		if (queryResult == null) {
			throw new ManagedException(ManagedErrorCode.LIFE_GRID_NOT_FOUND_DEVOUR_ITEM);
		}

		Map<Integer, LifeGridItem> devouredItems = storage.getBeDevourItems(queryResult.getIndex());

		if (devouredItems.isEmpty()) {
			throw new ManagedException(ManagedErrorCode.LIFE_GRID_NOT_FOUND_DEVOURED_ITEMS);
		}

		List<ItemResource> sameTypeResources = ItemManager.getInstance()
				.getSameTypeLifeGridResource(queryResult.getItem().getResource().getQuality(),
						queryResult.getItem().getResource().getLifeGridType());

		LifeGridItem targetItem = storage.getItemByIndex(queryResult.getIndex());

		// 最终消耗的命格
		Map<Integer, LifeGridItem> targetDevouredItems = new HashMap<Integer, LifeGridItem>();
		int newExp = targetItem.getExp();
		int newlevel = targetItem.getResource().getLevel();
		int tempLevel = targetItem.getResource().getLevel();
		for (Map.Entry<Integer, LifeGridItem> devouredItemEntry : devouredItems.entrySet()) {
			LifeGridItem deItem = devouredItemEntry.getValue();
			int addExp = deItem.getExp() + deItem.getResource().getBaseExp();

			boolean maxLevel = false;
			newExp += addExp;
			targetDevouredItems.put(devouredItemEntry.getKey(), deItem);
			for (ItemResource r : sameTypeResources) {
				if (r.getLifeGridLevel() < tempLevel) {
					continue;
				}
				if (r.isLifeGridMaxLevel()) {
					maxLevel = true;
					break;
				}

				if (newExp >= r.getNeedExp()) {
					newlevel++;
				} else {
					break;
				}
			}

			if (maxLevel) {
				break;
			}
			tempLevel = newlevel;
		}

		ItemResource targetResource = getTargetLevelResource(newlevel, sameTypeResources);

		storage.removeItemByIndex(targetDevouredItems.keySet());
		int actualyAddExp = newExp - targetItem.getExp();
		for (Map.Entry<Integer, LifeGridItem> entry : targetDevouredItems.entrySet()) {
			LogManager.addLifeGridItemLog(player, System.currentTimeMillis(), ModuleInfo.valueOf(ModuleType.LIFEGRID,
					SubModuleType.DEVOUR_ALL_LIFEGRID_ITEM, targetResource.getKey() + "#" + actualyAddExp + "#"
							+ newExp), -1, type.getType(), entry.getValue().getSize(), entry.getValue(), player
					.getLifeGridPool().getTotalItemCountByKey(entry.getValue().getKey()));
		}
		storage.removeItemByIndex(queryResult.getIndex());
		LogManager.addLifeGridItemLog(
				player,
				System.currentTimeMillis(),
				ModuleInfo.valueOf(ModuleType.LIFEGRID, SubModuleType.DEVOUR_ALL_LIFEGRID_ITEM, targetResource.getKey()
						+ "#" + actualyAddExp + "#" + newExp), -1, type.getType(), queryResult.getItem().getSize(),
				queryResult.getItem(), player.getLifeGridPool().getTotalItemCountByKey(queryResult.getItem().getKey()));

		LifeGridItem newItem = ItemManager.getInstance().createItem(targetResource.getKey());
		newItem.setExp(newExp);
		if (queryResult.getItem().isLock()) {
			// 吞噬者是锁定的
			newItem.openLock();
		}
		storage.addItemByIndex(queryResult.getIndex(), newItem);
		LogManager.addLifeGridItemLog(player, System.currentTimeMillis(), ModuleInfo.valueOf(ModuleType.LIFEGRID,
				SubModuleType.DEVOUR_ALL_LIFEGRID_ITEM, targetResource.getKey() + "#" + actualyAddExp + "#" + newExp),
				1, type.getType(), newItem.getSize(), newItem,
				player.getLifeGridPool().getTotalItemCountByKey(newItem.getKey()));

		return SM_LifeGrid_DevourAll.valueOf(type, storage.collectUpdate());

	}

	public SM_LifeGrid_Drop drop(Player player, LifeStorageType type, int[] indexs) {
		if (player == null) {
			throw new ManagedException(ManagedErrorCode.NO_ROLE);
		}

		if (type == LifeStorageType.EQUIP) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}

		LifeGridStorage storage = player.getLifeGridPool().getStorageByType(type);

		for (int index : indexs) {
			LifeGridItem item = storage.getItemByIndex(index);
			if (item == null) {
				throw new ManagedException(ManagedErrorCode.OPERATE_TOO_FAST);
			}

			if (item.isLock()) {
				throw new ManagedException(ManagedErrorCode.LIFE_GRID_ITEM_LOCK);
			}
			if (item.getResource().isCannotDrop()) {
				throw new ManagedException(ManagedErrorCode.ITEM_CANNOT_DROP);
			}
		}

		for (int index : indexs) {
			LifeGridItem dropItem = storage.removeItemByIndex(index);
			LogManager.addLifeGridItemLog(player, System.currentTimeMillis(),
					ModuleInfo.valueOf(ModuleType.DROPITEM, SubModuleType.DROP_LIFEGRID_ITEM), -1, type.getType(),
					dropItem.getSize(), dropItem, player.getLifeGridPool().getTotalItemCountByKey(dropItem.getKey()));
		}

		SM_LifeGrid_Drop result = SM_LifeGrid_Drop.valueOf(type, storage.collectUpdate());
		return result;
	}

	public void convert(Player player, String id) {
		if (player == null) {
			throw new ManagedException(ManagedErrorCode.NO_ROLE);
		}
		if (!moduleOpenManager.isOpenByModuleKey(player, ModuleKey.LIFEGRID)) {
			throw new ManagedException(ManagedErrorCode.MODULE_NOT_OPEN);
		}

		LifeGridPool pool = player.getLifeGridPool();
		LifeGridStorage lifeGridPack = pool.getPackStorage();
		if (lifeGridPack.isFull()) {
			throw new ManagedException(ManagedErrorCode.LIFE_GRID_PACK_NOT_ENOUGH_SIZE);
		}

		LifeGridConvertResource resource = config.convertStorage.get(id, true);
		if (!resource.getConds().verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}

		if (pool.getPoint() < resource.getActPoint()) {
			throw new ManagedException(ManagedErrorCode.LIFE_GRID_POINT_NOT_ENOUGH);
		}

		rewardManager.grantReward(player, resource.getRewardId(),
				ModuleInfo.valueOf(ModuleType.LIFEGRID, SubModuleType.LIFEGRID_CONVERT_REWARD, id), null);

		int point = pool.decreasePoint(ModuleInfo.valueOf(ModuleType.LIFEGRID, SubModuleType.LIFEGRID_CONVERT_ACTION),
				resource.getActPoint());
		PacketSendUtility.sendPacket(player, SM_LifeGrid_Convert.valueOf(id, point));
	}

}
