package com.mmorpg.mir.model.ministerfete.facade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.ministerfete.manager.MinisterFeteManager;
import com.mmorpg.mir.model.ministerfete.packet.CM_Enter_Minister;
import com.mmorpg.mir.model.ministerfete.packet.CM_Leave_Minister;
import com.mmorpg.mir.model.ministerfete.packet.CM_Minister_Dice;
import com.mmorpg.mir.model.ministerfete.packet.CM_Minister_Ranks;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.SessionUtil;
import com.xiaosan.dispatcher.anno.HandlerAnno;
import com.xiaosan.socket.core.TSession;

@Component
public class MinisterFeteFacade {

	private static final Logger logger = LoggerFactory.getLogger(MinisterFeteFacade.class);

	@Autowired
	private MinisterFeteManager ministerManager;

	@HandlerAnno
	public void enterAct(TSession session, CM_Enter_Minister req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			ministerManager.enterMinisterAct(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("进入荆轲刺秦异常", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void leaveAct(TSession session, CM_Leave_Minister req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			ministerManager.leaveMinisterAct(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("进入荆轲刺秦异常", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void getCountryTechCopyRanks(TSession session, CM_Minister_Ranks req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			ministerManager.getCountryTechCopyRanks(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("查看排行榜奖励失败", e);
			PacketSendUtility.sendErrorMessage(player);
		}
	}

	@HandlerAnno
	public void throwDice(TSession session, CM_Minister_Dice req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			ministerManager.throwDice(player, req.getHpPercent());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("查看排行榜奖励失败", e);
			PacketSendUtility.sendErrorMessage(player);
		}
	}
	
}
