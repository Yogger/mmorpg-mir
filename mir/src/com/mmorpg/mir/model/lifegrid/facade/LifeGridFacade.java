package com.mmorpg.mir.model.lifegrid.facade;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.lifegrid.manager.LifeGridManager;
import com.mmorpg.mir.model.lifegrid.model.LifeStorageType;
import com.mmorpg.mir.model.lifegrid.packet.CM_LifeGrid_Convert;
import com.mmorpg.mir.model.lifegrid.packet.CM_LifeGrid_Devour;
import com.mmorpg.mir.model.lifegrid.packet.CM_LifeGrid_DevourAll;
import com.mmorpg.mir.model.lifegrid.packet.CM_LifeGrid_Drop;
import com.mmorpg.mir.model.lifegrid.packet.CM_LifeGrid_Equip;
import com.mmorpg.mir.model.lifegrid.packet.CM_LifeGrid_ExchangePack;
import com.mmorpg.mir.model.lifegrid.packet.CM_LifeGrid_OperateLock;
import com.mmorpg.mir.model.lifegrid.packet.CM_LifeGrid_Take;
import com.mmorpg.mir.model.lifegrid.packet.CM_LifeGrid_Take_All;
import com.mmorpg.mir.model.lifegrid.packet.CM_LifeGrid_UnEquip;
import com.mmorpg.mir.model.lifegrid.packet.SM_LifeGrid_Devour;
import com.mmorpg.mir.model.lifegrid.packet.SM_LifeGrid_DevourAll;
import com.mmorpg.mir.model.lifegrid.packet.SM_LifeGrid_Drop;
import com.mmorpg.mir.model.lifegrid.packet.SM_LifeGrid_Equip;
import com.mmorpg.mir.model.lifegrid.packet.SM_LifeGrid_ExchangePack;
import com.mmorpg.mir.model.lifegrid.packet.SM_LifeGrid_OperateLock;
import com.mmorpg.mir.model.lifegrid.packet.SM_LifeGrid_Take;
import com.mmorpg.mir.model.lifegrid.packet.SM_LifeGrid_Take_All;
import com.mmorpg.mir.model.lifegrid.packet.SM_LifeGrid_UnEquip;
import com.mmorpg.mir.model.player.event.LevelUpEvent;
import com.mmorpg.mir.model.player.event.LoginEvent;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.SessionUtil;
import com.windforce.common.event.anno.ReceiverAnno;
import com.xiaosan.dispatcher.anno.HandlerAnno;
import com.xiaosan.socket.core.TSession;

@Component
public class LifeGridFacade {
	private static final Logger logger = Logger.getLogger(LifeGridFacade.class);
	@Autowired
	private LifeGridManager lifeGridManager;

	@Autowired
	private PlayerManager playerManager;

	@HandlerAnno
	public SM_LifeGrid_Take takeFromLifeGridStorage(TSession session, CM_LifeGrid_Take req) {
		Player player = SessionUtil.getPlayerBySession(session);
		SM_LifeGrid_Take result = new SM_LifeGrid_Take();
		try {
			result = lifeGridManager.takeFromLifeGridStorage(player, req.getIndex());
		} catch (ManagedException e) {
			result.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("玩家召回宠物异常", e);
			result.setCode(ManagedErrorCode.ERROR_MSG);
		}
		return result;
	}

	@HandlerAnno
	public SM_LifeGrid_Take_All takeAllFromLifeGridStorage(TSession session, CM_LifeGrid_Take_All req) {
		Player player = SessionUtil.getPlayerBySession(session);
		SM_LifeGrid_Take_All result = new SM_LifeGrid_Take_All();
		try {
			result = lifeGridManager.takeAllFromLifeGridStorage(player, req.getIndexs());
		} catch (ManagedException e) {
			result.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("玩家召回宠物异常", e);
			result.setCode(ManagedErrorCode.ERROR_MSG);
		}
		return result;
	}

	@HandlerAnno
	public SM_LifeGrid_OperateLock operateLock(TSession session, CM_LifeGrid_OperateLock req) {
		Player player = SessionUtil.getPlayerBySession(session);
		SM_LifeGrid_OperateLock result = new SM_LifeGrid_OperateLock();
		try {
			result = lifeGridManager.operateLock(player, LifeStorageType.typeOf(req.getType()), req.getIndex());
		} catch (ManagedException e) {
			result.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("玩家召回宠物异常", e);
			result.setCode(ManagedErrorCode.ERROR_MSG);
		}
		return result;
	}

	@HandlerAnno
	public SM_LifeGrid_ExchangePack exchangePack(TSession session, CM_LifeGrid_ExchangePack req) {
		Player player = SessionUtil.getPlayerBySession(session);
		SM_LifeGrid_ExchangePack result = new SM_LifeGrid_ExchangePack();
		try {
			result = lifeGridManager.exchangePack(player, LifeStorageType.typeOf(req.getFromType()),
					LifeStorageType.typeOf(req.getToType()), req.getFromIndex(), req.getToIndex());
		} catch (ManagedException e) {
			result.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("玩家召回宠物异常", e);
			result.setCode(ManagedErrorCode.ERROR_MSG);
		}
		return result;
	}

	@HandlerAnno
	public SM_LifeGrid_Equip equip(TSession session, CM_LifeGrid_Equip req) {
		Player player = SessionUtil.getPlayerBySession(session);
		SM_LifeGrid_Equip result = new SM_LifeGrid_Equip();
		try {
			result = lifeGridManager.equip(player, req.getIndex());
		} catch (ManagedException e) {
			result.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("玩家召回宠物异常", e);
			result.setCode(ManagedErrorCode.ERROR_MSG);
		}
		return result;
	}

	@HandlerAnno
	public SM_LifeGrid_UnEquip unEquip(TSession session, CM_LifeGrid_UnEquip req) {
		Player player = SessionUtil.getPlayerBySession(session);
		SM_LifeGrid_UnEquip result = new SM_LifeGrid_UnEquip();
		try {
			result = lifeGridManager.upEquip(player, req.getIndex());
		} catch (ManagedException e) {
			result.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("玩家召回宠物异常", e);
			result.setCode(ManagedErrorCode.ERROR_MSG);
		}
		return result;
	}

	@HandlerAnno
	public SM_LifeGrid_Devour devour(TSession session, CM_LifeGrid_Devour req) {
		Player player = SessionUtil.getPlayerBySession(session);
		SM_LifeGrid_Devour result = new SM_LifeGrid_Devour();
		try {
			result = lifeGridManager.devour(player, LifeStorageType.typeOf(req.getFromStorageType()),
					LifeStorageType.typeOf(req.getToStorageType()), req.getFromIndex(), req.getToIndex());
		} catch (ManagedException e) {
			result.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("玩家召回宠物异常", e);
			result.setCode(ManagedErrorCode.ERROR_MSG);
		}
		return result;
	}

	@HandlerAnno
	public SM_LifeGrid_DevourAll devourAll(TSession session, CM_LifeGrid_DevourAll req) {
		Player player = SessionUtil.getPlayerBySession(session);
		SM_LifeGrid_DevourAll result = new SM_LifeGrid_DevourAll();
		try {
			result = lifeGridManager.devourAll(player, LifeStorageType.typeOf(req.getType()));
		} catch (ManagedException e) {
			result.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("玩家召回宠物异常", e);
			result.setCode(ManagedErrorCode.ERROR_MSG);
		}
		return result;
	}

	@HandlerAnno
	public SM_LifeGrid_Drop drop(TSession session, CM_LifeGrid_Drop req) {
		Player player = SessionUtil.getPlayerBySession(session);
		SM_LifeGrid_Drop result = new SM_LifeGrid_Drop();
		try {
			result = lifeGridManager.drop(player, LifeStorageType.typeOf(req.getType()), req.getIndexs());
		} catch (ManagedException e) {
			result.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("玩家召回宠物异常", e);
			result.setCode(ManagedErrorCode.ERROR_MSG);
		}
		return result;
	}

	@HandlerAnno
	public void convert(TSession session, CM_LifeGrid_Convert req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			lifeGridManager.convert(player, req.getId());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("玩家锻造装备异常", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@ReceiverAnno
	public void upLevel(LevelUpEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		lifeGridManager.addEquipPackSize(player);
	}

	@ReceiverAnno
	public void login(LoginEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		lifeGridManager.addEquipPackSize(player);
	}
}
