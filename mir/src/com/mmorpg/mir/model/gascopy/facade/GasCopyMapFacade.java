package com.mmorpg.mir.model.gascopy.facade;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.event.MonsterKillEvent;
import com.mmorpg.mir.model.gascopy.config.GasCopyMapConfig;
import com.mmorpg.mir.model.gascopy.manager.GasCopyManager;
import com.mmorpg.mir.model.gascopy.packet.CM_Enter_GasCopy;
import com.mmorpg.mir.model.gascopy.packet.CM_Leave_GasCopy;
import com.mmorpg.mir.model.player.event.AnotherDayEvent;
import com.mmorpg.mir.model.player.event.LoginEvent;
import com.mmorpg.mir.model.player.event.LogoutEvent;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.system.packet.SM_System_Message;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.SessionUtil;
import com.windforce.common.event.anno.ReceiverAnno;
import com.xiaosan.dispatcher.anno.HandlerAnno;
import com.xiaosan.socket.core.TSession;

@Component
public class GasCopyMapFacade {

	private static final Logger logger = Logger.getLogger(GasCopyMapFacade.class);
	
	@Autowired
	private PlayerManager playerManager;
	
	@Autowired
	private GasCopyManager gasCopyManager;

	@HandlerAnno
	public void enterGasCopy(TSession session, CM_Enter_GasCopy req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			gasCopyManager.enterGasCopy(player, req.isGold());
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("进入西周王陵出现未知异常", e);
			PacketSendUtility.sendErrorMessage(player);
		}
	}
	
	@HandlerAnno
	public void leaveGasCopy(TSession session, CM_Leave_GasCopy req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			gasCopyManager.leaveGasCopy(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("离开西周王陵出现未知异常", e);
			PacketSendUtility.sendErrorMessage(player);
		}
	}
	
	@ReceiverAnno
	public void loginGasCopyHandler(LoginEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		player.getGasCopy().offlineLogin();
	}
	
	@ReceiverAnno
	public void logout(LogoutEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		player.getGasCopy().cancelLeaveTask();
	}
	
	@ReceiverAnno
	public void anotherDayResetPlayerData(AnotherDayEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		player.getGasCopy().refresh();
	}
	
	@ReceiverAnno
	public void monsterKill(MonsterKillEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		if (event.getMostDamagePlayer() != event.getOwner()) {
			return;
		}
		
		if (!GasCopyMapConfig.getInstance().isInGasCopyMap(player.getMapId())) {
			return;
		}
		gasCopyManager.killMonster(player, event.getKey());
	}
	
}
