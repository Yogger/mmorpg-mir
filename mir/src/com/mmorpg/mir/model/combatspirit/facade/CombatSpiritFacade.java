package com.mmorpg.mir.model.combatspirit.facade;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.combatspirit.CombatSpirit;
import com.mmorpg.mir.model.combatspirit.manager.CombatSpiritManager;
import com.mmorpg.mir.model.combatspirit.model.CombatSpiritStorage.CombatSpiritType;
import com.mmorpg.mir.model.combatspirit.packet.CM_Query_CombatSpirit;
import com.mmorpg.mir.model.combatspirit.packet.CM_Upgrade_CombatSpirit;
import com.mmorpg.mir.model.combatspirit.packet.SM_Combatspirit_DayInital;
import com.mmorpg.mir.model.combatspirit.service.CombatSpiritService;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.event.MonsterKillEvent;
import com.mmorpg.mir.model.moduleopen.event.ModuleOpenEvent;
import com.mmorpg.mir.model.player.event.AnotherDayEvent;
import com.mmorpg.mir.model.player.event.KillPlayerEvent;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.suicide.event.SuicideTurnEvent;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.SessionUtil;
import com.windforce.common.event.anno.ReceiverAnno;
import com.xiaosan.dispatcher.anno.HandlerAnno;
import com.xiaosan.socket.core.TSession;

@Component
public class CombatSpiritFacade {

	private static Logger logger = Logger.getLogger(CombatSpiritFacade.class);

	@Autowired
	private CombatSpiritService service;

	@HandlerAnno
	public void upgradeCombatSpirit(TSession session, CM_Upgrade_CombatSpirit req) {
		Player player = SessionUtil.getPlayerBySession(session);
		CombatSpiritType type = CombatSpiritType.valueOf(req.getCombatSpiritType());
		try {
			service.upgradeCombatSpirit(player, type, req.isAuto());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("升级战魂", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void queryCombatSpiritInfo(TSession session, CM_Query_CombatSpirit req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			service.queryCombatSpirit(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("查看战魂状态", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@ReceiverAnno
	public void combatSpiritModuleOpen(ModuleOpenEvent event) {
		Map<String, String> map = CombatSpiritManager.getInstance().COMBAT_SPIRIT_INIT.getValue();
		if (map.containsKey(event.getModuleResourceId())) {
			service.openCombatSpirit(event, CombatSpiritType.valueOf(map.get(event.getModuleResourceId())));
		}
	}

	@ReceiverAnno
	public void combatSpiritModuleOpen(MonsterKillEvent event) {
		if (event.isUseForMedal()) {
			service.upgradeMedal(event);
		}
	}

	@ReceiverAnno
	public void killGainBenifit(KillPlayerEvent event) {
		Player killer = PlayerManager.getInstance().getPlayer(event.getOwner());
		try {
			if (event.isKillOtherCountryPlayer()) {
				Player killed = PlayerManager.getInstance().getPlayer(event.getKilledPlayerId());
				service.killGainBenifit(killer, killed);
			}
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(killer, e.getCode());
		} catch (Exception e) {
			logger.error("战魂护符成长", e);
			PacketSendUtility.sendErrorMessage(killer, ManagedErrorCode.SYS_ERROR);
		}
	}

	@ReceiverAnno
	public void anotherDayRefresh(AnotherDayEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		player.getCombatSpiritStorage().refresh();
		PacketSendUtility.sendPacket(player,
				SM_Combatspirit_DayInital.valueOf(player.getCombatSpiritStorage().getTodayInitialResoruceId()));
	}
	
}
