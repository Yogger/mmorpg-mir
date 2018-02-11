package com.mmorpg.mir.model.item.facade;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.item.config.ItemConfig;
import com.mmorpg.mir.model.item.core.IItemService;
import com.mmorpg.mir.model.item.model.EquipmentStorageType;
import com.mmorpg.mir.model.item.packet.CM_AddPackSize;
import com.mmorpg.mir.model.item.packet.CM_Attach_God;
import com.mmorpg.mir.model.item.packet.CM_Attach_SuicideState;
import com.mmorpg.mir.model.item.packet.CM_BuyBack;
import com.mmorpg.mir.model.item.packet.CM_CallBack_Pet;
import com.mmorpg.mir.model.item.packet.CM_Call_Pet;
import com.mmorpg.mir.model.item.packet.CM_Change_Equipment_Suit;
import com.mmorpg.mir.model.item.packet.CM_Combing_Ex;
import com.mmorpg.mir.model.item.packet.CM_Combining;
import com.mmorpg.mir.model.item.packet.CM_Combining_Create_Soul;
import com.mmorpg.mir.model.item.packet.CM_Compose;
import com.mmorpg.mir.model.item.packet.CM_Decompose_Item;
import com.mmorpg.mir.model.item.packet.CM_Drop;
import com.mmorpg.mir.model.item.packet.CM_EnhanceEquip;
import com.mmorpg.mir.model.item.packet.CM_EnhanceEquip_Ex;
import com.mmorpg.mir.model.item.packet.CM_Equip;
import com.mmorpg.mir.model.item.packet.CM_EquipReElement;
import com.mmorpg.mir.model.item.packet.CM_Equip_Create_Soul;
import com.mmorpg.mir.model.item.packet.CM_Equip_Inset_Gem;
import com.mmorpg.mir.model.item.packet.CM_Equip_Unset_Gem;
import com.mmorpg.mir.model.item.packet.CM_ExchagePack;
import com.mmorpg.mir.model.item.packet.CM_ExtendsEquip;
import com.mmorpg.mir.model.item.packet.CM_ExtendsEquip_Horse;
import com.mmorpg.mir.model.item.packet.CM_Merge;
import com.mmorpg.mir.model.item.packet.CM_MovePack;
import com.mmorpg.mir.model.item.packet.CM_Sell;
import com.mmorpg.mir.model.item.packet.CM_SmeltEquipment;
import com.mmorpg.mir.model.item.packet.CM_SmeltEquipment_Horse;
import com.mmorpg.mir.model.item.packet.CM_Split;
import com.mmorpg.mir.model.item.packet.CM_Store;
import com.mmorpg.mir.model.item.packet.CM_Super_Forge_Equip;
import com.mmorpg.mir.model.item.packet.CM_Take;
import com.mmorpg.mir.model.item.packet.CM_Treasure_Queery_All_Treasure_History;
import com.mmorpg.mir.model.item.packet.CM_Treasure_SeekTreasure;
import com.mmorpg.mir.model.item.packet.CM_Treasure_Take;
import com.mmorpg.mir.model.item.packet.CM_Treasure_TakeAll;
import com.mmorpg.mir.model.item.packet.CM_UnEquip;
import com.mmorpg.mir.model.item.packet.CM_Upgrade_Soul;
import com.mmorpg.mir.model.item.packet.CM_UseItem;
import com.mmorpg.mir.model.item.packet.SM_AddPackSize;
import com.mmorpg.mir.model.item.packet.SM_BuyBack;
import com.mmorpg.mir.model.item.packet.SM_Call_Pet;
import com.mmorpg.mir.model.item.packet.SM_Combining_CreateSoul;
import com.mmorpg.mir.model.item.packet.SM_Decompose_Item;
import com.mmorpg.mir.model.item.packet.SM_Drop;
import com.mmorpg.mir.model.item.packet.SM_Equip;
import com.mmorpg.mir.model.item.packet.SM_Equip_Create_Soul;
import com.mmorpg.mir.model.item.packet.SM_Equip_Inset_Gem;
import com.mmorpg.mir.model.item.packet.SM_Equip_Unset_Gem;
import com.mmorpg.mir.model.item.packet.SM_Equipment_Update;
import com.mmorpg.mir.model.item.packet.SM_Equipment_Update_Ex;
import com.mmorpg.mir.model.item.packet.SM_ExchagePack;
import com.mmorpg.mir.model.item.packet.SM_Merge;
import com.mmorpg.mir.model.item.packet.SM_MovePack;
import com.mmorpg.mir.model.item.packet.SM_Sell;
import com.mmorpg.mir.model.item.packet.SM_Split;
import com.mmorpg.mir.model.item.packet.SM_Store;
import com.mmorpg.mir.model.item.packet.SM_Take;
import com.mmorpg.mir.model.item.packet.SM_UnEquip;
import com.mmorpg.mir.model.item.packet.SM_UseItem;
import com.mmorpg.mir.model.player.event.AnotherDayEvent;
import com.mmorpg.mir.model.player.event.LoginEvent;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.shop.event.ShopBuyEvent;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.SessionUtil;
import com.windforce.common.event.anno.ReceiverAnno;
import com.xiaosan.dispatcher.anno.HandlerAnno;
import com.xiaosan.socket.core.TSession;

@Component
public final class ItemFacade {

	private static final Logger logger = Logger.getLogger(ItemFacade.class);

	@Autowired
	private IItemService itemService;

	@HandlerAnno
	public SM_Equip equip(TSession session, CM_Equip req) {
		SM_Equip sm = new SM_Equip();
		try {
			Player player = SessionUtil.getPlayerBySession(session);
			sm = itemService.equip(player, req.getIndex());
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("玩家穿装备异常", e);
			sm.setCode(ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}

	@HandlerAnno
	public SM_Call_Pet callPet(TSession session, CM_Call_Pet req) {
		SM_Call_Pet sm = new SM_Call_Pet();
		try {
			Player player = SessionUtil.getPlayerBySession(session);
			sm.setPetItem(itemService.callPet(player, req.getIndex()));
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("玩家召唤宠物异常", e);
			sm.setCode(ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}

	@HandlerAnno
	public void callBackPet(TSession session, CM_CallBack_Pet req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			itemService.callBackPet(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("玩家召回宠物异常", e);
			PacketSendUtility.sendErrorMessage(player);
		}
	}

	@HandlerAnno
	public SM_UnEquip unEquip(TSession session, CM_UnEquip req) {
		SM_UnEquip sm = new SM_UnEquip();
		try {
			Player player = SessionUtil.getPlayerBySession(session);
			sm = itemService.unEquip(player, req.getIndex(), EquipmentStorageType.typeOf(req.getEquipStorageType()));
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("玩家脱装备异常", e);
			sm.setCode(ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}

	@HandlerAnno
	public SM_ExchagePack exchangePack(TSession session, CM_ExchagePack req) {
		SM_ExchagePack sm = new SM_ExchagePack();
		try {
			Player player = SessionUtil.getPlayerBySession(session);
			sm = itemService.exchangePack(player, req.getType(), req.getFromIndex(), req.getToIndex());
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("玩家交换道具异常", e);
			sm.setCode(ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}

	@HandlerAnno
	public SM_Store store(TSession session, CM_Store req) {
		SM_Store sm = new SM_Store();
		try {
			Player player = SessionUtil.getPlayerBySession(session);
			sm = itemService.store(player, req.getIndexs());
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("玩家存储物品异常", e);
			sm.setCode(ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}

	@HandlerAnno
	public SM_Take take(TSession session, CM_Take req) {
		SM_Take sm = new SM_Take();
		try {
			Player player = SessionUtil.getPlayerBySession(session);
			sm = itemService.take(player, req.getIndexs());
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("玩家取出物品异常", e);
			sm.setCode(ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}

	@HandlerAnno
	public SM_Drop drop(TSession session, CM_Drop req) {
		SM_Drop sm = new SM_Drop();
		try {
			Player player = SessionUtil.getPlayerBySession(session);
			sm = itemService.drop(player, req.getIndexs());
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("玩家丢弃物品异常", e);
			sm.setCode(ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}

	@HandlerAnno
	public SM_UseItem useItem(TSession session, CM_UseItem req) {
		SM_UseItem sm = new SM_UseItem();
		if (req.getNum() <= 0) {
			sm.setCode(ManagedErrorCode.ERROR_MSG);
			return sm;
		}
		try {
			Player player = SessionUtil.getPlayerBySession(session);
			sm = itemService.useItem(player, req.getIndex(), req.getNum());
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("玩家使用物品异常", e);
			sm.setCode(ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}

	@HandlerAnno
	public SM_Merge merge(TSession session, CM_Merge req) {
		SM_Merge sm = new SM_Merge();
		try {
			Player player = SessionUtil.getPlayerBySession(session);
			sm = itemService.merge(player, req.getType());
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("玩家整理背包异常", e);
			sm.setCode(ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}

	@HandlerAnno
	public SM_MovePack movePack(TSession session, CM_MovePack req) {
		SM_MovePack sm = new SM_MovePack();
		try {
			Player player = SessionUtil.getPlayerBySession(session);
			sm = itemService.movePack(player, req.getType(), req.getFromIndex(), req.getToIndex());
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("玩家移动背包仓库道具异常", e);
			sm.setCode(ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}

	@HandlerAnno
	public SM_AddPackSize addPackSize(TSession session, CM_AddPackSize req) {
		SM_AddPackSize sm = new SM_AddPackSize();
		try {
			Player player = SessionUtil.getPlayerBySession(session);
			sm = itemService.addPackSize(player, req.getType(), req.getIndex());
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("玩家购买背包格异常", e);
			sm.setCode(ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}

	@HandlerAnno
	public SM_Split split(TSession session, CM_Split req) {
		SM_Split sm = new SM_Split();
		if (req.getSize() <= 0) {
			sm.setCode(ManagedErrorCode.ERROR_MSG);
			return sm;
		}

		try {
			Player player = SessionUtil.getPlayerBySession(session);
			sm = itemService.split(player, req.getType(), req.getIndex(), req.getSize());
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("玩家拆分道具异常", e);
			sm.setCode(ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}

	@HandlerAnno
	public SM_Sell sell(TSession session, CM_Sell req) {
		SM_Sell sm = new SM_Sell();
		try {
			Player player = SessionUtil.getPlayerBySession(session);
			sm = itemService.sell(player, req.getIndexs());
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("玩家卖出道具异常", e);
			sm.setCode(ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}

	@HandlerAnno
	public SM_BuyBack buyBack(TSession session, CM_BuyBack req) {
		SM_BuyBack sm = new SM_BuyBack();
		try {
			Player player = SessionUtil.getPlayerBySession(session);
			sm = itemService.buyBack(player, req.getIndex());
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("玩家回购道具异常", e);
			sm.setCode(ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}

	@HandlerAnno
	public void enhanceEquip(TSession session, CM_EnhanceEquip req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			itemService.enhanceEquipment(player, req.getEquipIndex(), req.isGold());
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_Equipment_Update.failReturn(
					player.getPack().getEnhanceLuckPoints(), player.getPack().getEnhanceLuckTime(), e.getCode(), 0));
		} catch (Exception e) {
			logger.error("玩家强化装备异常", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void enhanceEquipEx(TSession session, CM_EnhanceEquip_Ex req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			itemService.enhanceEquipmentEx(player, req.getEquipIndex(), req.isGold(),
					EquipmentStorageType.typeOf(req.getEquipStorageType()));
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_Equipment_Update_Ex.failReturn(player.getPack()
					.getEnhanceLuckPoints(), player.getPack().getEnhanceLuckTime(), e.getCode(), 0,
					EquipmentStorageType.typeOf(req.getEquipStorageType())));
		} catch (Exception e) {
			logger.error("玩家强化其他装备栏装备异常", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void forgedEquip(TSession session, CM_Compose req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			itemService.forgeEquipment(player, req.getId(), req.getRecipeKey());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("玩家锻造装备异常", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	// @HandlerAnno
	public void superForge(TSession session, CM_Super_Forge_Equip req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			itemService.superForge(player, req);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("玩家至尊锻造装备异常", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void extendsEquip(TSession session, CM_ExtendsEquip req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			itemService.extendsEquipment(player, req);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("玩家装备继承异常", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void extendsEquipHorse(TSession session, CM_ExtendsEquip_Horse req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			itemService.extendsEquipEx(player, req.getMainEquipIndex(), req.getViceEquipIndex(), req.isSelectElement(),
					req.isSelectSoul(), req.isSelectRare(), EquipmentStorageType.HORSE);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("玩家装备继承异常", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void resetEquipElement(TSession session, CM_EquipReElement req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			itemService.resetEquipElement(player, req);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("玩家装备重置五行异常", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void smeltEquipment(TSession session, CM_SmeltEquipment req) {
		Player player = SessionUtil.getPlayerBySession(session);
		if (req.getIsTreasureWare() != 0 && req.getIsTreasureWare() != 1) {
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
			return;
		}
		try {
			itemService.smeltEquipment(player, req);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("玩家装备熔炼异常", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void smeltEquipmentHorse(TSession session, CM_SmeltEquipment_Horse req) {
		Player player = SessionUtil.getPlayerBySession(session);
		if (req.getIsTreasureWare() != 0 && req.getIsTreasureWare() != 1) {
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
			return;
		}
		try {
			itemService.smeltEquipmentEx(player, req.getIndexs(), req.getIsTreasureWare(), EquipmentStorageType.HORSE);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("玩家装备熔炼异常", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void combiningItem(TSession session, CM_Combining req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			itemService.combiningItem(player, req.getCombiningId(), req.getAddition(), req.isUseGold(),
					req.getQuantity());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("玩家装备合成异常", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void queryAllTreasureHistory(TSession session, CM_Treasure_Queery_All_Treasure_History req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			itemService.queryAllTreasureHistory(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("查询全服探宝记录异常", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void takeFromTreasureStorage(TSession session, CM_Treasure_Take req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			itemService.takeFromTreasureStorage(player, req.getIndex());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("玩家提取探宝仓库异常", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void takeAllFromTreasureStorage(TSession session, CM_Treasure_TakeAll req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			itemService.takeAllFromTreasureStorage(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("玩家提取探宝仓库异常", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void seekTreasure(TSession session, CM_Treasure_SeekTreasure req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			itemService.seekTreasure(player, req.getId());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("玩家提取探宝仓库异常", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	// @HandlerAnno
	public SM_Equip_Create_Soul equipCreateSoul(TSession session, CM_Equip_Create_Soul req) {
		Player player = SessionUtil.getPlayerBySession(session);
		SM_Equip_Create_Soul sm = new SM_Equip_Create_Soul();
		try {
			sm = itemService.equipCreateSoul(player, req.getObjId(), req.getKey());
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("装备铸魂", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}

	@HandlerAnno
	public SM_Equip_Inset_Gem insetEquipGem(TSession session, CM_Equip_Inset_Gem req) {
		Player player = SessionUtil.getPlayerBySession(session);
		SM_Equip_Inset_Gem sm = new SM_Equip_Inset_Gem();
		try {
			sm = itemService.insetEquipGem(player, req.getEquipmentId(), req.getGemId());
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("装备铸魂", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}

	@HandlerAnno
	public SM_Equip_Unset_Gem unsetEquipGem(TSession session, CM_Equip_Unset_Gem req) {
		Player player = SessionUtil.getPlayerBySession(session);
		SM_Equip_Unset_Gem sm = new SM_Equip_Unset_Gem();
		try {
			sm = itemService.unsetEquipGem(player, req.getEquipmentId(), req.getIndex());
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("装备铸魂", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}

	@HandlerAnno
	public SM_Combining_CreateSoul combiningCreateSoul(TSession session, CM_Combining_Create_Soul req) {
		Player player = SessionUtil.getPlayerBySession(session);
		SM_Combining_CreateSoul sm = new SM_Combining_CreateSoul();
		try {
			sm = itemService.combiningCreateSoul(player, req.getKey(), req.getCount());
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("武魂合成", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}

	@HandlerAnno
	public void attachGodExtraStat(TSession session, CM_Attach_God req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			itemService.attachGodExtraStat(player, req.getIndex(), req.isAddRate());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("武魂合成", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void attachTurnExtraStat(TSession session, CM_Attach_SuicideState req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			itemService.attachTurnExtra(player, req.getIndex(), req.isAddRate());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("武魂合成", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}

	}

	@HandlerAnno
	public void changeEquipmentSuit(TSession session, CM_Change_Equipment_Suit req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			itemService.changeEquipmentSuit(player, req.getIndex());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("合成套装装备", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void upgradeEquipmentSoulLevel(TSession session, CM_Upgrade_Soul req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			itemService.upgradeEquipmentSoulLevel(player, req.getIndex());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("灵魂升星", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void combingItemEx(TSession session, CM_Combing_Ex req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			itemService.combiningItemEx(player, req.getCombiningId(), req.getPackIndex());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("灵魂升星", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	// @HandlerAnno
	public SM_Decompose_Item decomposeItem(TSession session, CM_Decompose_Item req) {
		Player player = SessionUtil.getPlayerBySession(session);
		SM_Decompose_Item sm = new SM_Decompose_Item();
		try {
			sm = itemService.spliteCreateSoul(player, req.getIndexs());
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("物品分解", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}

	@ReceiverAnno
	public void anotherDayRefresh(AnotherDayEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		player.getPack().refresh();
	}

	@ReceiverAnno
	public void beta19EnhanceLevelPromotion(LoginEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		itemService.transferEnhanceLevel(player);
		itemService.transferTreasureItems(player);
		itemService.transferSoulLevel(player);
		player.getEquipmentStorage().startPetTask(player);
		if (player.getShoppingHistory().getTotalCount(ItemConfig.getInstance().NEWER_BUY_PET_SHOP_ID.getValue()) > 0) {
			itemService.deleteNewerPet(player, false);
		}
	}

	@ReceiverAnno
	public void buyShopPet(ShopBuyEvent event) {
		if (event.getShopId().equals(ItemConfig.getInstance().NEWER_BUY_PET_SHOP_ID.getValue())) {
			Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
			itemService.deleteNewerPet(player, true);
		}
	}
}
