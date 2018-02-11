package com.mmorpg.mir.model.item.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.h2.util.New;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.log.LogManager;
import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.chat.model.show.object.ItemShow;
import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.common.FormulaParmsUtil;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.action.CoreActionManager;
import com.mmorpg.mir.model.core.action.CoreActionType;
import com.mmorpg.mir.model.core.action.CoreActions;
import com.mmorpg.mir.model.core.action.CurrencyAction;
import com.mmorpg.mir.model.core.action.ItemAction;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.formula.Formula;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectId;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectType;
import com.mmorpg.mir.model.i18n.manager.I18NparamKey;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.item.AbstractItem;
import com.mmorpg.mir.model.item.Equipment;
import com.mmorpg.mir.model.item.ItemType;
import com.mmorpg.mir.model.item.PetItem;
import com.mmorpg.mir.model.item.SmeltVO;
import com.mmorpg.mir.model.item.UseableItem;
import com.mmorpg.mir.model.item.AbstractItem.ItemState;
import com.mmorpg.mir.model.item.config.ItemConfig;
import com.mmorpg.mir.model.item.entity.TreasureHistoryEntity;
import com.mmorpg.mir.model.item.event.CollectItemsEvent;
import com.mmorpg.mir.model.item.event.EquipEquipmentEvent;
import com.mmorpg.mir.model.item.event.EquipmentReElementEvent;
import com.mmorpg.mir.model.item.event.ExtendsEquipmentEvent;
import com.mmorpg.mir.model.item.event.TreasureEvent;
import com.mmorpg.mir.model.item.event.UnEquipEquipmentEvent;
import com.mmorpg.mir.model.item.model.EquipmentElementType;
import com.mmorpg.mir.model.item.model.EquipmentStat;
import com.mmorpg.mir.model.item.model.EquipmentStatType;
import com.mmorpg.mir.model.item.model.EquipmentStorageType;
import com.mmorpg.mir.model.item.model.TreasureType;
import com.mmorpg.mir.model.item.packet.CM_EquipReElement;
import com.mmorpg.mir.model.item.packet.CM_ExtendsEquip;
import com.mmorpg.mir.model.item.packet.CM_SmeltEquipment;
import com.mmorpg.mir.model.item.packet.CM_Super_Forge_Equip;
import com.mmorpg.mir.model.item.packet.SM_AddPackSize;
import com.mmorpg.mir.model.item.packet.SM_Attach_God;
import com.mmorpg.mir.model.item.packet.SM_Attach_SuicideState;
import com.mmorpg.mir.model.item.packet.SM_BuyBack;
import com.mmorpg.mir.model.item.packet.SM_CallBack_Pet;
import com.mmorpg.mir.model.item.packet.SM_Change_Equipment_Suit;
import com.mmorpg.mir.model.item.packet.SM_Change_Pet;
import com.mmorpg.mir.model.item.packet.SM_Combing_Ex;
import com.mmorpg.mir.model.item.packet.SM_Combining;
import com.mmorpg.mir.model.item.packet.SM_Combining_CreateSoul;
import com.mmorpg.mir.model.item.packet.SM_Compose;
import com.mmorpg.mir.model.item.packet.SM_Decompose_Item;
import com.mmorpg.mir.model.item.packet.SM_Drop;
import com.mmorpg.mir.model.item.packet.SM_Equip;
import com.mmorpg.mir.model.item.packet.SM_EquipReElement;
import com.mmorpg.mir.model.item.packet.SM_Equip_Change;
import com.mmorpg.mir.model.item.packet.SM_Equip_Create_Soul;
import com.mmorpg.mir.model.item.packet.SM_Equip_Inset_Gem;
import com.mmorpg.mir.model.item.packet.SM_Equip_Unset_Gem;
import com.mmorpg.mir.model.item.packet.SM_Equipment_Update;
import com.mmorpg.mir.model.item.packet.SM_Equipment_Update_Ex;
import com.mmorpg.mir.model.item.packet.SM_Equips_Update;
import com.mmorpg.mir.model.item.packet.SM_Equips_Update_Horse;
import com.mmorpg.mir.model.item.packet.SM_ExchagePack;
import com.mmorpg.mir.model.item.packet.SM_Merge;
import com.mmorpg.mir.model.item.packet.SM_MovePack;
import com.mmorpg.mir.model.item.packet.SM_Packet_Update;
import com.mmorpg.mir.model.item.packet.SM_Sell;
import com.mmorpg.mir.model.item.packet.SM_SmeltEquipment;
import com.mmorpg.mir.model.item.packet.SM_SmeltEquipment_Horse;
import com.mmorpg.mir.model.item.packet.SM_Split;
import com.mmorpg.mir.model.item.packet.SM_Store;
import com.mmorpg.mir.model.item.packet.SM_Super_Forge_Equip;
import com.mmorpg.mir.model.item.packet.SM_Take;
import com.mmorpg.mir.model.item.packet.SM_Treasure_All_History;
import com.mmorpg.mir.model.item.packet.SM_Treasure_Storage_Update;
import com.mmorpg.mir.model.item.packet.SM_UnEquip;
import com.mmorpg.mir.model.item.packet.SM_UnEquip_Change;
import com.mmorpg.mir.model.item.packet.SM_Upgrade_Soul;
import com.mmorpg.mir.model.item.packet.SM_UseItem;
import com.mmorpg.mir.model.item.resource.CombingExResource;
import com.mmorpg.mir.model.item.resource.CombiningResource;
import com.mmorpg.mir.model.item.resource.EquipmentComposeResource;
import com.mmorpg.mir.model.item.resource.EquipmentCreateSoulResource;
import com.mmorpg.mir.model.item.resource.EquipmentEnhanceExResource;
import com.mmorpg.mir.model.item.resource.EquipmentEnhanceResource;
import com.mmorpg.mir.model.item.resource.EquipmentGodResource;
import com.mmorpg.mir.model.item.resource.EquipmentSoulResource;
import com.mmorpg.mir.model.item.resource.EquipmentSuitResource;
import com.mmorpg.mir.model.item.resource.EquipmentTurnResource;
import com.mmorpg.mir.model.item.resource.ItemResource;
import com.mmorpg.mir.model.item.resource.TreasureItemFixResource;
import com.mmorpg.mir.model.item.resource.TreasureResource;
import com.mmorpg.mir.model.item.storage.EquipmentStorage;
import com.mmorpg.mir.model.item.storage.ItemStorage;
import com.mmorpg.mir.model.item.storage.PlayerItemStorage;
import com.mmorpg.mir.model.item.storage.RemoveItem;
import com.mmorpg.mir.model.item.storage.TreasureItemStorage;
import com.mmorpg.mir.model.item.storage.EquipmentStorage.EquipmentType;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.mail.manager.MailManager;
import com.mmorpg.mir.model.mail.model.Mail;
import com.mmorpg.mir.model.moduleopen.manager.ModuleOpenManager;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.purse.model.CurrencyType;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.reward.model.RewardItem;
import com.mmorpg.mir.model.reward.model.RewardType;
import com.mmorpg.mir.model.shop.manager.ShopManager;
import com.mmorpg.mir.model.shop.resouce.ShopResource;
import com.mmorpg.mir.model.trigger.manager.TriggerManager;
import com.mmorpg.mir.model.trigger.model.TriggerContextKey;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.welfare.event.EnhanceEquipmentEvent;
import com.mmorpg.mir.model.welfare.event.SmeltEquipmentEvent;
import com.windforce.common.event.core.EventBusManager;
import com.windforce.common.ramcache.anno.Inject;
import com.windforce.common.ramcache.service.EntityBuilder;
import com.windforce.common.ramcache.service.EntityCacheService;
import com.windforce.common.utility.DateUtils;
import com.windforce.common.utility.RandomUtils;

@Component
public final class ItemService implements IItemService {

	@Autowired
	private ItemManager itemManager;

	@Autowired
	private RewardManager rewardManager;

	@Autowired
	private CoreActionManager coreActionManager;

	@Autowired
	private TriggerManager triggerManager;

	@Autowired
	private ModuleOpenManager moduleOpenManager;

	private static ItemService self;

	public static ItemService getInstance() {
		return self;
	}

	@Inject
	private EntityCacheService<Long, TreasureHistoryEntity> cachService;

	private TreasureHistoryEntity treasureHistoryEntity;

	@PostConstruct
	void init() {
		treasureHistoryEntity = cachService.loadOrCreate(1L, new EntityBuilder<Long, TreasureHistoryEntity>() {

			@Override
			public TreasureHistoryEntity newInstance(Long id) {
				return TreasureHistoryEntity.valueOf(id);
			}
		});
		treasureHistoryEntity.setEntityCachService(cachService);
		treasureHistoryEntity.getTreasureHistory().setTreasureHistoryEntity(treasureHistoryEntity);
		self = this;
	}

	@Override
	public PetItem callPet(Player player, int index) {
		if (player == null) {
			throw new ManagedException(ManagedErrorCode.NO_ROLE);
		}

		if (player.isTrading()) {
			throw new ManagedException(ManagedErrorCode.IN_TRADING_ERROR);
		}

		ItemStorage pack = player.getPack();
		EquipmentStorage eStore = player.getEquipmentStorage();
		AbstractItem petItem = pack.getItemByIndex(index);
		if (petItem == null || !(petItem instanceof PetItem)) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		if (!petItem.getResource().getItemConditions(1).verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		if (petItem.checkDeprecated()) {
			throw new ManagedException(ManagedErrorCode.PET_IS_DEPRECATED);
		}
		petItem.openState(ItemState.BIND.getMark());
		pack.removeItemByIndex(index);
		PetItem beforeItem = eStore.callPet(petItem);
		if (beforeItem != null) {
			beforeItem.stopDeprecatedFuture();
			Map<String, String> params = New.hashMap();
			params.put(ItemState.BIND.name(), "1");
			if (beforeItem.getDeprecatedTime() != 0) {
				params.put(ItemState.DEPRECATED.name(),
						DateUtils.date2String(new Date(beforeItem.getDeprecatedTime()), DateUtils.PATTERN_DATE_TIME));
			}
			Reward reward = Reward.valueOf().addRewardItem(
					RewardItem.valueOf(RewardType.ITEM, beforeItem.getKey(), 1, params));
			rewardManager.grantReward(player, reward,
					ModuleInfo.valueOf(ModuleType.PET_ITEM, SubModuleType.PET_ITEM_UNEQUIP));
		}
		petItem.startDeprecatedFuture(player, false);
		player.getGameStats().replaceModifiers(
				StatEffectId.valueOf(ItemType.PETITEM.name(), StatEffectType.ITEM_EFFECT),
				player.getEquipmentStorage().getPetResource().getStats(), true);
		PacketSendUtility.sendPacket(player, SM_Packet_Update.valueOf(player));
		PacketSendUtility.broadcastPacket(player, SM_Change_Pet.valueOf(player, petItem.getResource().getTemplateId()));

		return (PetItem) petItem;
	}

	@Override
	public void callBackPet(Player player) {
		PetItem item = player.getEquipmentStorage().getPetItem();
		if (item == null) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		if (player.getPack().isFull()) {
			throw new ManagedException(ManagedErrorCode.PACK_FULL);
		}
		callBackPet(player, item.getObjectId());
	}

	public void callBackPet(Player player, long petItemId) {
		PetItem petItem = player.getEquipmentStorage().getPetItem();
		if (petItem == null || petItem.getObjectId() != petItemId) {
			return;
		}
		player.getEquipmentStorage().changePetItem(petItem, null);
		boolean isNewerPet = petItem.getKey().equals(ItemConfig.getInstance().NEWER_PET_ITEM_KEY.getValue());
		boolean isBuy = player.getShoppingHistory().getTotalCount(
				ItemConfig.getInstance().NEWER_BUY_PET_SHOP_ID.getValue()) > 0;
		player.getGameStats().endModifiers(StatEffectId.valueOf(ItemType.PETITEM.name(), StatEffectType.ITEM_EFFECT),
				true);
		petItem.stopDeprecatedFuture();
		PacketSendUtility.sendPacket(player, SM_CallBack_Pet.valueOf(player));
		if (!isNewerPet || (!isBuy)) {
			Map<String, String> params = New.hashMap();
			params.put(ItemState.BIND.name(), "1");
			if (petItem.getDeprecatedTime() != 0) {
				params.put(ItemState.DEPRECATED.name(),
						DateUtils.date2String(new Date(petItem.getDeprecatedTime()), DateUtils.PATTERN_DATE_TIME));
			}
			Reward reward = Reward.valueOf().addRewardItem(
					RewardItem.valueOf(RewardType.ITEM, petItem.getKey(), 1, params));
			rewardManager.grantReward(player, reward,
					ModuleInfo.valueOf(ModuleType.PET_ITEM, SubModuleType.PET_ITEM_DEPRECATED));
		}
		PacketSendUtility.broadcastPacket(player, SM_Change_Pet.valueOf(player, (short) 0));
	}

	public void deleteNewerPet(Player player, boolean broadCast) {
		PetItem petItem = player.getEquipmentStorage().getPetItem();
		if (petItem != null && petItem.getKey().equals(ItemConfig.getInstance().NEWER_PET_ITEM_KEY.getValue())) {
			player.getGameStats().endModifiers(
					StatEffectId.valueOf(ItemType.PETITEM.name(), StatEffectType.ITEM_EFFECT), true);
			petItem.stopDeprecatedFuture();
			PacketSendUtility.sendPacket(player, SM_CallBack_Pet.valueOf(player));
			PacketSendUtility.broadcastPacket(player, SM_Change_Pet.valueOf(player, (short) 0));
			player.getEquipmentStorage().changePetItem(petItem, null);
		}
		List<RemoveItem> removes = player.getPack().removeItemByKeyNotDeprecated(
				ItemConfig.getInstance().NEWER_PET_ITEM_KEY.getValue());
		ModuleInfo moduleInfo = ModuleInfo.valueOf(ModuleType.PET_ITEM, SubModuleType.NEWER_PET_INSTEAD,
				ItemConfig.getInstance().NEWER_PET_ITEM_KEY.getValue());
		removes.addAll(player.getWareHouse().removeItemByKeyNotDeprecated(
				ItemConfig.getInstance().NEWER_PET_ITEM_KEY.getValue()));
		if (broadCast) {
			for (String petItemKey : ItemConfig.getInstance().SHOP_PET_ITEM_KEY.getValue()) {
				AbstractItem shopItem = player.getPack().getItemByKey(petItemKey);
				if (shopItem != null && shopItem.isDeprecated()) {
					AbstractItem deleteItem = player.getPack().removeItemByGuid(shopItem.getObjectId());
					if (deleteItem != null)
						removes.add(RemoveItem.valueOf(deleteItem, deleteItem.getSize()));
				}
			}
		}
		for (RemoveItem remove : removes) {
			AbstractItem item = remove.getItem();
			LogManager.addItemLog(player, System.currentTimeMillis(), moduleInfo, -1, remove.getNum(), item, player
					.getPack().getItemSizeByKey(item.getKey()));
		}
		if (!removes.isEmpty()) {
			PacketSendUtility.sendPacket(player, SM_Packet_Update.valueOf(player));
		}

		if (broadCast) {
			I18nUtils utils = I18nUtils.valueOf("309301");
			utils.addParm("name", I18nPack.valueOf(player.getName()));
			utils.addParm("country", I18nPack.valueOf(player.getCountry().getName()));
			ChatManager.getInstance().sendSystem(0, utils, null);
			I18nUtils tvUtils = I18nUtils.valueOf("20401");
			tvUtils.addParm("name", I18nPack.valueOf(player.getName()));
			tvUtils.addParm("country", I18nPack.valueOf(player.getCountry().getName()));
			ChatManager.getInstance().sendSystem(11001, tvUtils, null);
		}
	}

	public SM_Equip equip(Player player, int index) {
		if (player == null) {
			throw new ManagedException(ManagedErrorCode.NO_ROLE);
		}

		if (player.isTrading()) {
			throw new ManagedException(ManagedErrorCode.IN_TRADING_ERROR);
		}

		ItemStorage pack = player.getPack();

		Equipment equip = checkIsEquipment(pack.getItemByIndex(index));

		ItemResource itemResource = itemManager.getResource(equip.getKey());
		EquipmentStorageType targetEquipStorageType = itemResource.getEquipStorageByType();

		EquipmentStorage eStore = targetEquipStorageType.getEquipmentStorage(player);
		// 验证玩家是否可以装备
		if (!equip.getEquipCoreConditions().verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}

		// TODO 这里写个测试，装备了以后就绑定
		if (equip.isEquipBind())
			equip.openState(ItemState.BIND.getMark());

		pack.removeItemByIndex(index);
		Equipment unEquip = eStore.equip(equip);
		if (unEquip != null) {
			player.getGameStats().endModifiers(StatEffectId.valueOf(unEquip.getObjectId(), StatEffectType.EQUIPMENT),
					false);
			unEquip.endEquipmentExtraStats(player, false);
			pack.addItems(unEquip);
			PacketSendUtility.broadcastPacket(player,
					SM_UnEquip_Change.valueOf(player.getObjectId(), unEquip.getKey(), targetEquipStorageType));
		}
		// 修改属性的修改
		player.getGameStats().addModifiers(StatEffectId.valueOf(equip.getObjectId(), StatEffectType.EQUIPMENT),
				equip.getModifiers(), false);

		PacketSendUtility.broadcastPacket(player,
				SM_Equip_Change.valueOf(player.getObjectId(), equip.getKey(), targetEquipStorageType.getWhere()));

		// 灵魂属性
		equip.addEquipmentExtraStats(player, false);
		itemManager.soulActivateChange(player, equip.getEquipmentType(), targetEquipStorageType);
		// 套装属性
		itemManager.calOnAddSuitStat(player, false);
		itemManager.calOnAddSuitStatOnOtherEquipStorage(player, false);
		player.getGameStats().recomputeStats();

		EventBusManager.getInstance().submit(EquipEquipmentEvent.valueOf(player));

		// SM_Equip result = new SM_Equip();
		// result.setPackUpdate(pack.collectUpdate());
		// result.setEquipUpdate(eStore.collectUpdate());

		SM_Equip result = SM_Equip.valueOf(player, targetEquipStorageType);
		return result;
	}

	public SM_UnEquip unEquip(Player player, int index, EquipmentStorageType equipStorageType) {
		if (player == null) {
			throw new ManagedException(ManagedErrorCode.NO_ROLE);
		}

		if (player.isTrading()) {
			throw new ManagedException(ManagedErrorCode.IN_TRADING_ERROR);
		}

		EquipmentStorage eStore = equipStorageType.getEquipmentStorage(player);
		Equipment equip = eStore.getEquip(index);
		if (equip == null) {
			throw new ManagedException(ManagedErrorCode.NOT_EQUIPMENT);
		}
		EquipmentType type = equip.getEquipmentType();

		ItemStorage pack = player.getPack();

		if (pack.isFull()) {
			throw new ManagedException(ManagedErrorCode.PACK_FULL);
		}

		Equipment unEquip = eStore.unEquip(type);
		pack.addItems(unEquip);

		PacketSendUtility.broadcastPacket(player,
				SM_UnEquip_Change.valueOf(player.getObjectId(), unEquip.getKey(), equipStorageType));
		// 更新属性
		player.getGameStats()
				.endModifiers(StatEffectId.valueOf(unEquip.getObjectId(), StatEffectType.EQUIPMENT), false);
		unEquip.endEquipmentExtraStats(player, false);
		unEquip.setSoulType(0);
		// 影响到其他的装备
		itemManager.soulActivateChange(player, unEquip.getEquipmentType(), equipStorageType);
		// 套装属性
		itemManager.calOnAddSuitStat(player, false);
		itemManager.calOnAddSuitStatOnOtherEquipStorage(player, false);
		player.getGameStats().recomputeStats();

		EventBusManager.getInstance().submit(UnEquipEquipmentEvent.valueOf(player));

		// SM_UnEquip result = new SM_UnEquip();
		// result.setPackUpdate(pack.collectUpdate());
		// result.setEquipUpdate(eStore.collectUpdate());

		SM_UnEquip result = SM_UnEquip.valueOf(player, equipStorageType);

		return result;
	}

	public SM_ExchagePack exchangePack(Player player, byte type, int fromIndex, int toIndex) {
		if (player == null) {
			throw new ManagedException(ManagedErrorCode.NO_ROLE);
		}

		if (player.isTrading()) {
			throw new ManagedException(ManagedErrorCode.IN_TRADING_ERROR);
		}

		ItemStorage pack = player.getPack();
		ItemStorage wareHouse = player.getWareHouse();

		SM_ExchagePack result = new SM_ExchagePack();
		if (type == 0) {
			pack.exchage(fromIndex, toIndex);
			result.setPackUpdate(pack.collectUpdate());
		} else {
			wareHouse.exchage(fromIndex, toIndex);
			result.setPackUpdate(wareHouse.collectUpdate());
		}
		return result;
	}

	public SM_Store store(Player player, HashSet<Integer> indexs) {
		if (player == null) {
			throw new ManagedException(ManagedErrorCode.NO_ROLE);
		}

		if (player.isTrading()) {
			throw new ManagedException(ManagedErrorCode.IN_TRADING_ERROR);
		}

		ItemStorage pack = player.getPack();
		ItemStorage wareHouse = player.getWareHouse();

		int size = indexs.size();
		if (wareHouse.getEmptySize() < size) {
			throw new ManagedException(ManagedErrorCode.WARE_NOT_ENOUGH);
		}

		exchange(pack, wareHouse, indexs);

		SM_Store result = new SM_Store();
		result.setPackUpdate(pack.collectUpdate());
		result.setWareHouseUpdate(wareHouse.collectUpdate());

		return result;
	}

	public SM_Take take(Player player, HashSet<Integer> indexs) {
		if (player == null) {
			throw new ManagedException(ManagedErrorCode.NO_ROLE);
		}

		if (player.isTrading()) {
			throw new ManagedException(ManagedErrorCode.IN_TRADING_ERROR);
		}

		ItemStorage pack = player.getPack();
		ItemStorage wareHouse = player.getWareHouse();

		int size = indexs.size();
		if (pack.getEmptySize() < size) {
			throw new ManagedException(ManagedErrorCode.PACK_NOT_ENOUGH);
		}

		exchange(wareHouse, pack, indexs);

		SM_Take result = new SM_Take();
		result.setPackUpdate(pack.collectUpdate());
		result.setWareHouseUpdate(wareHouse.collectUpdate());

		return result;
	}

	private void exchange(ItemStorage from, ItemStorage to, Set<Integer> indexs) {
		for (int index : indexs) {
			exchange(from, to, index);
		}
	}

	private void exchange(ItemStorage from, ItemStorage to, int index) {
		AbstractItem item = from.removeItemByIndex(index);
		if (item != null) {
			to.addItems(false, item);
		}
	}

	public SM_Drop drop(Player player, int[] indexs) {
		if (player == null) {
			throw new ManagedException(ManagedErrorCode.NO_ROLE);
		}

		if (player.isTrading()) {
			throw new ManagedException(ManagedErrorCode.IN_TRADING_ERROR);
		}

		ItemStorage pack = player.getPack();

		for (int index : indexs) {
			AbstractItem item = pack.getItemByIndex(index);
			if (item == null) {
				throw new ManagedException(ManagedErrorCode.OPERATE_TOO_FAST);
			}
			if (item.getResource().isCannotDrop()) {
				throw new ManagedException(ManagedErrorCode.ITEM_CANNOT_DROP);
			}
		}

		for (int index : indexs) {
			AbstractItem item = pack.removeItemByIndex(index);
			if (item != null) {
				if (item instanceof Equipment) {
					sendGemMail(player, false, (Equipment) item);
				}
				LogManager.addItemLog(player, System.currentTimeMillis(),
						ModuleInfo.valueOf(ModuleType.DROPITEM, SubModuleType.DROP_ITEM), -1, item.getSize(), item,
						player.getPack().getItemSizeByKey(item.getKey()));
			}
		}

		SM_Drop result = new SM_Drop();
		result.setPackUpdate(pack.collectUpdate());

		return result;
	}

	public SM_UseItem useItem(Player player, int index, int num) {
		if (player == null) {
			throw new ManagedException(ManagedErrorCode.NO_ROLE);
		}

		if (player.isTrading()) {
			throw new ManagedException(ManagedErrorCode.IN_TRADING_ERROR);
		}

		if (player.getLifeStats().isAlreadyDead()) {
			throw new ManagedException(ManagedErrorCode.DEAD_ERROR);
		}

		PlayerItemStorage pack = player.getPack();

		AbstractItem item = pack.getItemByIndex(index);
		if (!(item instanceof UseableItem)) {
			throw new ManagedException(ManagedErrorCode.NOT_USEABLE_ITEM);
		}

		if (item.getSize() < num) {
			throw new ManagedException(item.getResource().getErrorCode());
		}

		UseableItem use = (UseableItem) item;

		int group = use.getGroup();

		if (player.isPublicDisabled(group)) {
			throw new ManagedException(ManagedErrorCode.ITEM_IN_COOLDOWN);
		}

		CoreConditions conditions = use.getItemConditions(num);
		CoreActions actions = use.getItemActions(num);
		// TODO 这里需要传送模块信息
		ModuleInfo moduleInfo = ModuleInfo.valueOf(ModuleType.USEITEM, SubModuleType.USE_ITEM, item.getName());

		/** 这里会抛异常 **/
		if (!conditions.verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}

		actions.verify(player, true);

		List<String> rewardIds = new LinkedList<String>();
		for (int i = 0; i < num; i++) {
			if (use.getReward() != null) {
				rewardIds.add(use.getReward());
			}
			if (use.getResource().getChooserReward() != null) {
				List<String> rewards = ChooserManager.getInstance().chooseValueByRequire(player,
						use.getResource().getChooserReward());
				rewardIds.addAll(rewards);
			}
		}
		pack.cosumeItems(player, use.getKey(), num);

		Map<String, Object> params = New.hashMap();
		params.put("LEVEL", player.getLevel());
		params.put("STANDARD_EXP", PlayerManager.getInstance().getStandardExp(player));
		Reward reward = rewardManager.grantReward(player, rewardIds, moduleInfo, params);

		Map<String, Object> contexts = New.hashMap();
		contexts.put(TriggerContextKey.PLAYER, player);
		contexts.put(TriggerContextKey.REWARD, reward);
		for (int i = 0; i < num; i++) {
			for (String triggerId : use.getTriggerIds()) {
				// triggerManager.trigger(player, triggerId);
				triggerManager.trigger(contexts, triggerId);
			}
		}

		long now = System.currentTimeMillis();
		LogManager.addItemLog(player, System.currentTimeMillis(), moduleInfo, -1, num, item, player.getPack()
				.getItemSizeByKey(item.getKey()));

		int coolDown = use.getCoolDown();
		long newCoolDown = now + coolDown;
		player.setPublicCoolDown(group, newCoolDown);

		pack.reduceItemByIndex(index, num);
		actions.act(player, moduleInfo);

		SM_UseItem result = new SM_UseItem();
		result.setNextTime(newCoolDown);
		result.setPackUpdate(pack.collectUpdate());
		if (item.getResource().getIsSendReward() > 0) {
			result.setReward(reward);
		}
		return result;
	}

	public SM_Merge merge(Player player, byte type) {
		if (player == null) {
			throw new ManagedException(ManagedErrorCode.NO_ROLE);
		}

		if (player.isTrading()) {
			throw new ManagedException(ManagedErrorCode.IN_TRADING_ERROR);
		}

		SM_Merge result = new SM_Merge();

		ItemStorage storage = null;
		long cd = 0;

		if (type == 0) {
			storage = player.getPack();
			cd = ItemConfig.getInstance().BAGTIDYCD.getValue() * 1000L;
		} else if (type == 1) {
			storage = player.getWareHouse();
			cd = ItemConfig.getInstance().DEPOTTIDYCD.getValue() * 1000L;
		} else if (type == 3) {
			storage = player.getTreasureWareHouse();
			cd = ItemConfig.getInstance().TREASURETIDYCD.getValue() * 1000L;
		}

		long now = System.currentTimeMillis();

		if (now - storage.getLastMergeTime() < cd) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}

		storage.merge();
		storage.setLastMergeTime(now);
		result.setStorage(storage);

		return result;
	}

	public SM_MovePack movePack(Player player, byte type, int fromIndex, int toIndex) {
		if (player == null) {
			throw new ManagedException(ManagedErrorCode.NO_ROLE);
		}

		if (player.isTrading()) {
			throw new ManagedException(ManagedErrorCode.IN_TRADING_ERROR);
		}

		ItemStorage pack = player.getPack();
		ItemStorage wareHouse = player.getWareHouse();

		if (type == 0) {
			AbstractItem fItem = pack.removeItemByIndex(fromIndex);
			AbstractItem tItem = wareHouse.removeItemByIndex(toIndex);
			pack.addItemByIndex(fromIndex, tItem);
			wareHouse.addItemByIndex(toIndex, fItem);
		} else {
			AbstractItem fItem = wareHouse.removeItemByIndex(fromIndex);
			AbstractItem tItem = pack.removeItemByIndex(toIndex);
			wareHouse.addItemByIndex(fromIndex, tItem);
			pack.addItemByIndex(toIndex, fItem);
		}

		SM_MovePack result = new SM_MovePack();
		result.setPackUpdate(pack.collectUpdate());
		result.setWareHouseUpdate(wareHouse.collectUpdate());
		return result;
	}

	public SM_AddPackSize addPackSize(Player player, byte type, int index) {
		if (player == null) {
			throw new ManagedException(ManagedErrorCode.NO_ROLE);
		}

		if (player.isTrading()) {
			throw new ManagedException(ManagedErrorCode.IN_TRADING_ERROR);
		}

		ItemStorage pack = player.getPack();
		ItemStorage wareHouse = player.getWareHouse();

		ItemStorage storage = null;

		if (type == 0) {
			storage = pack;
		} else {
			storage = wareHouse;
		}

		if (storage.getSize() >= index + 1 || index + 1 > itemManager.getMaxSize(type)) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}

		int size = index - storage.getSize() + 1;
		long now = System.currentTimeMillis();
		if (storage.getOpenTime() < now) {
			size--;
		}

		int price = 0;
		while (size > 0) {
			price += itemManager.getAddPackPrice(index - size + 1, type);
			size--;
		}
		ModuleInfo moduleInfo = ModuleInfo
				.valueOf(ModuleType.BUYPACK, SubModuleType.ADD_PACKSIZE, String.valueOf(type));

		CurrencyAction action = CoreActionType.createCurrencyCondition(CurrencyType.GOLD, price);
		action.verify(player);
		action.act(player, moduleInfo);
		int expAmount = 0;
		for (int i = storage.getSize(); i <= index; i++) {
			expAmount += itemManager.getAddPackExp(i, type);
		}
		Stat newHpStat = itemManager.replaceAddPackStat(index, type);
		if (type == 0) {
			player.getGameStats().replaceModifiers(ItemManager.OPEN_PACK, Arrays.asList(newHpStat), true);
			player.getPack().setHpStat(newHpStat);
		} else {
			player.getGameStats().replaceModifiers(ItemManager.OPEN_WAREHOUSE, Arrays.asList(newHpStat), true);
			player.getWareHouse().setHpStat(newHpStat);
		}

		storage.setSize(index + 1);
		long waitTime = itemManager.getReaminTime(storage, type);
		// VIP缩小时间
		if (type == 0) {
			waitTime = (long) Math.ceil(waitTime * 1.0
					/ ((player.getVip().getResource().getBagOpenSpeedUp() + 10000) * 1.0 / 10000));
		} else {
			waitTime = (long) Math.ceil(waitTime * 1.0
					/ ((player.getVip().getResource().getWareHouseOpenSpeedUp() + 10000) * 1.0 / 10000));
		}
		storage.calculateWaitTime(waitTime);
		storage.reset();

		rewardManager.grantReward(player, Reward.valueOf().addExp(expAmount), moduleInfo);
		SM_AddPackSize result = new SM_AddPackSize();
		result.setOpenTime(storage.getOpenTime());
		result.setPackSize(storage.getSize());
		result.setWaitTime(storage.getWaitTime());

		return result;
	}

	public SM_Split split(Player player, byte type, int index, int size) {
		if (player == null) {
			throw new ManagedException(ManagedErrorCode.NO_ROLE);
		}

		if (player.isTrading()) {
			throw new ManagedException(ManagedErrorCode.IN_TRADING_ERROR);
		}

		ItemStorage pack = player.getPack();
		ItemStorage wareHouse = player.getWareHouse();

		ItemStorage storage = null;

		if (type == 0) {
			storage = pack;
		} else {
			storage = wareHouse;
		}

		if (storage.isFull()) {
			throw new ManagedException(ManagedErrorCode.PACK_FULL);
		}

		AbstractItem item = storage.getItemByIndex(index);
		if (item == null) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}

		if (item.getSize() <= size) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}

		storage.reduceItemByIndex(index, size);
		AbstractItem[] newItems = ItemManager.getInstance().createItems(item.getKey(), size);
		for (AbstractItem newItem : newItems) {
			newItem.setDeprecatedTime(item.getDeprecatedTime());
			newItem.setState(item.getState());
		}
		storage.addItems(false, newItems);

		SM_Split result = new SM_Split();
		result.setPackUpdate(storage.collectUpdate());

		return result;
	}

	public SM_Sell sell(Player player, HashSet<Integer> indexs) {
		if (player == null) {
			throw new ManagedException(ManagedErrorCode.NO_ROLE);
		}

		if (player.isTrading()) {
			throw new ManagedException(ManagedErrorCode.IN_TRADING_ERROR);
		}

		ItemStorage pack = player.getPack();
		ItemStorage buyBackPack = player.getBuyBackPack();

		int copper = 0;

		for (int index : indexs) {
			AbstractItem item = pack.getItemByIndex(index);
			if (item == null) {
				throw new ManagedException(ManagedErrorCode.OPERATE_TOO_FAST);
			}
			// 这里需要判断一下是否可以卖出
			if (!item.getResource().canSell()) {
				throw new ManagedException(ManagedErrorCode.OPERATE_TOO_FAST);
			}

			copper += item.getSellPrice() * item.getSize();
		}

		ModuleInfo moduleInfo = ModuleInfo.valueOf(ModuleType.SELLITEM, SubModuleType.SELL_ITEM);

		for (int index : indexs) {
			AbstractItem item = pack.removeItemByIndex(index);
			if (item != null && item.canBuyBack()) {
				if (item instanceof Equipment) {
					sendGemMail(player, false, (Equipment) item);
				}
				buyBackPack.addItems(false, item);
			}

			LogManager.addItemLog(player, System.currentTimeMillis(), moduleInfo, -1, item.getSize(), item, player
					.getPack().getItemSizeByKey(item.getKey()));
		}

		RewardManager.getInstance().grantReward(player, Reward.valueOf().addCurrency(CurrencyType.COPPER, copper),
				moduleInfo);

		SM_Sell result = new SM_Sell();
		result.setPackUpdate(pack.collectUpdate());
		result.setBuyBackUpdate(buyBackPack.collectUpdate());
		return result;
	}

	public SM_BuyBack buyBack(Player player, int index) {
		if (player == null) {
			throw new ManagedException(ManagedErrorCode.NO_ROLE);
		}

		if (player.isTrading()) {
			throw new ManagedException(ManagedErrorCode.IN_TRADING_ERROR);
		}

		ItemStorage pack = player.getPack();
		ItemStorage buyBackPack = player.getBuyBackPack();

		if (pack.isFull())
			throw new ManagedException(ManagedErrorCode.PACK_FULL);

		AbstractItem item = buyBackPack.getItemByIndex(index);

		if (item == null)
			throw new ManagedException(ManagedErrorCode.OPERATE_TOO_FAST);

		int copper = item.getSellPrice() * item.getSize();

		CurrencyAction action = CoreActionType.createCurrencyCondition(CurrencyType.COPPER, copper);

		ModuleInfo moduleInfo = ModuleInfo.valueOf(ModuleType.BUYBACK, SubModuleType.BUYBACK, item.getKey());

		action.verify(player);
		action.act(player, moduleInfo);

		item = buyBackPack.removeItemByIndex(index);
		pack.addItems(false, item);

		LogManager.addItemLog(player, System.currentTimeMillis(), moduleInfo, 1, item.getSize(), item, player.getPack()
				.getItemSizeByKey(item.getKey()));

		SM_BuyBack result = new SM_BuyBack();
		result.setPackUpdate(pack.collectUpdate());
		result.setBuyBackUpdate(buyBackPack.collectUpdate());
		// result.setCurrencyUpdate(player.getPurse().collectUpdate());
		return result;
	}

	public void enhanceEquipment(Player player, int equipIndex, boolean gold) {
		Equipment strengthening = player.getEquipmentStorage().getEquip(equipIndex);
		if (strengthening == null)
			throw new ManagedException(ManagedErrorCode.IMPROVE_EQUIP_NOT_EXIST);

		EquipmentEnhanceResource resource = itemManager.getEquipEnhanceResource(strengthening.getEnhanceLevel());

		CoreConditions conditions = CoreConditionManager.getInstance().getCoreConditions(1, resource.getConditionIds());
		if (!conditions.verify(strengthening, true)) {
			PacketSendUtility.sendErrorMessage(player);
			return;
		}

		if (resource.getNextLevel() == 0) {
			throw new ManagedException(ManagedErrorCode.IMPROVE_EQUIP_MAX_LEVEL);
		}

		CoreActions actions = new CoreActions();
		CoreActions copperAction = CoreActionManager.getInstance().getCoreActions(1, resource.getCopperActionIds());
		copperAction.verify(player, true);
		CoreActions priAction = CoreActionManager.getInstance().getCoreActions(1, resource.getActionIds());
		if (priAction.verify(player)) {
			actions.addActions(priAction);
		} else {
			if (!gold) {
				throw new ManagedException(ManagedErrorCode.NOT_ENOUGH_ITEM);
			}
			CoreConditions cond = CoreConditionManager.getInstance().getCoreConditions(1,
					resource.getAutoBuyCondition());
			if (!cond.verify(player, false)) {
				PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.ENHANCE_AUTO_BUY_FAIL);
				return;
			}
			String itemKey = priAction.getFirstItemKey();
			int have = (int) player.getPack().getItemSizeByKey(itemKey);
			ShopResource res = ShopManager.getInstance().getShopResource(resource.getAutoBuyShopId());
			actions.addActions(CoreActionManager.getInstance().getCoreActions(resource.getAutoBuyNums() - have,
					res.getActions()));
			if (have > 0) {
				actions.addActions(CoreActionType.createItemCondition(itemKey, have));
			}
		}
		ModuleInfo moduleInfo = ModuleInfo.valueOf(ModuleType.ENHANCE, SubModuleType.ENHANCE_EQUIP);
		actions.addActions(copperAction);
		actions.verify(player, true);
		actions.act(player, moduleInfo);

		// improve equipment, if fail add luckyPoint
		Integer curLuckPoint = player.getPack().getEnhanceLuckPoints().get(resource.getLuckyPointType());
		// Long luckEndTime =
		// player.getPack().getEnhanceLuckTime().get(resource.getLuckyPointType());
		Integer enhanceCount = player.getEquipmentStorage().getEnhanceCount().get(resource.getLuckyPointType());
		// long now = System.currentTimeMillis();

		if (curLuckPoint == null || enhanceCount == null) {
			curLuckPoint = 0;
			enhanceCount = 0;
			player.getPack().getEnhanceLuckPoints().put(resource.getLuckyPointType(), curLuckPoint);
			// player.getPack().getEnhanceLuckTime().put(resource.getLuckyPointType(),
			// now + DateUtils.MILLIS_PER_DAY);
			player.getEquipmentStorage().getEnhanceCount().put(resource.getLuckyPointType(), enhanceCount);
		}

		boolean costTooLess = enhanceCount + 1 <= resource.getMinSum();
		boolean costTooMuch = enhanceCount + 1 >= resource.getMaxSum();
		boolean fail = costTooLess ? true : (curLuckPoint < resource.getLuckyMax() && (!RandomUtils.isHit(resource
				.getSuccRate() * 1.0 / 10000.0)));
		if (fail && !costTooMuch) {
			player.getEquipmentStorage().getEnhanceCount().put(resource.getLuckyPointType(), enhanceCount + 1);
			String value = ChooserManager.getInstance().chooseValueByRequire(player, resource.getLuckyPoint()).get(0);
			int addLucky = Integer.valueOf(value);
			player.getPack().getEnhanceLuckPoints().put(resource.getLuckyPointType(), curLuckPoint + addLucky);
			PacketSendUtility.sendPacket(player, SM_Equipment_Update.failReturn(
					player.getPack().getEnhanceLuckPoints(), player.getPack().getEnhanceLuckTime(), 404, addLucky));
		} else {
			player.getEquipmentStorage().getEnhanceCount().put(resource.getLuckyPointType(), 0);
			player.getPack().getEnhanceLuckPoints().put(resource.getLuckyPointType(), 0);
			// player.getPack().getEnhanceLuckTime().put(resource.getLuckyPointType(),
			// 0L);
			strengthening.setEnhanceLevel(resource.getNextLevel());
			player.getGameStats().replaceModifiers(
					StatEffectId.valueOf(strengthening.getObjectId(), StatEffectType.EQUIPMENT),
					strengthening.getModifiers(), false);
			PacketSendUtility.sendPacket(player, SM_Equipment_Update.valueOf(player.getPack().getEnhanceLuckPoints(),
					player.getPack().getEnhanceLuckTime(), strengthening));
			// 星级套装
			itemManager.calculateStarSuit(player, false, EquipmentStorageType.PLAYER);
			player.getGameStats().recomputeStats();
			if (strengthening.getEnhanceLevel() >= 16) {
				ItemShow show = new ItemShow();
				show.setKey(strengthening.getKey());
				show.setOwner(player.getName());
				show.setItem(strengthening);
				I18nUtils utils = I18nUtils.valueOf("301005").addParm("user", I18nPack.valueOf(player.createSimple()))
						.addParm(I18NparamKey.COUNTRY, I18nPack.valueOf(player.getCountry().getName()))
						.addParm(I18NparamKey.EQUIPMENT_KEY, I18nPack.valueOf(show))
						.addParm(I18NparamKey.ENHANCE_LEVEL, I18nPack.valueOf(strengthening.getEnhanceLevel() + ""));
				ChatManager.getInstance().sendSystem(0, utils, null); // 聊天频道
				I18nUtils systemSend = I18nUtils.valueOf("20101");
				systemSend.addParm("name", I18nPack.valueOf(player.getName()));
				systemSend.addParm(I18NparamKey.COUNTRY, I18nPack.valueOf(player.getCountry().getName()));
				systemSend.addParm(I18NparamKey.EQUIPMENT_KEY, I18nPack.valueOf(show));
				systemSend.addParm(I18NparamKey.ENHANCE_LEVEL, I18nPack.valueOf(strengthening.getEnhanceLevel() + ""));
				ChatManager.getInstance().sendSystem(11001, systemSend, null); // TV
			} else if (strengthening.getEnhanceLevel() >= 12) {
				ItemShow show = new ItemShow();
				show.setKey(strengthening.getKey());
				show.setOwner(player.getName());
				show.setItem(strengthening);
				I18nUtils utils = I18nUtils.valueOf("301004").addParm("user", I18nPack.valueOf(player.createSimple()))
						.addParm(I18NparamKey.COUNTRY, I18nPack.valueOf(player.getCountry().getName()))
						.addParm(I18NparamKey.EQUIPMENT_KEY, I18nPack.valueOf(show))
						.addParm(I18NparamKey.ENHANCE_LEVEL, I18nPack.valueOf(strengthening.getEnhanceLevel() + ""));
				ChatManager.getInstance().sendSystem(0, utils, null);
			}

			// 强化成功日至
			LogManager.addEnhanceLog(player.getPlayerEnt().getAccountName(), player.getName(), player.getObjectId(),
					System.currentTimeMillis(), strengthening.getObjectId(), strengthening.getKey(),
					resource.getLevel(), strengthening.getEnhanceLevel());
		}

		EventBusManager.getInstance().submit(EnhanceEquipmentEvent.valueOf(player));
	}

	/**
	 * 强化其他装备栏装备
	 * 
	 * @param player
	 * @param equipIndex
	 * @param gold
	 * @param equipStorageType
	 *            装备栏类型
	 */
	public void enhanceEquipmentEx(Player player, int equipIndex, boolean gold, EquipmentStorageType equipStorageType) {
		EquipmentStorage targetEquipStorage = equipStorageType.getEquipmentStorage(player);
		Equipment strengthening = targetEquipStorage.getEquip(equipIndex);
		if (strengthening == null)
			throw new ManagedException(ManagedErrorCode.IMPROVE_EQUIP_NOT_EXIST);

		EquipmentEnhanceExResource resource = itemManager.getEquipEnhaceResourceEx(strengthening.getEnhanceLevel(),
				equipStorageType.getWhere());

		CoreConditions conditions = CoreConditionManager.getInstance().getCoreConditions(1, resource.getConditionIds());
		if (!conditions.verify(strengthening, true)) {
			PacketSendUtility.sendErrorMessage(player);
			return;
		}

		if (resource.getNextLevel() == 0) {
			throw new ManagedException(ManagedErrorCode.IMPROVE_EQUIP_MAX_LEVEL);
		}

		CoreActions actions = new CoreActions();
		CoreActions copperAction = CoreActionManager.getInstance().getCoreActions(1, resource.getCopperActionIds());
		copperAction.verify(player, true);
		CoreActions priAction = CoreActionManager.getInstance().getCoreActions(1, resource.getActionIds());
		if (priAction.verify(player)) {
			actions.addActions(priAction);
		} else {
			if (!gold) {
				throw new ManagedException(ManagedErrorCode.NOT_ENOUGH_ITEM);
			}
			CoreConditions cond = CoreConditionManager.getInstance().getCoreConditions(1,
					resource.getAutoBuyCondition());
			if (!cond.verify(player, false)) {
				PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.ENHANCE_AUTO_BUY_FAIL);
				return;
			}
			String itemKey = priAction.getFirstItemKey();
			int have = (int) player.getPack().getItemSizeByKey(itemKey);
			ShopResource res = ShopManager.getInstance().getShopResource(resource.getAutoBuyShopId());
			actions.addActions(CoreActionManager.getInstance().getCoreActions(resource.getAutoBuyNums() - have,
					res.getActions()));
			if (have > 0) {
				actions.addActions(CoreActionType.createItemCondition(itemKey, have));
			}
		}
		SubModuleType subType = null;
		if (equipStorageType == EquipmentStorageType.HORSE) {
			subType = SubModuleType.ENHANCE_HORSE_EQUIP;
		}
		ModuleInfo moduleInfo = ModuleInfo.valueOf(ModuleType.ENHANCE, subType);
		actions.addActions(copperAction);
		actions.verify(player, true);
		actions.act(player, moduleInfo);

		// improve equipment, if fail add luckyPoint
		Integer curLuckPoint = player.getPack().getEnhanceLuckPoints().get(resource.getLuckyPointType());
		// Long luckEndTime =
		// player.getPack().getEnhanceLuckTime().get(resource.getLuckyPointType());
		Integer enhanceCount = targetEquipStorage.getEnhanceCount().get(resource.getLuckyPointType());
		// long now = System.currentTimeMillis();

		if (curLuckPoint == null || enhanceCount == null) {
			curLuckPoint = 0;
			enhanceCount = 0;
			player.getPack().getEnhanceLuckPoints().put(resource.getLuckyPointType(), curLuckPoint);
			// player.getPack().getEnhanceLuckTime().put(resource.getLuckyPointType(),
			// now + DateUtils.MILLIS_PER_DAY);
			targetEquipStorage.getEnhanceCount().put(resource.getLuckyPointType(), enhanceCount);
		}

		boolean costTooLess = enhanceCount + 1 <= resource.getMinSum();
		boolean costTooMuch = enhanceCount + 1 >= resource.getMaxSum();
		boolean fail = costTooLess ? true : (curLuckPoint < resource.getLuckyMax() && (!RandomUtils.isHit(resource
				.getSuccRate() * 1.0 / 10000.0)));
		if (fail && !costTooMuch) {
			targetEquipStorage.getEnhanceCount().put(resource.getLuckyPointType(), enhanceCount + 1);
			String value = ChooserManager.getInstance().chooseValueByRequire(player, resource.getLuckyPoint()).get(0);
			int addLucky = Integer.valueOf(value);
			player.getPack().getEnhanceLuckPoints().put(resource.getLuckyPointType(), curLuckPoint + addLucky);
			PacketSendUtility.sendPacket(player, SM_Equipment_Update_Ex.failReturn(player.getPack()
					.getEnhanceLuckPoints(), player.getPack().getEnhanceLuckTime(), 404, addLucky, equipStorageType));
		} else {
			targetEquipStorage.getEnhanceCount().put(resource.getLuckyPointType(), 0);
			player.getPack().getEnhanceLuckPoints().put(resource.getLuckyPointType(), 0);
			// player.getPack().getEnhanceLuckTime().put(resource.getLuckyPointType(),
			// 0L);
			strengthening.setEnhanceLevel(resource.getNextLevel());
			player.getGameStats().replaceModifiers(
					StatEffectId.valueOf(strengthening.getObjectId(), StatEffectType.EQUIPMENT),
					strengthening.getModifiers(), false);
			PacketSendUtility.sendPacket(player, SM_Equipment_Update_Ex.valueOf(
					player.getPack().getEnhanceLuckPoints(), player.getPack().getEnhanceLuckTime(), strengthening,
					equipStorageType));
			// 星级套装
			itemManager.calculateStarSuit(player, false, equipStorageType);
			player.getGameStats().recomputeStats();

			if (strengthening.getEnhanceLevel() >= 12) {
				ItemShow show = new ItemShow();
				show.setKey(strengthening.getKey());
				show.setOwner(player.getName());
				show.setItem(strengthening);
				I18nUtils utils = I18nUtils.valueOf("20112")
						.addParm(I18NparamKey.PLAYERNAME, I18nPack.valueOf(player.getName()))
						.addParm(I18NparamKey.COUNTRY, I18nPack.valueOf(player.getCountry().getName()))
						.addParm(I18NparamKey.EQUIPMENT_KEY, I18nPack.valueOf(show))
						.addParm(I18NparamKey.ENHANCE_LEVEL, I18nPack.valueOf(strengthening.getEnhanceLevel() + ""));
				ChatManager.getInstance().sendSystem(11001, utils, null);

				I18nUtils utils2 = I18nUtils.valueOf("301030").addParm("user", I18nPack.valueOf(player.getName()))
						.addParm(I18NparamKey.COUNTRY, I18nPack.valueOf(player.getCountry().getName()))
						.addParm(I18NparamKey.EQUIPMENT_KEY, I18nPack.valueOf(show))
						.addParm(I18NparamKey.ENHANCE_LEVEL, I18nPack.valueOf(strengthening.getEnhanceLevel() + ""));
				ChatManager.getInstance().sendSystem(0, utils2, null);

			}

			// 强化成功日至
			LogManager.addEnhanceLog(player.getPlayerEnt().getAccountName(), player.getName(), player.getObjectId(),
					System.currentTimeMillis(), strengthening.getObjectId(), strengthening.getKey(),
					resource.getLevel(), strengthening.getEnhanceLevel());
		}
		EventBusManager.getInstance().submit(EnhanceEquipmentEvent.valueOf(player));

	}

	public void forgeEquipment(Player player, String id, String recipeKey) {
		EquipmentComposeResource resource = itemManager.getEquipComposeResource(id);

		if (!resource.containsRecipe(recipeKey)) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		if (player.getPack().isFull()) {
			throw new ManagedException(ManagedErrorCode.PACK_FULL);
		}

		int size = resource.getActionMap().get(recipeKey).size();
		String[] actionIds = new String[size];
		CoreActions actions = CoreActionManager.getInstance().getCoreActions(1,
				resource.getActionMap().get(recipeKey).toArray(actionIds));
		actions.verify(player, true);

		List<String> result = ChooserManager.getInstance().chooseValueByRequire(player,
				resource.getResultChooserGroupMap().get(recipeKey));
		String key = itemManager.getItemResourceKeyByHash(resource.getRoleType(),
				resource.getEquipmentType().ordinal(), resource.getLevel(), Integer.valueOf(result.get(0)),
				resource.getSpecialType());
		Equipment constructImage = itemManager.createItem(key); // 仅仅只是用来传给前端显示用的
		Map<String, String> context = New.hashMap();

		String souLevel = ChooserManager
				.getInstance()
				.chooseValueByRequire(resource.getEquipmentType(), resource.getSoulLevelChooserGroupId().get(recipeKey))
				.get(0);
		if (!souLevel.equals("0")) { // 有灵魂属性
			String soulType = ChooserManager
					.getInstance()
					.chooseValueByRequire(resource.getEquipmentType(),
							resource.getSoulTypeChooserGroupId().get(recipeKey)).get(0);
			String soulKey = itemManager.getSoulIndexKey(resource.getEquipmentType(), souLevel, soulType);
			if (soulKey == null) {
				PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.TARGET_SOUL_STAT_NOT_EXIST);
				return;
			}
			context.put(EquipmentStatType.SOUL_STAT.name(), soulKey);
			constructImage.addExtraStats(EquipmentStatType.SOUL_STAT.getValue(), soulKey);
			if (resource.getElementChooserGroupMap() != null
					&& resource.getElementChooserGroupMap().get(recipeKey) != null) {// 五行
				String element = ChooserManager.getInstance()
						.chooseValueByRequire(player, resource.getElementChooserGroupMap().get(recipeKey)).get(0);
				context.put(EquipmentStatType.ELEMENT_STAT.name(), element);
				constructImage.setElement(Integer.valueOf(element));
			}
		}
		if (!ItemConfig.getInstance().DELETE_EQUIPMENT_PERFECT_STAT.getValue()) {
			String rareChooserGroupId = itemManager.getEquipmentRareChooserGroup(result.get(0));
			if (rareChooserGroupId != null) {
				StringBuilder sb = new StringBuilder();
				List<String> rares = ChooserManager.getInstance().chooseValueByRequire(resource.getEquipmentType(),
						rareChooserGroupId);
				for (String s : rares) {
					sb.append(s).append('|');
				}
				String[] temp = new String[rares.size()];
				constructImage.addExtraStats(EquipmentStatType.PERFECT_STAT.getValue(), rares.toArray(temp));
				context.put(EquipmentStatType.PERFECT_STAT.name(), sb.toString());
			}
		}
		context.put(ItemState.BIND.name(), "1");
		constructImage.openState(ItemState.BIND.getMark());

		ModuleInfo moduleInfo = ModuleInfo.valueOf(ModuleType.FORGE, SubModuleType.FORGE_EQUIP);
		actions.act(player, moduleInfo);
		rewardManager.grantReward(player, Reward.valueOf().addEquipment(key, 1, context), moduleInfo);

		if (Integer.valueOf(result.get(0)) == ItemResource.PERFECT_ORANGE) {
			ItemShow show = new ItemShow();
			show.setKey(constructImage.getKey());
			show.setOwner(player.getName());
			show.setItem(constructImage);
			I18nUtils utils = I18nUtils.valueOf("301006")
					.addParm(I18NparamKey.PLAYERNAME, I18nPack.valueOf(player.getName()))
					.addParm(I18NparamKey.COUNTRY, I18nPack.valueOf(player.getCountry().getName()))
					.addParm(I18NparamKey.EQUIPMENT_KEY, I18nPack.valueOf(show));
			ChatManager.getInstance().sendSystem(0, utils, null);

			I18nUtils systemUtils = I18nUtils.valueOf("20102")
					.addParm(I18NparamKey.PLAYERNAME, I18nPack.valueOf(player.getName()))
					.addParm(I18NparamKey.COUNTRY, I18nPack.valueOf(player.getCountry().getName()))
					.addParm(I18NparamKey.EQUIPMENT_KEY, I18nPack.valueOf(itemManager.getResource(key).getName()));
			ChatManager.getInstance().sendSystem(11001, systemUtils, null);
		}
		PacketSendUtility.sendPacket(player, SM_Compose.valueOf(constructImage));
		LogManager.addForgeLog(player.getPlayerEnt().getServer(), player.getPlayerEnt().getAccountName(),
				player.getName(), player.getObjectId(), System.currentTimeMillis(), constructImage.getKey(),
				context.get(EquipmentStatType.ELEMENT_STAT.name()), context.get(EquipmentStatType.SOUL_STAT.name()));
	}

	public void extendsEquipment(Player player, CM_ExtendsEquip req) {
		if (req.getMainEquipIndex() == req.getViceEquipIndex()) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}

		EquipmentStorageType targetEquipStorageType = EquipmentStorageType.typeOf(req.getEquipStorageType());
		EquipmentStorage targetEquipStorage = targetEquipStorageType.getEquipmentStorage(player);
		boolean mainSelectByEquip = true, viceSelectByEquip = true;
		Equipment main = null, vice = null;
		for (Equipment e : targetEquipStorage.getEquipments()) {
			if (e != null) {
				if (e.getObjectId() == req.getMainEquipIndex())
					main = e;
				if (e.getObjectId() == req.getViceEquipIndex())
					vice = e;
			}
		}
		if (main == null) {
			main = checkIsEquipment(player.getPack().getItemByGuid(req.getMainEquipIndex()));
			mainSelectByEquip = false;
		}
		if (vice == null) {
			vice = checkIsEquipment(player.getPack().getItemByGuid(req.getViceEquipIndex()));
			viceSelectByEquip = false;
		}

		if (main.getEquipmentType() != vice.getEquipmentType()) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}

		// 操作
		int mainLevel = main.getEnhanceLevel();
		int viceLevel = vice.getEnhanceLevel();
		vice.setEnhanceLevel(mainLevel);
		main.setEnhanceLevel(viceLevel);

		if (main.isBind()) { // 主装备是绑定的,那副装备也是
			vice.openState(ItemState.BIND.getMark());
		}

		if (main.hasSpecifiedTypeStat(EquipmentStatType.CREATE_SOUL_STAT)) {
			EquipmentStat stat = vice.getExtraStats().put(EquipmentStatType.CREATE_SOUL_STAT.getValue(),
					main.getExtraStats().get(EquipmentStatType.CREATE_SOUL_STAT.getValue()));
			if (stat != null) {
				main.getExtraStats().put(EquipmentStatType.CREATE_SOUL_STAT.getValue(), stat);
			} else {
				main.getExtraStats().remove(EquipmentStatType.CREATE_SOUL_STAT.getValue());
			}
		} else if (vice.hasSpecifiedTypeStat(EquipmentStatType.CREATE_SOUL_STAT)) {
			main.getExtraStats().put(EquipmentStatType.CREATE_SOUL_STAT.getValue(),
					vice.getExtraStats().get(EquipmentStatType.CREATE_SOUL_STAT.getValue()));
			vice.getExtraStats().remove(EquipmentStatType.CREATE_SOUL_STAT.getValue());
		}

		sendGemMail(player, true, main);
		sendGemMail(player, true, vice);

		ModuleInfo moduleInfo = ModuleInfo.valueOf(ModuleType.EXTENDS, SubModuleType.ENHANCE_LELVE_TRANSFER);
		long now = System.currentTimeMillis();
		if (mainSelectByEquip) {
			targetEquipStorage.markByEquipmentType(main.getEquipmentType());
			main.addEquipmentExtraStats(player, false);
			player.getGameStats().endModifiers(StatEffectId.valueOf(main.getObjectId(), StatEffectType.EQUIPMENT),
					false);
			player.getGameStats().addModifiers(StatEffectId.valueOf(main.getObjectId(), StatEffectType.EQUIPMENT),
					main.getModifiers());
		} else {
			player.getPack().markByGuid(main.getObjectId());
		}
		if (viceSelectByEquip) {
			targetEquipStorage.markByEquipmentType(vice.getEquipmentType());
			vice.addEquipmentExtraStats(player, false);
			player.getGameStats().endModifiers(StatEffectId.valueOf(vice.getObjectId(), StatEffectType.EQUIPMENT),
					false);
			player.getGameStats().addModifiers(StatEffectId.valueOf(vice.getObjectId(), StatEffectType.EQUIPMENT),
					vice.getModifiers());
		} else {
			player.getPack().markByGuid(vice.getObjectId());
		}
		// 装备强化等级变化日志
		LogManager.transferEnhanceLevelLog(player, moduleInfo, now, main.getObjectId(), main.getKey(), mainLevel,
				viceLevel, 1);
		LogManager.transferEnhanceLevelLog(player, moduleInfo, now, vice.getObjectId(), vice.getKey(), viceLevel,
				mainLevel, 0);

		if (mainSelectByEquip || viceSelectByEquip) {
			// 套装属性
			itemManager.calOnAddSuitStat(player, true);
			itemManager.calOnAddSuitStatOnOtherEquipStorage(player, true);
		}

		EventBusManager.getInstance().submit(ExtendsEquipmentEvent.valueOf(player.getObjectId()));
		PacketSendUtility.sendPacket(player, SM_Equips_Update.valueOf(player, vice.getKey(), targetEquipStorageType));
	}

	public void extendsEquipEx(Player player, long mainEquipIndex, long viceEquipIndex, boolean selectElement,
			boolean selectSoul, boolean selectRare, EquipmentStorageType storageType) {
		if (mainEquipIndex == viceEquipIndex) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}

		EquipmentStorage targetEquipStorage = storageType.getEquipmentStorage(player);
		boolean mainSelectByEquip = true, viceSelectByEquip = true;
		Equipment main = null, vice = null;
		for (Equipment e : targetEquipStorage.getEquipments()) {
			if (e != null) {
				if (e.getObjectId() == mainEquipIndex)
					main = e;
				if (e.getObjectId() == viceEquipIndex)
					vice = e;
			}
		}
		if (main == null) {
			main = checkIsEquipment(player.getPack().getItemByGuid(mainEquipIndex));
			mainSelectByEquip = false;
		}
		if (vice == null) {
			vice = checkIsEquipment(player.getPack().getItemByGuid(viceEquipIndex));
			viceSelectByEquip = false;
		}

		if (main.getEquipmentType() != vice.getEquipmentType()) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}

		// 操作
		int mainLevel = main.getEnhanceLevel();
		int viceLevel = vice.getEnhanceLevel();
		vice.setEnhanceLevel(mainLevel);
		main.setEnhanceLevel(viceLevel);

		if (main.isBind()) { // 主装备是绑定的,那副装备也是
			vice.openState(ItemState.BIND.getMark());
		}

		if (main.hasSpecifiedTypeStat(EquipmentStatType.CREATE_SOUL_STAT)) {
			EquipmentStat stat = vice.getExtraStats().put(EquipmentStatType.CREATE_SOUL_STAT.getValue(),
					main.getExtraStats().get(EquipmentStatType.CREATE_SOUL_STAT.getValue()));
			if (stat != null) {
				main.getExtraStats().put(EquipmentStatType.CREATE_SOUL_STAT.getValue(), stat);
			} else {
				main.getExtraStats().remove(EquipmentStatType.CREATE_SOUL_STAT.getValue());
			}
		} else if (vice.hasSpecifiedTypeStat(EquipmentStatType.CREATE_SOUL_STAT)) {
			main.getExtraStats().put(EquipmentStatType.CREATE_SOUL_STAT.getValue(),
					vice.getExtraStats().get(EquipmentStatType.CREATE_SOUL_STAT.getValue()));
			vice.getExtraStats().remove(EquipmentStatType.CREATE_SOUL_STAT.getValue());
		}

		ModuleInfo moduleInfo = ModuleInfo.valueOf(ModuleType.EXTENDS, SubModuleType.ENHANCE_HORSE_LELVE_TRANSFER);
		long now = System.currentTimeMillis();
		if (mainSelectByEquip) {
			targetEquipStorage.markByEquipmentType(main.getEquipmentType());
			main.addEquipmentExtraStats(player, false);
			player.getGameStats().endModifiers(StatEffectId.valueOf(main.getObjectId(), StatEffectType.EQUIPMENT),
					false);
			player.getGameStats().addModifiers(StatEffectId.valueOf(main.getObjectId(), StatEffectType.EQUIPMENT),
					main.getModifiers());
		} else {
			player.getPack().markByGuid(main.getObjectId());
		}
		if (viceSelectByEquip) {
			targetEquipStorage.markByEquipmentType(vice.getEquipmentType());
			vice.addEquipmentExtraStats(player, false);
			player.getGameStats().endModifiers(StatEffectId.valueOf(vice.getObjectId(), StatEffectType.EQUIPMENT),
					false);
			player.getGameStats().addModifiers(StatEffectId.valueOf(vice.getObjectId(), StatEffectType.EQUIPMENT),
					vice.getModifiers());
		} else {
			player.getPack().markByGuid(vice.getObjectId());
		}
		// 装备强化等级变化日志
		LogManager.transferEnhanceLevelLog(player, moduleInfo, now, main.getObjectId(), main.getKey(), mainLevel,
				viceLevel, 1);
		LogManager.transferEnhanceLevelLog(player, moduleInfo, now, vice.getObjectId(), vice.getKey(), viceLevel,
				mainLevel, 0);

		if (mainSelectByEquip || viceSelectByEquip) {
			// 套装属性
			itemManager.calOnAddSuitStat(player, true);
			itemManager.calOnAddSuitStatOnOtherEquipStorage(player, true);
		}

		EventBusManager.getInstance().submit(ExtendsEquipmentEvent.valueOf(player.getObjectId()));
		if (storageType == EquipmentStorageType.HORSE) {
			PacketSendUtility.sendPacket(player, SM_Equips_Update_Horse.valueOf(player, vice.getKey()));
		}
	}

	public void smeltEquipment(Player player, CM_SmeltEquipment req) {
		player.getEquipmentStorage().refresh();

		ItemStorage storage = null;
		if (req.getIsTreasureWare() == 0) {
			storage = player.getPack();
		} else if (req.getIsTreasureWare() == 1) {
			storage = player.getTreasureWareHouse();
		}

		double vipExtra = player.getVip().getResource().getSmeltExpExtra() / 10000.0;
		double militaryExtra = player.getMilitary().getResource().getSmeltAddition() / 10000.0;
		long vipAddValue = 0L;

		long totalAddValue = 0;
		int code = 0;
		int totalGodStoneFragment = 0;
		Reward godStoneReward = Reward.valueOf();
		Reward soulUpgradeReward = Reward.valueOf();
		Map<Integer, SmeltVO> map = New.hashMap();
		for (Integer index : req.getIndexs()) {
			AbstractItem equip = storage.getItemByIndex(index);
			Equipment e = checkIsEquipment(equip);
			int inc = ItemConfig.getInstance().calcSmeltEquipmentExp(e);
			int vipResult = (int) Math.round(inc * vipExtra);
			int militaryResult = (int) Math.round(inc * militaryExtra);
			int result = inc + vipResult + militaryResult;
			if (player.getEquipmentStorage().getSmeltExp() + totalAddValue + result > itemManager.SMELT_DALIYEXP_LIMIT
					.getValue()) {
				code = ManagedErrorCode.DAILY_EXP_LIMIT;
				break;
			}
			vipAddValue += vipResult;
			totalAddValue += result;
			ItemShow show = new ItemShow();
			show.setKey(e.getKey());
			show.setOwner(player.getName());
			show.setItem(e);
			map.put(index, SmeltVO.valueOf(show, result));

			if (e.hasSpecifiedTypeStat(EquipmentStatType.GOD_STAT)) {
				for (String godId : e.getExtraStats().get(EquipmentStatType.GOD_STAT.getValue()).getContext()) {
					EquipmentGodResource resource = itemManager.equipmentGodResources.get(godId, true);
					godStoneReward.addReward(rewardManager.creatReward(player, resource.getSmeltRewardId(), null));
				}
			} else if (e.hasSpecifiedTypeStat(EquipmentStatType.SOUL_STAT)) {
				totalGodStoneFragment += ItemConfig.getInstance().calcEquipmentStoneFragment(player, e);
			}

			if (e.hasSpecifiedTypeStat(EquipmentStatType.SUICIDE_TURN)) {
				for (String id : e.getExtraStats().get(EquipmentStatType.SUICIDE_TURN.getValue()).getContext()) {
					EquipmentTurnResource resource = itemManager.equipTurnResources.get(id, true);
					godStoneReward.addReward(rewardManager.creatReward(player, resource.getSmeltRewardId(), null));
				}
			}

			if (e.hasSpecifiedTypeStat(EquipmentStatType.SUIT_STAT)) {
				godStoneReward.addReward(ItemConfig.getInstance().smeltNormalSuitEquipmentBack(player, e));
			}

			if (e.hasSpecifiedTypeStat(EquipmentStatType.SOUL_STAT)) {
				String soulKey = e.getExtraIDs(EquipmentStatType.SOUL_STAT).get(0);
				EquipmentSoulResource soulResource = itemManager.getEquipmentSoulReousrce(soulKey);
				int preSoulStatLevel = soulResource.getSoulStatLevel() - 1;
				if (preSoulStatLevel >= ItemConfig.getInstance().SOUL_UPGRADE_MIN_LEVEL.getValue()
						&& preSoulStatLevel < ItemConfig.getInstance().SOUL_MAX_LEVEL.getValue()) {
					String preSoulKey = itemManager.getSoulIndexKey(soulResource.getEquipmentType(), preSoulStatLevel,
							soulResource.getStatType());
					EquipmentSoulResource preSoulResource = itemManager.getEquipmentSoulReousrce(preSoulKey);
					Reward soulUp = Reward.valueOf().addItem(
							ItemConfig.getInstance().STONE_OF_SOUL_ITEM_KEY.getValue(),
							ItemConfig.getInstance().getUpgradeSoulLevelItemActNum(player, e.getEquipmentType(),
									preSoulResource));
					soulUpgradeReward.addReward(soulUp);
				}
			}

			sendGemMail(player, false, e);
			LogManager.addItemLog(player, System.currentTimeMillis(), ModuleInfo.valueOf(ModuleType.SMELT,
					SubModuleType.SMELT_EQUIP_REWARD), -1, e.getSize(), e, player.getPack()
					.getItemSizeByKey(e.getKey()));
		}

		for (Integer index : map.keySet()) {
			storage.removeItemByIndex(index);
		}
		player.getEquipmentStorage().addSmeltExp(totalAddValue);

		while (totalAddValue > 0) {
			if (totalAddValue > Integer.MAX_VALUE) {
				rewardManager.grantReward(player, Reward.valueOf().addExp(Integer.MAX_VALUE),
						ModuleInfo.valueOf(ModuleType.SMELT, SubModuleType.SMELT_EQUIP_REWARD));
				totalAddValue -= Integer.MAX_VALUE;
			} else {
				rewardManager.grantReward(player, Reward.valueOf().addExp((int) totalAddValue),
						ModuleInfo.valueOf(ModuleType.SMELT, SubModuleType.SMELT_EQUIP_REWARD));
				break;
			}
		}

		if (totalGodStoneFragment != 0) {
			godStoneReward.addItem(ItemConfig.getInstance().STONE_FRAGMENT_OF_GOD_ITEMKEY.getValue(),
					totalGodStoneFragment);
		}
		if (!godStoneReward.isEmpty()) {
			for (RewardItem ri : godStoneReward.getItemsByType(RewardType.ITEM)) {
				ri.putParms(ItemState.BIND.name(), "1");
			}
			Mail mail = Mail.valueOf(I18nUtils.valueOf(ItemConfig.getInstance().SMELT_STONE_MAIL_TITLE.getValue()),
					I18nUtils.valueOf(ItemConfig.getInstance().SMELT_STONE_MAIL_CONTENT.getValue()), null,
					godStoneReward);
			MailManager.getInstance().sendMail(mail, player.getObjectId());
		}
		if (!soulUpgradeReward.isEmpty()) {
			for (RewardItem ri : soulUpgradeReward.getItemsByType(RewardType.ITEM)) {
				ri.putParms(ItemState.BIND.name(), "1");
			}
			Mail mail = Mail.valueOf(
					I18nUtils.valueOf(ItemConfig.getInstance().SOUL_SMELT_STONE_MAIL_TITLE.getValue()),
					I18nUtils.valueOf(ItemConfig.getInstance().SOUL_SMELT_STONE_MAIL_CONTENT.getValue()), null,
					soulUpgradeReward);
			MailManager.getInstance().sendMail(mail, player.getObjectId());
		}
		PacketSendUtility.sendPacket(player,
				SM_SmeltEquipment.valueOf(req.getIsTreasureWare(), storage, code, totalAddValue, map));
		EventBusManager.getInstance().submit(SmeltEquipmentEvent.valueOf(player));
		I18nUtils utils = I18nUtils.valueOf("202001");
		utils.addParm("addValue", I18nPack.valueOf(String.valueOf(totalAddValue)));
		utils.addParm("vipaddValue", I18nPack.valueOf(String.valueOf(vipAddValue)));
		ChatManager.getInstance().sendSystem(41007, utils, null, player.getObjectId(), player.getObjectId());

	}

	public void smeltEquipmentEx(Player player, HashSet<Integer> indexs, int isTreasureWare,
			EquipmentStorageType equipStorageType) {
		EquipmentStorage targetEquipStorage = equipStorageType.getEquipmentStorage(player);
		targetEquipStorage.refresh();
		ItemStorage storage = null;
		if (isTreasureWare == 0) {
			storage = player.getPack();
		} else if (isTreasureWare == 1) {
			storage = player.getTreasureWareHouse();
		}

		double vipExtra = player.getVip().getResource().getSmeltExpExtra() / 10000.0;
		double militaryExtra = player.getMilitary().getResource().getSmeltAddition() / 10000.0;
		long vipAddValue = 0L;

		long totalAddValue = 0;
		int code = 0;
		int totalGodStoneFragment = 0;
		Reward godStoneReward = Reward.valueOf();
		Reward soulUpgradeReward = Reward.valueOf();
		Map<Integer, SmeltVO> map = New.hashMap();
		for (Integer index : indexs) {
			AbstractItem equip = storage.getItemByIndex(index);
			Equipment e = checkIsEquipment(equip);
			int inc = ItemConfig.getInstance().calcSmeltEquipmentExp(e);
			int vipResult = (int) Math.round(inc * vipExtra);
			int militaryResult = (int) Math.round(inc * militaryExtra);
			int result = inc + vipResult + militaryResult;
			if (targetEquipStorage.getSmeltExp() + totalAddValue + result > itemManager.SMELT_DALIYEXP_LIMIT.getValue()) {
				code = ManagedErrorCode.DAILY_EXP_LIMIT;
				break;
			}
			vipAddValue += vipResult;
			totalAddValue += result;
			ItemShow show = new ItemShow();
			show.setKey(e.getKey());
			show.setOwner(player.getName());
			show.setItem(e);
			map.put(index, SmeltVO.valueOf(show, result));

			if (e.hasSpecifiedTypeStat(EquipmentStatType.GOD_STAT)) {
				for (String godId : e.getExtraStats().get(EquipmentStatType.GOD_STAT.getValue()).getContext()) {
					EquipmentGodResource resource = itemManager.equipmentGodResources.get(godId, true);
					godStoneReward.addReward(rewardManager.creatReward(player, resource.getSmeltRewardId(), null));
				}
			} else if (e.hasSpecifiedTypeStat(EquipmentStatType.SOUL_STAT)) {
				totalGodStoneFragment += ItemConfig.getInstance().calcEquipmentStoneFragment(player, e);
			}

			if (e.hasSpecifiedTypeStat(EquipmentStatType.SUIT_STAT)) {
				godStoneReward.addReward(ItemConfig.getInstance().smeltNormalSuitEquipmentBack(player, e));
			}

			if (e.hasSpecifiedTypeStat(EquipmentStatType.SOUL_STAT)) {
				String soulKey = e.getExtraIDs(EquipmentStatType.SOUL_STAT).get(0);
				EquipmentSoulResource soulResource = itemManager.getEquipmentSoulReousrce(soulKey);
				int preSoulStatLevel = soulResource.getSoulStatLevel() - 1;
				if (preSoulStatLevel >= ItemConfig.getInstance().SOUL_UPGRADE_MIN_LEVEL.getValue()
						&& preSoulStatLevel < ItemConfig.getInstance().SOUL_MAX_LEVEL.getValue()) {
					String preSoulKey = itemManager.getSoulIndexKey(soulResource.getEquipmentType(), preSoulStatLevel,
							soulResource.getStatType());
					EquipmentSoulResource preSoulResource = itemManager.getEquipmentSoulReousrce(preSoulKey);
					Reward soulUp = Reward.valueOf().addItem(
							ItemConfig.getInstance().STONE_OF_SOUL_ITEM_KEY.getValue(),
							ItemConfig.getInstance().getUpgradeSoulLevelItemActNum(player, e.getEquipmentType(),
									preSoulResource));
					soulUpgradeReward.addReward(soulUp);
				}
			}

			sendGemMail(player, false, e);
			LogManager.addItemLog(player, System.currentTimeMillis(), ModuleInfo.valueOf(ModuleType.SMELT,
					SubModuleType.SMELT_EQUIP_REWARD), -1, e.getSize(), e, player.getPack()
					.getItemSizeByKey(e.getKey()));
		}

		for (Integer index : map.keySet()) {
			storage.removeItemByIndex(index);
		}
		targetEquipStorage.addSmeltExp(totalAddValue);

		while (totalAddValue > 0) {
			if (totalAddValue > Integer.MAX_VALUE) {
				rewardManager.grantReward(player, Reward.valueOf().addExp(Integer.MAX_VALUE),
						ModuleInfo.valueOf(ModuleType.SMELT, SubModuleType.SMELT_EQUIP_REWARD));
				totalAddValue -= Integer.MAX_VALUE;
			} else {
				rewardManager.grantReward(player, Reward.valueOf().addExp((int) totalAddValue),
						ModuleInfo.valueOf(ModuleType.SMELT, SubModuleType.SMELT_EQUIP_REWARD));
				break;
			}
		}

		if (totalGodStoneFragment != 0) {
			godStoneReward.addItem(ItemConfig.getInstance().STONE_FRAGMENT_OF_GOD_ITEMKEY.getValue(),
					totalGodStoneFragment);
		}
		if (!godStoneReward.isEmpty()) {
			for (RewardItem ri : godStoneReward.getItemsByType(RewardType.ITEM)) {
				ri.putParms(ItemState.BIND.name(), "1");
			}
			Mail mail = Mail.valueOf(I18nUtils.valueOf(ItemConfig.getInstance().SMELT_STONE_MAIL_TITLE.getValue()),
					I18nUtils.valueOf(ItemConfig.getInstance().SMELT_STONE_MAIL_CONTENT.getValue()), null,
					godStoneReward);
			MailManager.getInstance().sendMail(mail, player.getObjectId());
		}
		if (!soulUpgradeReward.isEmpty()) {
			for (RewardItem ri : soulUpgradeReward.getItemsByType(RewardType.ITEM)) {
				ri.putParms(ItemState.BIND.name(), "1");
			}
			Mail mail = Mail.valueOf(
					I18nUtils.valueOf(ItemConfig.getInstance().SOUL_SMELT_STONE_MAIL_TITLE.getValue()),
					I18nUtils.valueOf(ItemConfig.getInstance().SOUL_SMELT_STONE_MAIL_CONTENT.getValue()), null,
					soulUpgradeReward);
			MailManager.getInstance().sendMail(mail, player.getObjectId());
		}

		if (equipStorageType == EquipmentStorageType.HORSE) {
			PacketSendUtility.sendPacket(player,
					SM_SmeltEquipment_Horse.valueOf(isTreasureWare, storage, code, totalAddValue, map));
			EventBusManager.getInstance().submit(SmeltEquipmentEvent.valueOf(player));
		}
		I18nUtils utils = I18nUtils.valueOf("202001");
		utils.addParm("addValue", I18nPack.valueOf(String.valueOf(totalAddValue)));
		utils.addParm("vipaddValue", I18nPack.valueOf(String.valueOf(vipAddValue)));
		ChatManager.getInstance().sendSystem(41007, utils, null, player.getObjectId(), player.getObjectId());

	}

	public void resetEquipElement(Player player, CM_EquipReElement req) {
		Equipment equip = null;
		boolean selectFromEquip = true;
		for (Equipment e : player.getEquipmentStorage().getEquipments()) {
			if (e != null && e.getObjectId() == req.getObjId()) {
				equip = e;
				break;
			}
		}
		if (equip == null) {
			equip = checkIsEquipment(player.getPack().getItemByGuid(req.getObjId()));
			selectFromEquip = false;
		}

		if (equip.getElement() == 0) {
			throw new ManagedException(ManagedErrorCode.EQUIP_ELEMENT_NONE);
		}

		CoreActions copperActions = CoreActionManager.getInstance().getCoreActions(
				ItemConfig.getInstance().ELEMENT_RESET_ACTIONIDS_COPPER.getValue());
		copperActions.verify(player, true);

		CoreActions actions = new CoreActions();
		CoreActions priActions = CoreActionManager.getInstance().getCoreActions(
				ItemConfig.getInstance().ELEMENT_RESET_ACTIONIDS.getValue());
		if (priActions.verify(player)) {
			actions.addActions(priActions);
		} else {
			if (!req.isGold()) {
				throw new ManagedException(ManagedErrorCode.NOT_ENOUGH_ITEM);
			}
			actions.addActions(CoreActionManager.getInstance().getCoreActions(
					ItemConfig.getInstance().ELEMENT_RESET_GOLDACTIONIDS.getValue()));
		}

		ModuleInfo moduleInfo = ModuleInfo.valueOf(ModuleType.RESET, SubModuleType.RESET_EQUIP_ELEMENT);
		actions.addActions(copperActions);
		actions.verify(player, true);
		actions.act(player, moduleInfo);

		int value = Integer.valueOf(ChooserManager.getInstance()
				.chooseValueByRequire(player, ItemConfig.getInstance().ELEMENT_CHOOSERID.getValue()).get(0));
		equip.setElement(EquipmentElementType.valueOf(value).getValue());

		// 选择的是装备栏里面的, 造成属性的变化
		if (selectFromEquip) {
			itemManager.soulActivateChange(player, equip.getEquipmentType(), EquipmentStorageType.PLAYER);
			itemManager.soulSelfActivateChange(player, equip.getEquipmentType(), EquipmentStorageType.PLAYER);
			itemManager.calculateSoulSuit(player, false);
			player.getGameStats().recomputeStats();
		} else {
			player.getPack().markByGuid(req.getObjId());
			PacketSendUtility.sendPacket(player, SM_Packet_Update.valueOf(player));
		}
		EventBusManager.getInstance().submit(EquipmentReElementEvent.valueOf(player));
		PacketSendUtility.sendPacket(player, SM_EquipReElement.valueOf(equip));

		// 装备五形变化
		LogManager.addItemLog(player, System.currentTimeMillis(), moduleInfo, 0, 1, equip, player.getPack()
				.getItemSizeByKey(equip.getKey()));
	}

	private Equipment checkIsEquipment(AbstractItem equip) {
		if (equip == null || (!(equip instanceof Equipment))) {
			throw new ManagedException(ManagedErrorCode.NOT_EQUIPMENT);
		}
		return (Equipment) equip;
	}

	/** 合成东西 */
	public void combiningItem(Player player, String combiningId, int addition, boolean useGold, int quantity) {
		if (quantity < 1 || quantity > 9999) {
			throw new ManagedException(ManagedErrorCode.INPUT_NUM_ILLEGAL);
		}
		CombiningResource resource = itemManager.getCombiningResource(combiningId);

		CoreConditions conditions = resource.getCondition();
		if (!conditions.verify(player, true)) {
			PacketSendUtility.sendErrorMessage(player);
			return;
		}

		CoreActions actions = resource.getActions(quantity);
		if (!actions.verify(player, true)) {
			PacketSendUtility.sendErrorMessage(player);
			return;
		}

		Set<String> useItems = actions.getAllItemKeys();
		boolean bindItem = false;
		for (String itemKey : useItems) {
			for (AbstractItem item : player.getPack().getItemsByKey(itemKey)) {
				if (item.isBind()) {
					bindItem = true;
				}
			}
		}

		int rate = resource.getSuccessRate();
		if (rate != 10000) {
			if (addition > 0) {
				CoreActions helperAct = CoreActionManager.getInstance().getCoreActions(addition * quantity,
						resource.getAssistantAct());
				if (!helperAct.verify(player, false)) {
					if (!useGold) {
						throw new ManagedException(ManagedErrorCode.ERROR_MSG);
					}
					CoreActions helperGoldAct = CoreActionManager.getInstance().getCoreActions(addition * quantity,
							resource.getAssistantGoldAct());
					helperGoldAct.verify(player, true);
					actions.addActions(helperGoldAct);
					rate = rate + (resource.getAddRate() * addition);
				}
			}
		}
		double resultRate = rate / 10000.0;
		actions.act(player, ModuleInfo.valueOf(ModuleType.COMBINING_ITEM, SubModuleType.COMBINING_ACT));
		if (RandomUtils.isHit(resultRate)) {
			ArrayList<String> rewardIds = New.arrayList();
			for (String s : resource.getRewardId()) {
				while (quantity-- > 0) {
					rewardIds.add(s);
				}
			}
			Reward reward = RewardManager.getInstance().creatReward(player, rewardIds, null);
			if (bindItem) {
				for (RewardItem ri : reward.getItemsByType(RewardType.ITEM)) {
					ri.putParms(ItemState.BIND.name(), "1");
				}
			}
			RewardManager.getInstance().grantReward(player, reward,
					ModuleInfo.valueOf(ModuleType.COMBINING_ITEM, SubModuleType.COMBINING_REWARD));
			PacketSendUtility.sendPacket(player, SM_Combining.valueOf(0));
		} else {
			PacketSendUtility.sendPacket(player, SM_Combining.valueOf(1));
		}
	}

	public void combiningItemEx(Player player, String combingId, int packIndex) {
		CombingExResource resource = itemManager.combingExResources.get(combingId, true);

		CoreConditions conditions = resource.getConditions();
		if (!conditions.verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}

		AbstractItem packItem = player.getPack().getItemByIndex(packIndex);
		if (packItem == null) {
			throw new ManagedException(ManagedErrorCode.COMBING_PACK_ITEM_NOT_EXIST);
		}

		if (!resource.isInMaterialItemId(packItem.getKey())) {
			throw new ManagedException(ManagedErrorCode.COMBING_NOT_IN_TARGET_MATERIAL_ITEM);
		}

		String usePackItemId = packItem.getKey();
		ItemResource itemResource = itemManager.getResource(usePackItemId);
		Equipment currentEquip = player.getEquipmentStorage().getEquip(itemResource.getEquipmentType());
		if (currentEquip == null) {
			throw new ManagedException(ManagedErrorCode.COMBING_WITHOUT_TARGET_EQUIPMENT_TYPE_MAINITEM);
		}

		ItemResource mainItemResource = itemManager.getResource(currentEquip.getKey());
		if (mainItemResource.getSpecialType() != resource.getSpecialType()) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}

		CombingExResource maxGradeResource = itemManager.combingExResources.getIndex(CombingExResource.GROUP_INDEX,
				resource.getGroup()).get(0);

		if (currentEquip.getGrade() >= maxGradeResource.getGrade() + 1) {
			throw new ManagedException(ManagedErrorCode.COMBING_ITEM_MAX_GRADE);
		}

		double rate = resource.getSuccessRate() / 10000.0;
		if (RandomUtils.isHit(rate)) {
			player.getEquipmentStorage().unEquip(itemResource.getEquipmentType());
			checkIsEquipment(packItem);
			Reward gemReward = Reward.valueOf();
			Reward packItemGem = getGemReward(player, (Equipment) packItem);
			if (packItemGem != null) {
				gemReward.addReward(packItemGem);
			}

			player.getPack().removeItemByIndex(packIndex);
			Reward currentEquipGem = getGemReward(player, currentEquip);

			if (currentEquipGem != null) {
				gemReward.addReward(currentEquipGem);
			}

			if (!gemReward.isEmpty()) {
				I18nUtils titel18n = I18nUtils.valueOf(itemManager.COMBING_EX_GEM_MAIL_TITLE.getValue());
				I18nUtils contextl18n = I18nUtils.valueOf(itemManager.COMBING_EX_GEM_MAIL_CONTENT.getValue());
				Mail mail = Mail.valueOf(titel18n, contextl18n, null, gemReward);
				MailManager.getInstance().sendMail(mail, player.getObjectId());
			}

			PacketSendUtility
					.broadcastPacket(player, SM_UnEquip_Change.valueOf(player.getObjectId(), currentEquip.getKey(),
							EquipmentStorageType.PLAYER));
			// 更新属性
			player.getGameStats().endModifiers(
					StatEffectId.valueOf(currentEquip.getObjectId(), StatEffectType.EQUIPMENT), false);
			currentEquip.endEquipmentExtraStats(player, false);
			currentEquip.setSoulType(0);
			// 影响到其他的装备
			itemManager.soulActivateChange(player, currentEquip.getEquipmentType(), EquipmentStorageType.PLAYER);
			// 套装属性
			itemManager.calOnAddSuitStat(player, false);
			player.getGameStats().recomputeStats();

			EventBusManager.getInstance().submit(UnEquipEquipmentEvent.valueOf(player));

			Map<String, String> context = new HashMap<String, String>();

			context.put(ItemState.BIND.name(), "1");

			if (currentEquip.hasSpecifiedTypeStat(EquipmentStatType.GOD_STAT)) {
				context.put(EquipmentStatType.GOD_STAT.name(),
						currentEquip.getExtraStats().get(EquipmentStatType.GOD_STAT.getValue()).getContext().get(0));
			}

			if (currentEquip.hasSpecifiedTypeStat(EquipmentStatType.SOUL_STAT)) {
				List<String> extraIds = currentEquip.getExtraIDs(EquipmentStatType.SOUL_STAT);
				String ids = StringUtils.join(extraIds, "|");
				context.put(EquipmentStatType.SOUL_STAT.name(), ids);
			}

			if (currentEquip.hasSpecifiedTypeStat(EquipmentStatType.SUICIDE_TURN)) {
				context.put(EquipmentStatType.SUICIDE_TURN.name(),
						currentEquip.getExtraStats().get(EquipmentStatType.SUICIDE_TURN.getValue()).getContext().get(0));
			}

			context.put("ENHANCE_LEVEL", currentEquip.getEnhanceLevel() + "");

			context.put(EquipmentStatType.COMMON_STAT.name(), combingId);

			int newGrade = currentEquip.getGrade() + 1;
			context.put("GRADE", newGrade + "");

			Reward targetItemReward = Reward.valueOf();
			targetItemReward.addEquipment(resource.getTargetItemId(), 1, context);
			rewardManager.grantReward(player, targetItemReward,
					ModuleInfo.valueOf(ModuleType.COMBINING_EX, SubModuleType.COMBING_EX_REWARD));
			PacketSendUtility.sendPacket(player, SM_Combing_Ex.valueOf(player.getPack().collectUpdate(), player
					.getEquipmentStorage().collectUpdate()));

		} else {
			// 失败
			PacketSendUtility.sendPacket(player, SM_Combing_Ex.valueOf());
		}

	}

	private Reward getGemReward(Player player, Equipment equip) {
		if (!equip.hasSpecifiedTypeStat(EquipmentStatType.GEM_STAT)) {
			return null;
		}

		List<String> gemIds = New.arrayList();
		for (String itemId : equip.getExtraStats().get(EquipmentStatType.GEM_STAT.getValue()).getContext()) {
			if (itemId == null || itemId.equals(EquipmentStat.GEM_NOT_EXIST)) {
				continue;
			}
			gemIds.add(itemId);
		}
		if (gemIds.isEmpty()) {
			return null;
		}
		Reward reward = Reward.valueOf();
		for (String itemKey : gemIds) {
			reward.addItem(itemKey, 1);
		}
		for (RewardItem ri : reward.getItemsByType(RewardType.ITEM)) {
			ri.putParms(ItemState.BIND.name(), "1");
		}
		return reward;
	}

	@Override
	public void superForge(Player player, CM_Super_Forge_Equip req) {
		if (!ModuleOpenManager.getInstance().isOpenByKey(player, "opmk67")) {
			throw new ManagedException(ManagedErrorCode.MODULE_NOT_OPEN);
		}
		String key = req.getEquipKey();
		String soulKey = req.getEquipSoulKey();
		String element = req.getElementType();
		// Double elementFactor =
		// itemManager.ELEMENT_FACTOR_MAP.getValue().get(element);
		if (key == null) {
			PacketSendUtility.sendErrorMessage(player);
			return;
		}
		if (soulKey == null) {
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.TARGET_SOUL_STAT_NOT_EXIST);
			return;
		}
		ItemResource resource = itemManager.getResource(key);
		EquipmentSoulResource soulResource = itemManager.getEquipmentSoulReousrce(soulKey);

		if (soulResource.getSoulStatLevel() < ItemConfig.getInstance().SOUL_MIN.getValue()) {
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.TARGET_SOUL_STAT_NOT_EXIST);
			return;
		}

		Double qualityFactor = itemManager.QUALITY_FACTOR_MAP.getValue().get(resource.getQuality() + "");
		Double levelFactor = itemManager.LEVEL_FACTOR_MAP.getValue().get(resource.getLevel() + "");
		Double equipTypeFactor = itemManager.EQUIPTYPE_FACTOR_MAP.getValue().get(resource.getEquipmentType().name());

		Integer gold = (Integer) FormulaParmsUtil.valueOf(itemManager.SUPER_FORGE_GOLD).addParm("LEVEL", levelFactor)
				.addParm("EQUIPTYPE", equipTypeFactor).addParm("QUALITY", qualityFactor)
				.addParm("SOUL", soulResource.getSoulGoldFactor())
				.addParm("SOULTYPE", soulResource.getSoulTypefactor()).getValue();
		CurrencyAction actions = CoreActionType.createCurrencyCondition(CurrencyType.GOLD, gold);
		actions.verify(player);
		actions.act(player, ModuleInfo.valueOf(ModuleType.FORGE, SubModuleType.SUPER_FORGE_ACT));

		Equipment constructImage = itemManager.createItem(key); // 仅仅只是用来传给前端显示用的
		Map<String, String> context = New.hashMap();
		context.put(EquipmentStatType.SOUL_STAT.name(), soulKey);
		constructImage.addExtraStats(EquipmentStatType.SOUL_STAT.getValue(), soulKey);

		ModuleInfo moduleInfo = ModuleInfo.valueOf(ModuleType.FORGE, SubModuleType.SUPER_FORGE_REWARD);
		rewardManager.grantReward(player, Reward.valueOf().addEquipment(key, 1, context), moduleInfo);
		PacketSendUtility.sendPacket(player, new SM_Super_Forge_Equip());

		ItemShow show = new ItemShow();
		show.setKey(constructImage.getKey());
		show.setOwner(player.getName());
		show.setItem(constructImage);
		I18nUtils utils = I18nUtils.valueOf("301008")
				.addParm(I18NparamKey.PLAYERNAME, I18nPack.valueOf(player.getName()))
				.addParm(I18NparamKey.COUNTRY, I18nPack.valueOf(player.getCountry().getName()))
				.addParm(I18NparamKey.EQUIPMENT_KEY, I18nPack.valueOf(show));
		ChatManager.getInstance().sendSystem(0, utils, null);

		I18nUtils systemUtils = I18nUtils.valueOf("20103")
				.addParm(I18NparamKey.PLAYERNAME, I18nPack.valueOf(player.getName()))
				.addParm(I18NparamKey.COUNTRY, I18nPack.valueOf(player.getCountry().getName()))
				.addParm(I18NparamKey.EQUIPMENT_KEY, I18nPack.valueOf(itemManager.getResource(key).getName()));
		ChatManager.getInstance().sendSystem(11001, systemUtils, null);

		LogManager.addForgeLog(player.getPlayerEnt().getServer(), player.getPlayerEnt().getAccountName(),
				player.getName(), player.getObjectId(), System.currentTimeMillis(), constructImage.getKey(), element,
				soulKey);
	}

	@Override
	public void seekTreasure(Player player, String id) {
		TreasureResource resource = itemManager.treasureResource.get(id, true);

		TreasureType treasureType = resource.getTreasureType();

		String moduleId = itemManager.TREASURE_MODULE_OPEN_IDS.getValue().get(treasureType.name());
		if (!moduleOpenManager.isOpenByKey(player, moduleId)) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}

		if (player.getTreasureWareHouse().getEmptySize() < resource.getCount()) {
			// 探宝仓库空间不足
			throw new ManagedException(ManagedErrorCode.TREASURE_STORAGE_PACK_SIZE_NOT_ENOUGH);
		}
		// 当前拥有的道具数
		int itemHaveCount = 0;
		if (resource.getItemPriorityActs() != null && resource.getItemPriorityActs().length != 0) {
			String itemId = CoreActionManager.getInstance().getCoreActions(1, resource.getItemPriorityActs())
					.getFirstItemKey();
			itemHaveCount = (int) player.getPack().getItemSizeByKey(itemId);
		}
		Map<String, String> params = New.hashMap();
		boolean itemSeek = false;
		if (resource.getItemCount() <= itemHaveCount && resource.getItemCount() != 0) {
			// 优先消耗道具
			CoreActions actions = CoreActionManager.getInstance().getCoreActions(resource.getItemCount(),
					resource.getItemPriorityActs());
			actions.verify(player, true);
			actions.act(
					player,
					ModuleInfo.valueOf(ModuleType.TREASURE,
							TreasureType.getActSubType(treasureType, resource.getCount())));
			params.put(ItemState.BIND.name(), "1");
			itemSeek = true;
		} else {
			CoreActions actions = new CoreActions();
			if (itemHaveCount > 0) {
				actions.addActions(coreActionManager.getCoreActions(itemHaveCount, resource.getItemPriorityActs()));
			}
			int needExtraCount = resource.getItemCount() - itemHaveCount;
			if (needExtraCount >= 0) {
				actions.addActions(coreActionManager.getCoreActions(needExtraCount, resource.getGoldActs()));
			}
			actions.verify(player, true);
			actions.act(
					player,
					ModuleInfo.valueOf(ModuleType.TREASURE,
							TreasureType.getActSubType(treasureType, resource.getGrade())));
		}
		String choserId = itemSeek == true ? resource.getRewardItemChooserGroupId() : resource
				.getRewardChooserGroupId();
		Reward rewards = Reward.valueOf();
		for (int i = 1; i <= resource.getCount(); i++) {
			List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(player, choserId);
			rewards.addRewardNotMerge(rewardManager.creatReward(player, rewardIds, null));
			player.getTreasureWareHouse().addCount(treasureType, 1);
		}
		if (!params.isEmpty()) {
			// 使用探宝钥匙产生道具是绑定的
			for (RewardItem item : rewards.getItems()) {
				item.getParms().putAll(params);
			}
		}
		rewardManager.grantReward(
				player,
				rewards,
				ModuleInfo.valueOf(ModuleType.TREASURE,
						TreasureType.getRewardSubType(treasureType, resource.getGrade())));
		PacketSendUtility.sendPacket(player, SM_Treasure_Storage_Update.valueOf(player));
		treasureHistoryEntity.getTreasureHistory().addPlayerHistory(player, rewards);
		EventBusManager.getInstance().submit(TreasureEvent.valueOf(player.getObjectId(), resource.getCount()));
	}

	@Override
	public void queryAllTreasureHistory(Player player) {
		PacketSendUtility.sendPacket(player,
				SM_Treasure_All_History.valueOf(treasureHistoryEntity.getTreasureHistory().getPlayerHistory()));
	}

	@Override
	public void takeFromTreasureStorage(Player player, int index) {
		if (player == null) {
			throw new ManagedException(ManagedErrorCode.NO_ROLE);
		}

		if (player.isTrading()) {
			throw new ManagedException(ManagedErrorCode.IN_TRADING_ERROR);
		}

		ItemStorage pack = player.getPack();
		TreasureItemStorage treasureWareHouse = player.getTreasureWareHouse();

		if (pack.isFull()) {
			throw new ManagedException(ManagedErrorCode.PACK_FULL);
		}

		AbstractItem item = treasureWareHouse.getItemByIndex(index);
		exchange(treasureWareHouse, pack, index);

		PacketSendUtility.sendPacket(player, SM_Packet_Update.valueOf(player));
		PacketSendUtility.sendPacket(player, SM_Treasure_Storage_Update.valueOf(player));
		EventBusManager.getInstance().submit(CollectItemsEvent.valueOf(player, Arrays.asList(item)));
	}

	@Override
	public void takeAllFromTreasureStorage(Player player) {
		if (player == null) {
			throw new ManagedException(ManagedErrorCode.NO_ROLE);
		}

		if (player.isTrading()) {
			throw new ManagedException(ManagedErrorCode.IN_TRADING_ERROR);
		}

		ItemStorage pack = player.getPack();
		TreasureItemStorage treasureWareHouse = player.getTreasureWareHouse();

		if (pack.isFull()) {
			throw new ManagedException(ManagedErrorCode.PACK_FULL);
		}
		int emptySize = pack.getEmptySize();
		List<AbstractItem> addItems = new LinkedList<AbstractItem>();
		for (int i = 0; i < treasureWareHouse.getSize() && emptySize > 0; i++) {
			AbstractItem item = treasureWareHouse.getItemByIndex(i);
			if (item != null) {
				addItems.add(item);
				exchange(treasureWareHouse, pack, i);
				emptySize--;
			}
		}
		PacketSendUtility.sendPacket(player, SM_Packet_Update.valueOf(player));
		PacketSendUtility.sendPacket(player, SM_Treasure_Storage_Update.valueOf(player));
		EventBusManager.getInstance().submit(CollectItemsEvent.valueOf(player, addItems));
	}

	@Override
	public SM_Equip_Create_Soul equipCreateSoul(Player player, long objId, String itemKey) {
		Equipment equip = player.getEquipmentStorage().getByGuid(objId);
		if (equip == null) {
			throw new ManagedException(ManagedErrorCode.NOT_EQUIPMENT);
		}
		EquipmentCreateSoulResource res = itemManager.getCreateSoulResourceByItemKey(itemKey);
		/*
		 * if (res.getEquipmentType() != equip.getEquipmentType()) { throw new
		 * ManagedException(ManagedErrorCode.ERROR_MSG); }
		 */

		if (!res.getEquipCreateSoulCoreActions().verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.NOT_ENOUGH_ITEM);
		}
		res.getEquipCreateSoulCoreActions().act(player,
				ModuleInfo.valueOf(ModuleType.CREATE_EQUIP_SOUL, SubModuleType.CREATE_EQUIPMENT_SOUL));

		equip.getExtraStats().put(EquipmentStatType.CREATE_SOUL_STAT.getValue(),
				EquipmentStat.valueOf(EquipmentStatType.CREATE_SOUL_STAT.getValue(), res.getId()));
		equip.addEquipmentExtraStats(player, true);
		player.getEquipmentStorage().markByEquipmentType(equip.getEquipmentType());

		ItemResource itemRes = itemManager.getResource(itemKey);

		if (itemRes.getQuality() >= ItemResource.ORANGE) {
			I18nUtils utils = I18nUtils.valueOf("20303");
			utils.addParm("name", I18nPack.valueOf(player.getName()));
			utils.addParm("country", I18nPack.valueOf(player.getCountry().getName()));
			utils.addParm("item", I18nPack.valueOf(itemRes.getName()));
			ChatManager.getInstance().sendSystem(11001, utils, null);
			I18nUtils chatUtils = I18nUtils.valueOf("309203");
			chatUtils.addParm("name", I18nPack.valueOf(player.getName()));
			chatUtils.addParm("country", I18nPack.valueOf(player.getCountry().getName()));
			chatUtils.addParm("item", I18nPack.valueOf(itemRes.getName()));
			ChatManager.getInstance().sendSystem(0, chatUtils, null);
		}
		return SM_Equip_Create_Soul.valueOf(player);
	}

	@Override
	public SM_Combining_CreateSoul combiningCreateSoul(Player player, String createSoulKey, int count) {
		if (count < 1 || count > 9999) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}

		EquipmentCreateSoulResource res = itemManager.getCreateSoulResource(createSoulKey);
		CoreActions actions = res.getCombiningCoreActions(count);
		actions.verify(player, true);
		actions.act(player,
				ModuleInfo.valueOf(ModuleType.FAMED_GENDERAL_COLLECT, SubModuleType.COMBINING_EQUIPMENT_SOUL));
		Reward reward = Reward.valueOf().addItem(res.getItemKey(), count);
		for (RewardItem ri : reward.getItemsByType(RewardType.ITEM)) {
			ri.putParms(ItemState.BIND.name(), "1");
		}
		rewardManager.grantReward(player, reward,
				ModuleInfo.valueOf(ModuleType.FAMED_GENDERAL_COLLECT, SubModuleType.COMBINING_EQUIPMENT_SOUL_REWARD));
		return SM_Combining_CreateSoul.valueOf(player);
	}

	@Override
	public SM_Decompose_Item spliteCreateSoul(Player player, HashSet<Long> indexes) {
		for (long idIndex : indexes) {
			AbstractItem item = player.getPack().getItemByGuid(idIndex);
			if (item == null) {
				throw new ManagedException(ManagedErrorCode.OPERATE_TOO_FAST);
			}
		}

		Reward reward = Reward.valueOf();
		for (long idIndex : indexes) {
			AbstractItem item = player.getPack().removeItemByGuid(idIndex);
			if (item.getResource().getReward() != null) {
				for (int i = 0; i < item.getSize(); i++) {
					reward.addReward(rewardManager.creatReward(player, item.getResource().getReward(), null));
				}
				LogManager.addItemLog(player, System.currentTimeMillis(),
						ModuleInfo.valueOf(ModuleType.DECOMPOSE_ITEM, SubModuleType.DECOMPOSE_ITEM_REWARD), -1,
						item.getSize(), item, player.getPack().getItemSizeByKey(item.getKey()));
			} else if (item.getResource().getChooserReward() != null) {
				for (int i = 0; i < item.getSize(); i++) {
					List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(player,
							item.getResource().getChooserReward());
					reward.addReward(rewardManager.creatReward(player, rewardIds, null));
				}
				LogManager.addItemLog(player, System.currentTimeMillis(),
						ModuleInfo.valueOf(ModuleType.DECOMPOSE_ITEM, SubModuleType.DECOMPOSE_ITEM_REWARD), -1,
						item.getSize(), item, player.getPack().getItemSizeByKey(item.getKey()));
			}
		}

		rewardManager.grantReward(player, reward,
				ModuleInfo.valueOf(ModuleType.DECOMPOSE_ITEM, SubModuleType.DECOMPOSE_ITEM_REWARD));

		return SM_Decompose_Item.valueOf(player);
	}

	@Override
	public SM_Equip_Inset_Gem insetEquipGem(Player player, long equipmentId, long gemId) {
		if (!moduleOpenManager.isOpenByKey(player, "opmk84")) {
			throw new ManagedException(ManagedErrorCode.MODULE_NOT_OPEN);
		}
		Equipment equip = null;
		for (Equipment e : player.getEquipmentStorage().getEquipments()) {
			if (e != null) {
				if (e.getObjectId() == equipmentId) {
					equip = e;
					break;
				}
			}
		}
		checkIsEquipment(equip);

		AbstractItem item = player.getPack().getItemByGuid(gemId);
		if (item == null || !item.getResource().getGemType().isBelongGroup(equip.getEquipmentType())) {
			throw new ManagedException(ManagedErrorCode.OPERATE_TOO_FAST);
		}

		EquipmentStat equipStat = equip.getExtraStats().get(EquipmentStatType.GEM_STAT.getValue());
		if (equipStat == null) {
			equipStat = EquipmentStat.valueOf(EquipmentStatType.GEM_STAT.getValue(), item.getKey());
		} else if (equipStat.getContext().size() >= itemManager.EQUIP_GEM_MAX_SIZE.getValue()) {
			boolean stillHave = false;
			for (int i = 0; i < itemManager.EQUIP_GEM_MAX_SIZE.getValue(); i++) {
				if (equipStat.getContext().get(i) == null
						|| equipStat.getContext().get(i).equals(EquipmentStat.GEM_NOT_EXIST)) {
					stillHave = true;
					equipStat.getContext().set(i, item.getKey());
					break;
				}
			}
			if (!stillHave) {
				throw new ManagedException(ManagedErrorCode.INSET_GEM_FULL);
			}
		} else {
			boolean inset = false;
			for (int i = 0; i < itemManager.EQUIP_GEM_MAX_SIZE.getValue() && i < equipStat.getContext().size(); i++) {
				if (equipStat.getContext().get(i) == null
						|| equipStat.getContext().get(i).equals(EquipmentStat.GEM_NOT_EXIST)) {
					equipStat.getContext().set(i, item.getKey());
					inset = true;
					break;
				}
			}
			if (!inset) {
				equipStat.getContext().add(item.getKey());
			}
		}
		equip.getExtraStats().put(EquipmentStatType.GEM_STAT.getValue(), equipStat);

		player.getPack().reduceItemByGuid(gemId, 1);

		equip.addEquipmentExtraStats(player, true);
		itemManager.calculateGemSuit(player, true);
		player.getEquipmentStorage().markByEquipmentType(equip.getEquipmentType());

		ModuleInfo info = ModuleInfo.valueOf(ModuleType.EQUIP_INSET_GEM, SubModuleType.INSET_GEM);
		LogManager.addItemLog(player, System.currentTimeMillis(), info, -1, item.getSize(), item, player.getPack()
				.getItemSizeByKey(item.getKey()));
		LogManager.equipmentGemLog(player, equip.getObjectId(), equip.getKey(), equipStat.getContext(), info);

		return SM_Equip_Inset_Gem.valueOf(player);
	}

	@Override
	public SM_Equip_Unset_Gem unsetEquipGem(Player player, long equipmentId, int index) {
		if (index >= itemManager.EQUIP_GEM_MAX_SIZE.getValue() || index < 0) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		if (player.getPack().isFull()) {
			throw new ManagedException(ManagedErrorCode.PACK_FULL);
		}

		boolean mainSelectByEquip = true;
		Equipment equip = null;
		for (Equipment e : player.getEquipmentStorage().getEquipments()) {
			if (e != null) {
				if (e.getObjectId() == equipmentId)
					equip = e;
			}
		}
		if (equip == null) {
			equip = checkIsEquipment(player.getPack().getItemByGuid(equipmentId));
			mainSelectByEquip = false;
		}

		EquipmentStat equipStat = equip.getExtraStats().get(EquipmentStatType.GEM_STAT.getValue());
		if (equipStat == null || index >= equipStat.getContext().size()
				|| equipStat.getContext().get(index).equals(EquipmentStat.GEM_NOT_EXIST)) {
			throw new ManagedException(ManagedErrorCode.UNSET_GEM_IS_NOT_EXIST);
		}
		Reward reward = Reward.valueOf().addItem(equipStat.getContext().get(index), 1);
		for (RewardItem ri : reward.getItemsByType(RewardType.ITEM)) {
			ri.putParms(ItemState.BIND.name(), "1");
		}
		rewardManager.grantReward(player, reward,
				ModuleInfo.valueOf(ModuleType.EQUIP_INSET_GEM, SubModuleType.UNSET_GEM));

		equipStat.getContext().set(index, EquipmentStat.GEM_NOT_EXIST);
		if (mainSelectByEquip) {
			equip.addEquipmentExtraStats(player, true);
			itemManager.calculateGemSuit(player, true);
			player.getEquipmentStorage().markByEquipmentType(equip.getEquipmentType());
		}

		LogManager.equipmentGemLog(player, equip.getObjectId(), equip.getKey(), equipStat.getContext(),
				ModuleInfo.valueOf(ModuleType.EQUIP_INSET_GEM, SubModuleType.UNSET_GEM));
		return SM_Equip_Unset_Gem.valueOf(player);
	}

	private void sendGemMail(Player player, boolean transfer, Equipment equip) {
		if (!equip.hasSpecifiedTypeStat(EquipmentStatType.GEM_STAT)) {
			return;
		}

		List<String> gemIds = New.arrayList();
		for (String itemId : equip.getExtraStats().get(EquipmentStatType.GEM_STAT.getValue()).getContext()) {
			if (itemId == null || itemId.equals(EquipmentStat.GEM_NOT_EXIST)) {
				continue;
			}
			gemIds.add(itemId);
		}
		equip.getExtraStats().remove(EquipmentStatType.GEM_STAT.getValue());
		if (gemIds.isEmpty()) {
			return;
		}
		Reward reward = Reward.valueOf();
		for (String itemKey : gemIds) {
			reward.addItem(itemKey, 1);
		}
		for (RewardItem ri : reward.getItemsByType(RewardType.ITEM)) {
			ri.putParms(ItemState.BIND.name(), "1");
		}
		I18nUtils titel18n = I18nUtils.valueOf(transfer ? itemManager.EQUIP_TRANSFER_LEVEL_GEM_TITLE.getValue()
				: itemManager.EQUIP_DESTROY_GEM_TITLE.getValue());
		I18nUtils contextl18n = I18nUtils.valueOf(transfer ? itemManager.EQUIP_TRANSFER_LEVEL_GEM_CONTENT.getValue()
				: itemManager.EQUIP_DESTROY_GEM_CONTENT.getValue());
		contextl18n.addParm("name", I18nPack.valueOf(player.getName()));
		contextl18n.addParm("equipment", I18nPack.valueOf(equip.getName()));
		Mail mail = Mail.valueOf(titel18n, contextl18n, null, reward);
		MailManager.getInstance().sendMail(mail, player.getObjectId());
	}

	@Override
	public void attachGodExtraStat(Player player, int index, boolean addRate) {
		Equipment equip = checkIsEquipment(player.getEquipmentStorage().getEquip(index));
		String resourceId = itemManager.getEquipmentGodResource(equip.getResource().getRoletype(), equip
				.getEquipmentType().getIndex(), equip.getResource().getLevel(), equip.getResource().getQuality(), equip
				.getResource().getSpecialType());
		if (resourceId == null || !equip.hasSpecifiedTypeStat(EquipmentStatType.SOUL_STAT)) {
			throw new ManagedException(ManagedErrorCode.EQUIPMENT_CANNOT_ATTACH_GODSTAT);
		}
		EquipmentGodResource resource = itemManager.equipmentGodResources.get(resourceId, true);
		CoreActions actions = resource.getEquipmentGodActions();
		if (!actions.verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.NOT_ENOUGH_ITEM);
		}

		if (addRate) {
			CoreActions rateAddActions = resource.getEquipmentGodComposeRateActions();
			if (!rateAddActions.verify(player, true)) {
				throw new ManagedException(ManagedErrorCode.NOT_ENOUGH_GOLD);
			}
			rateAddActions
					.act(player, ModuleInfo.valueOf(ModuleType.ATTACH_GOD, SubModuleType.ATTACH_GOD_STAT_ACTIONS));
		}
		actions.act(player, ModuleInfo.valueOf(ModuleType.ATTACH_GOD, SubModuleType.ATTACH_GOD_STAT_ACTIONS));

		boolean randSucc = addRate ? true : RandomUtils.isHit(resource.getRate() * 1.0 / 10000.0);

		if (randSucc) {
			equip.addExtraStats(EquipmentStatType.GOD_STAT.getValue(), resourceId);
			equip.addEquipmentExtraStats(player, true);
			player.getEquipmentStorage().markByEquipmentType(equip.getEquipmentType());

			ItemShow show = new ItemShow();
			show.setOwner(player.getName());
			show.setKey(equip.getKey());
			show.setItem(equip);
			I18nUtils utils = I18nUtils.valueOf("321001");
			utils.addParm("name", I18nPack.valueOf(player.getName()));
			utils.addParm("country", I18nPack.valueOf(player.getCountry().getName()));
			utils.addParm(
					"color",
					I18nPack.valueOf(ItemManager.getInstance().ITEM_QUALITY_COLOR.getValue().get(
							"" + equip.getResource().getQuality())));
			utils.addParm("item", I18nPack.valueOf(show));
			ChatManager.getInstance().sendSystem(0, utils, null);
			PacketSendUtility.sendPacket(player, SM_Attach_God.valueOf(player, true));
		} else {
			PacketSendUtility.sendPacket(player, SM_Attach_God.valueOf(player, false));
		}
		LogManager.equipmentGodStatsLog(player, equip.getObjectId(), equip.getKey(), resourceId,
				ModuleInfo.valueOf(ModuleType.ATTACH_GOD, SubModuleType.ATTACH_GOD_STAT_ACTIONS), randSucc ? 1 : 0);
	}

	/**
	 * 转生
	 * 
	 * @param player
	 * @param index
	 * @param addRate
	 */
	public void attachTurnExtra(Player player, int index, boolean addRate) {
		if (!moduleOpenManager.isOpenByModuleKey(player, ModuleKey.EQUIP_SUICIDE_TURN)) {
			throw new ManagedException(ManagedErrorCode.MODULE_NOT_OPEN);
		}

		Equipment equip = player.getEquipmentStorage().getEquip(index);
		if (!equip.getExtraStats().containsKey(EquipmentStatType.SOUL_STAT.getValue())) {
			throw new ManagedException(ManagedErrorCode.NOT_SOUL_EQUIPMENT);
		}

		String resourceId = itemManager.getEquipmentTurnResource(equip.getTurn() + 1,
				equip.getResource().getRoletype(), equip.getEquipmentType().getIndex(), equip.getResource().getLevel(),
				equip.getResource().getQuality(), equip.getResource().getSpecialType());
		if (resourceId == null) {
			throw new ManagedException(ManagedErrorCode.EQUIPMENT_CANNOT_TURN);
		}

		EquipmentTurnResource resource = itemManager.equipTurnResources.get(resourceId, true);

		CoreActions actions = new CoreActions();
		actions.addActions(resource.getCoreActions());
		if (!actions.verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}

		if (addRate) {
			CoreActions rateAddActions = resource.getRateActions();
			if (!rateAddActions.verify(player, true)) {
				throw new ManagedException(ManagedErrorCode.NOT_ENOUGH_GOLD);
			}
			rateAddActions.act(player, ModuleInfo.valueOf(ModuleType.EQUIP_TURN, SubModuleType.EQUIP_TURN_ACTIONS));
		}
		actions.act(player, ModuleInfo.valueOf(ModuleType.EQUIP_TURN, SubModuleType.EQUIP_TURN_ACTIONS));

		boolean randSucc = addRate ? true : RandomUtils.isHit(resource.getRate() * 1.0 / 10000.0);

		if (randSucc) {
			equip.addTurn(player, resource.getId());
			PacketSendUtility.sendPacket(player, SM_Attach_SuicideState.valueOf(player, true));
			ItemShow show = new ItemShow();
			show.setOwner(player.getName());
			show.setKey(equip.getKey());
			show.setItem(equip);
			I18nUtils utils = I18nUtils.valueOf("320016");
			utils.addParm("name", I18nPack.valueOf(player.getName()));
			utils.addParm("country", I18nPack.valueOf(player.getCountry().getName()));
			utils.addParm(
					"color",
					I18nPack.valueOf(ItemManager.getInstance().ITEM_QUALITY_COLOR.getValue().get(
							"" + equip.getResource().getQuality())));
			utils.addParm("item", I18nPack.valueOf(show));
			ChatManager.getInstance().sendSystem(0, utils, null);
		} else {
			PacketSendUtility.sendPacket(player, SM_Attach_SuicideState.valueOf(player, false));
		}
		LogManager.equipmentTurnStatsLog(player, equip.getObjectId(), equip.getKey(), resourceId,
				ModuleInfo.valueOf(ModuleType.EQUIP_TURN, SubModuleType.EQUIP_TURN_ACTIONS), randSucc ? 1 : 0);
	}

	@Override
	public void changeEquipmentSuit(Player player, int index) {
		Equipment equip = checkIsEquipment(player.getEquipmentStorage().getEquip(index));
		List<EquipmentSuitResource> resources = itemManager.getNormalEquipmentSuitResource(equip.getResource()
				.getLevel());
		if (equip.getResource().getQuality() < ItemResource.PURPLE || resources.isEmpty()
				|| !equip.hasSpecifiedTypeStat(EquipmentStatType.SOUL_STAT)
				|| equip.hasSpecifiedTypeStat(EquipmentStatType.SUIT_STAT)) {
			throw new ManagedException(ManagedErrorCode.EQUIPMENT_CANNOT_CHANGE_SUIT);
		}

		CoreActions actions = ItemConfig.getInstance().getSuitEquipmentActions(player, equip);
		if (actions == null) {
			throw new ManagedException(ManagedErrorCode.EQUIPMENT_CANNOT_CHANGE_SUIT);
		}
		if (!actions.verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.NOT_ENOUGH_ITEM);
		}
		actions.act(player, ModuleInfo.valueOf(ModuleType.CHANGE_SUIT, SubModuleType.CHANGE_SUIT_ACTIONS));
		equip.addExtraStats(EquipmentStatType.SUIT_STAT.getValue(), EquipmentStatType.SUIT_STAT.name());

		itemManager.calculateSuit(player, true);
		player.getEquipmentStorage().markByEquipmentType(equip.getEquipmentType());

		ItemShow show = new ItemShow();
		show.setOwner(player.getName());
		show.setKey(equip.getKey());
		show.setItem(equip);
		I18nUtils utils = I18nUtils.valueOf("309105");
		utils.addParm("name", I18nPack.valueOf(player.getName()));
		utils.addParm("country", I18nPack.valueOf(player.getCountry().getName()));
		utils.addParm("equip", I18nPack.valueOf(show));
		ChatManager.getInstance().sendSystem(0, utils, null);
		PacketSendUtility.sendPacket(player, SM_Change_Equipment_Suit.valueOf(player));
	}

	@Override
	public void transferEnhanceLevel(Player player) {
		if (player.getEquipmentStorage().isResetEnhanceLevel()) {
			return;
		}
		boolean changed = false;
		for (Equipment e : player.getEquipmentStorage().getEquipments()) {
			if (e == null) {
				continue;
			}
			EquipmentEnhanceResource res = itemManager.getEquipEnhanceResource(e.getEnhanceLevel());
			if (res.getBeta19NewEnhanceLevel() != 0) {
				e.setEnhanceLevel(res.getBeta19NewEnhanceLevel());
				changed = true;
			}

			LogManager.addEnhanceLog(player.getPlayerEnt().getAccountName(), player.getName(), player.getObjectId(),
					System.currentTimeMillis(), e.getObjectId(), e.getKey(), res.getLevel(), e.getEnhanceLevel());
		}
		for (AbstractItem item : player.getPack().getItems()) {
			if (item != null && (item instanceof Equipment)) {
				Equipment e = (Equipment) item;
				EquipmentEnhanceResource res = itemManager.getEquipEnhanceResource(e.getEnhanceLevel());
				if (res.getBeta19NewEnhanceLevel() != 0) {
					e.setEnhanceLevel(res.getBeta19NewEnhanceLevel());
					changed = true;
				}

				LogManager.addEnhanceLog(player.getPlayerEnt().getAccountName(), player.getName(),
						player.getObjectId(), System.currentTimeMillis(), e.getObjectId(), e.getKey(), res.getLevel(),
						e.getEnhanceLevel());
			}
		}
		for (AbstractItem item : player.getWareHouse().getItems()) {
			if (item != null && (item instanceof Equipment)) {
				Equipment e = (Equipment) item;
				EquipmentEnhanceResource res = itemManager.getEquipEnhanceResource(e.getEnhanceLevel());
				if (res.getBeta19NewEnhanceLevel() != 0) {
					e.setEnhanceLevel(res.getBeta19NewEnhanceLevel());
					changed = true;
				}

				LogManager.addEnhanceLog(player.getPlayerEnt().getAccountName(), player.getName(),
						player.getObjectId(), System.currentTimeMillis(), e.getObjectId(), e.getKey(), res.getLevel(),
						e.getEnhanceLevel());
			}
		}
		// transfer over
		player.getEquipmentStorage().setResetEnhanceLevel(true);
		if (changed) {
			EventBusManager.getInstance().submit(EnhanceEquipmentEvent.valueOf(player));
		}
	}

	@Override
	public void upgradeEquipmentSoulLevel(Player player, int index) {
		Equipment equip = checkIsEquipment(player.getEquipmentStorage().getEquip(index));
		if (equip.getResource().getQuality() < ItemResource.ORANGE
				|| !equip.hasSpecifiedTypeStat(EquipmentStatType.SOUL_STAT)) {
			throw new ManagedException(ManagedErrorCode.EQUIPMENT_CANNOT_UPGRADE_SOUL);
		}
		List<String> ids = equip.getExtraIDs(EquipmentStatType.SOUL_STAT);
		String originalSoulKey = ids.get(0);
		EquipmentSoulResource resource = itemManager.getEquipmentSoulReousrce(originalSoulKey);
		if (!ItemConfig.getInstance().checkCanUpgradeSoul(originalSoulKey)) {
			throw new ManagedException(ManagedErrorCode.EQUIPMENT_CANNOT_UPGRADE_SOUL);
		}
		ItemAction action = CoreActionType.createItemCondition(ItemConfig.getInstance().STONE_OF_SOUL_ITEM_KEY
				.getValue(),
				ItemConfig.getInstance().getUpgradeSoulLevelItemActNum(player, equip.getEquipmentType(), resource));
		if (!action.verify(player)) {
			throw new ManagedException(ManagedErrorCode.NOT_ENOUGH_ITEM);
		}
		action.act(player,
				ModuleInfo.valueOf(ModuleType.EQUIPMENT_SOUL_UPGRADE, SubModuleType.EQUIPMENT_SOUL_UPGRADE_ACTIONS));
		String targetSoulKey = itemManager.getSoulIndexKey(equip.getEquipmentType(),
				String.valueOf((resource.getSoulStatLevel() + 1)), String.valueOf(resource.getStatType()));
		equip.changeStats(EquipmentStatType.SOUL_STAT, targetSoulKey, 0);
		equip.addEquipmentExtraStats(player, true);
		player.getEquipmentStorage().markByEquipmentType(equip.getEquipmentType());

		PacketSendUtility.sendPacket(player, SM_Upgrade_Soul.valueOf(player));
		LogManager.addForgeLog(player.getPlayerEnt().getServer(), player.getPlayerEnt().getAccountName(),
				player.getName(), player.getObjectId(), System.currentTimeMillis(), equip.getKey(), "", targetSoulKey);
		ItemShow show = new ItemShow();
		show.setKey(equip.getKey());
		show.setOwner(player.getName());
		show.setItem(equip);
		I18nUtils utils = I18nUtils.valueOf("309106");
		utils.addParm("name", I18nPack.valueOf(player.getName()));
		utils.addParm("country", I18nPack.valueOf(player.getCountry().getName()));
		utils.addParm("equip", I18nPack.valueOf(show));
		ChatManager.getInstance().sendSystem(0, utils, null);
	}

	public void transferTreasureItems(Player player) {
		for (TreasureItemFixResource resource : itemManager.treasureFixStorage.getAll()) {
			String sourceItem = resource.getSourceItem();
			// 在背包的数量
			long sourceItemPackSize = player.getPack().getItemSizeByKey(sourceItem);
			Formula formula = itemManager.formulaStorage.get(resource.getFormulaId(), true);
			long sourceItemTotalSize = 0;
			long targetItemTotalSize = 0;
			if (sourceItemPackSize > 0L) {
				Integer targetItemSize = (Integer) FormulaParmsUtil.valueOf(formula)
						.addParm("count", sourceItemPackSize).getValue();
				sourceItemTotalSize += sourceItemPackSize;
				targetItemTotalSize += targetItemSize;
				player.getPack().reduceItemByKey(resource.getSourceItem(), (int) sourceItemPackSize);
			}
			// 在仓库数量
			long sourceItemWareHouseSize = player.getWareHouse().getItemSizeByKey(resource.getSourceItem());
			if (sourceItemWareHouseSize > 0L) {
				Integer targetItemSize = (Integer) FormulaParmsUtil.valueOf(formula)
						.addParm("count", sourceItemWareHouseSize).getValue();
				sourceItemTotalSize += sourceItemWareHouseSize;
				targetItemTotalSize += targetItemSize;
				player.getWareHouse().reduceItemByKey(resource.getSourceItem(), (int) sourceItemWareHouseSize);
			}

			if (sourceItemTotalSize > 0) {
				I18nUtils titel18n = I18nUtils.valueOf(resource.getMailTitleIl18n());
				I18nUtils contextl18n = I18nUtils.valueOf(resource.getMailContentIl18n());
				contextl18n.addParm("count", I18nPack.valueOf(sourceItemTotalSize));
				contextl18n.addParm("item",
						I18nPack.valueOf(ItemManager.getInstance().getResource(resource.getSourceItem()).getName()));
				contextl18n.addParm("num", I18nPack.valueOf(targetItemTotalSize));
				contextl18n.addParm("targetitem",
						I18nPack.valueOf(ItemManager.getInstance().getResource(resource.getTargetItem()).getName()));
				Map<String, String> params = new HashMap<String, String>();
				params.put(ItemState.BIND.name(), "1");
				Reward reward = Reward.valueOf();
				reward.addItem(RewardType.ITEM, resource.getTargetItem(), (int) targetItemTotalSize, params);
				Mail mail = Mail.valueOf(titel18n, contextl18n, null, reward);
				MailManager.getInstance().sendMail(mail, player.getObjectId());
			}
		}
	}

	@Override
	public void transferSoulLevel(Player player) {
		for (Equipment e : player.getEquipmentStorage().getEquipments()) {
			transferEquipmentSoulLevel(e);
		}
		for (AbstractItem item : player.getPack().getItems()) {
			if (item != null && (item instanceof Equipment)) {
				Equipment e = (Equipment) item;
				transferEquipmentSoulLevel(e);
			}
		}
		for (AbstractItem item : player.getWareHouse().getItems()) {
			if (item != null && (item instanceof Equipment)) {
				Equipment e = (Equipment) item;
				transferEquipmentSoulLevel(e);
			}
		}
	}

	private void transferEquipmentSoulLevel(Equipment e) {
		if (e == null || !e.hasSpecifiedTypeStat(EquipmentStatType.SOUL_STAT)) {
			return;
		}
		List<String> soulIds = e.getExtraIDs(EquipmentStatType.SOUL_STAT);
		if (soulIds == null || soulIds.isEmpty()) {
			return;
		}
		EquipmentSoulResource soulResource = itemManager.getEquipmentSoulReousrce(soulIds.get(0));

		if (soulResource.getSoulStatLevel() == 5) {
			String soulId = soulResource.getNextId();
			soulIds.set(0, soulId);
		}
	}
}
