package com.mmorpg.mir.model.relive.facade;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.event.LoginEvent;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.relive.manager.PlayerReliveManager;
import com.mmorpg.mir.model.relive.packet.CM_Respawn;
import com.mmorpg.mir.model.relive.service.PlayerReliveService;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.SessionUtil;
import com.windforce.common.event.anno.ReceiverAnno;
import com.xiaosan.dispatcher.anno.HandlerAnno;
import com.xiaosan.socket.core.TSession;

@Component
public class ReliveFacade {
	private static final Logger logger = Logger.getLogger(ReliveFacade.class);

	@Autowired
	private PlayerReliveService reliveService;

	@HandlerAnno
	public void respawn(TSession session, CM_Respawn req) {
		Player player = null;
		try {
			player = SessionUtil.getPlayerBySession(session);
			if (player != null) {
				reliveService.respawn(player, req.isUseItem(), req.isBuy(), true);
			}
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("玩家重生异常", e);
		}
	}

	@ReceiverAnno
	public void playerLoginRelive(LoginEvent loginEvent) {
		Player player = PlayerManager.getInstance().getPlayer(loginEvent.getOwner());
		long passTime = System.currentTimeMillis() - player.getLifeStats().getLastDeadTime();
		if (player.getLifeStats().isAlreadyDead() && passTime > PlayerReliveManager.getInstance().AUTO_RELIVE_TIME.getValue()) {
			reliveService.respawn(player, false, false, false);
		}
	}
}
