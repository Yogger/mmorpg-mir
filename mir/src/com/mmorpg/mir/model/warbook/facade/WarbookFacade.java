package com.mmorpg.mir.model.warbook.facade;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.moduleopen.event.ModuleOpenEvent;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.SessionUtil;
import com.mmorpg.mir.model.warbook.event.WarbookUpGradeEvent;
import com.mmorpg.mir.model.warbook.manager.WarbookManager;
import com.mmorpg.mir.model.warbook.packet.CM_Warbook_OperateHide;
import com.mmorpg.mir.model.warbook.packet.CM_Warbook_Upgrade;
import com.mmorpg.mir.model.warbook.packet.SM_Warbook_Upgrade;
import com.windforce.common.event.anno.ReceiverAnno;
import com.windforce.common.event.core.EventBusManager;
import com.xiaosan.dispatcher.anno.HandlerAnno;
import com.xiaosan.socket.core.TSession;

@Component
public class WarbookFacade {

	private static final Logger logger = Logger.getLogger(WarbookFacade.class);

	@Autowired
	private PlayerManager playerManager;

	@Autowired
	private WarbookManager warBookManager;

	@HandlerAnno
	public void operateHide(TSession session, CM_Warbook_OperateHide req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			warBookManager.operateHide(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("升级兵书出现未知异常！", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public SM_Warbook_Upgrade upgrade(TSession session, CM_Warbook_Upgrade req) {
		Player player = SessionUtil.getPlayerBySession(session);
		SM_Warbook_Upgrade sm = new SM_Warbook_Upgrade();
		try {
			sm = warBookManager.upgrade(player, req.isAutoBuy());
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("升级兵书出现未知异常！", e);
			sm.setCode(ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}

	@ReceiverAnno
	public void moduleOpen(ModuleOpenEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		if ("opmk95".equals(event.getModuleResourceId())) {
			player.getWarBook().broadCastKnownList();
			player.getWarBook().refreshStats(true);
			EventBusManager.getInstance().submit(WarbookUpGradeEvent.valueOf(player));
		}

	}
}
