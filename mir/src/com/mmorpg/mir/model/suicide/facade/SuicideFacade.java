package com.mmorpg.mir.model.suicide.facade;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.suicide.manager.SuicideManager;
import com.mmorpg.mir.model.suicide.packet.CM_Suicide_Common_Charge;
import com.mmorpg.mir.model.suicide.packet.CM_Suicide_Quick_Charge;
import com.mmorpg.mir.model.suicide.packet.CM_Suicide_Transfer;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.SessionUtil;
import com.xiaosan.dispatcher.anno.HandlerAnno;
import com.xiaosan.socket.core.TSession;

@Component
public class SuicideFacade {
	private static final Logger logger = Logger.getLogger(SuicideFacade.class);

	@Autowired
	private SuicideManager suicideManager;

	@HandlerAnno
	public void commonCharge(TSession session, CM_Suicide_Common_Charge req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			suicideManager.commonCharge(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("普通充元出现未知异常 ！", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void quickCharge(TSession session, CM_Suicide_Quick_Charge req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			suicideManager.quickCharge(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("一键充元出现未知异常 ！", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void transfer(TSession session, CM_Suicide_Transfer req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			suicideManager.tranfer(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("转生出现未知异常 ！", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

}
