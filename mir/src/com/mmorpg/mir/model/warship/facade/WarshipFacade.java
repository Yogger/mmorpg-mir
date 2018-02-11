package com.mmorpg.mir.model.warship.facade;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.event.AnotherDayEvent;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.system.packet.SM_System_Message;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.SessionUtil;
import com.mmorpg.mir.model.warship.packet.CM_Warship_King;
import com.mmorpg.mir.model.warship.packet.CM_Warship_Refresh;
import com.mmorpg.mir.model.warship.packet.CM_Warship_Status;
import com.mmorpg.mir.model.warship.service.WarshipService;
import com.windforce.common.event.anno.ReceiverAnno;
import com.xiaosan.dispatcher.anno.HandlerAnno;
import com.xiaosan.socket.core.TSession;

@Component
public class WarshipFacade {
	
	private static final Logger logger = Logger.getLogger(WarshipFacade.class);
	
	@Autowired
	private WarshipService warshipService;
	
	@HandlerAnno
	public void getKingsWarshipStatus(TSession session, CM_Warship_Status req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			warshipService.getWarshipStatus(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("查看膜拜皇帝的情况", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}
	
	@HandlerAnno
	public void warshipKingOfKing(TSession session, CM_Warship_King req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			warshipService.warShipKing(player, req.isSupport());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("膜拜皇帝", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}
	
	@HandlerAnno
	public void warshipRefresh(TSession session, CM_Warship_Refresh req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			warshipService.warShipRefreshReward(player, req.isGold());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("膜拜皇帝", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@ReceiverAnno
	public void anotherDayRefresh(AnotherDayEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		player.getWarship().refresh();
	}
}
