package com.mmorpg.mir.model.horse.facade;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.country.resource.ConfigValueManager;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.mmorpg.mir.model.horse.model.Horse;
import com.mmorpg.mir.model.horse.packet.CM_ActiveIllution;
import com.mmorpg.mir.model.horse.packet.CM_Cancel_Illution;
import com.mmorpg.mir.model.horse.packet.CM_Effect_Illution;
import com.mmorpg.mir.model.horse.packet.CM_Ride;
import com.mmorpg.mir.model.horse.packet.CM_UnRide;
import com.mmorpg.mir.model.horse.packet.CM_Upgrade;
import com.mmorpg.mir.model.horse.packet.SM_HorseUpdate;
import com.mmorpg.mir.model.horse.service.HorseService;
import com.mmorpg.mir.model.moduleopen.event.ModuleOpenEvent;
import com.mmorpg.mir.model.moduleopen.manager.ModuleOpenManager;
import com.mmorpg.mir.model.moduleopen.resource.ModuleOpenResource;
import com.mmorpg.mir.model.player.event.LevelUpEvent;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.SessionUtil;
import com.mmorpg.mir.model.world.World;
import com.windforce.common.event.anno.ReceiverAnno;
import com.xiaosan.dispatcher.anno.HandlerAnno;
import com.xiaosan.socket.core.TSession;

@Component
public class HorseFacade {

	private static Logger logger = Logger.getLogger(HorseFacade.class);
	@Autowired
	private HorseService horseService;

	@Autowired
	private ModuleOpenManager moduleOpen;

	@Autowired
	private ConfigValueManager configValueManager;

	@HandlerAnno
	public SM_HorseUpdate upgrade(TSession session, CM_Upgrade req) {
		SM_HorseUpdate sm = new SM_HorseUpdate();
		try {
			Player player = SessionUtil.getPlayerBySession(session);
			player.getHorse().refreshBlessing();
			sm = horseService.upgradeHorse(player, req.getUseGold() == 1 ? true : false);
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("升级坐骑", e);
			sm.setCode(ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}

	@HandlerAnno
	public void ride(TSession session, CM_Ride req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			if (!World.getInstance().getMapResource(player.getMapId()).isCanRide()) {
				return;
			}
			boolean inGreenhandMap = configValueManager.GREENHAND_MAPS.getValue()[player.getCountryValue() - 1] == player
					.getMapId() ? true : false;
			if (moduleOpen.isOpenByModuleKey(player, ModuleKey.HORSE) || inGreenhandMap) {
				player.getHorse().refreshBlessing();
				horseService.ride(player);
			} else {
				PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.MODULE_NOT_OPEN);
			}
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("升级坐骑", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void unRide(TSession session, CM_UnRide req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			if (moduleOpen.isOpenByModuleKey(player, ModuleKey.HORSE)) {
				player.getHorse().refreshBlessing();
				horseService.unRide(player);
			} else {
				PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.MODULE_NOT_OPEN);
			}
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("下坐骑出现未知未知异常", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void activeIllution(TSession session, CM_ActiveIllution req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			horseService.activeIllution(player, req.isForeverActive(), req.getSign());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("下坐骑出现未知未知异常", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void cancelIllution(TSession session, CM_Cancel_Illution req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			horseService.cancelActiveIllution(player, req.getSign());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("下坐骑出现未知未知异常", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void useIllution(TSession session, CM_Effect_Illution req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			horseService.useIllution(player, req.getSign());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("下坐骑出现未知未知异常", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@ReceiverAnno
	public void playerLevelHorse(LevelUpEvent levelUpEvent) {
		Player player = PlayerManager.getInstance().getPlayer(levelUpEvent.getOwner());
		if (moduleOpen.isOpenByModuleKey(player, ModuleKey.HORSE)) {
			Stat[] levelStats = PlayerManager.getInstance().getPlayerLevelResource(player.getHorse().getCurrentLevel())
					.getHorseStats();
			player.getGameStats().replaceModifiers(Horse.PLAYER_LEVEL_STATEID, levelStats, true);
		}
	}

	@ReceiverAnno
	public void moduleOpenHandler(ModuleOpenEvent event) {
		ModuleOpenResource resource = ModuleOpenManager.getInstance().getResource(event.getModuleResourceId());
		if (resource != null && resource.getModuleKey() == ModuleKey.HORSE.value()) {
			Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
			horseService.flushHorse(player, false, false, false);
		}
	}

}
