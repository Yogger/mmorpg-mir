package com.mmorpg.mir.model.seal.facade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.moduleopen.event.ModuleOpenEvent;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.seal.manager.SealManager;
import com.mmorpg.mir.model.seal.packet.CM_Seal_Upgrade;
import com.mmorpg.mir.model.seal.packet.SM_Seal_Upgrade;
import com.mmorpg.mir.model.utils.SessionUtil;
import com.windforce.common.event.anno.ReceiverAnno;
import com.xiaosan.dispatcher.anno.HandlerAnno;
import com.xiaosan.socket.core.TSession;

@Component
public class SealFacade {

	private static final Logger logger = LoggerFactory.getLogger(SealFacade.class);
	
	@Autowired
	private SealManager manager;
	
	@HandlerAnno
	public SM_Seal_Upgrade upgrade(TSession session, CM_Seal_Upgrade req) {
		Player player = SessionUtil.getPlayerBySession(session);
		SM_Seal_Upgrade sm = new SM_Seal_Upgrade();
		try {
			sm = manager.upgrade(player, req.isAutoBuy());
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("升级兵书出现未知异常！", e);
			sm.setCode(ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}
	
	@ReceiverAnno
	public void sealModuleOpen(ModuleOpenEvent event) {
		if (event.getModuleResourceId().equals("opmk105")) {
			Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
			player.getSeal().refreshStats(true);
		}
	}
}
