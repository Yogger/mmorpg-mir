package com.mmorpg.mir.model.assassin.facade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.assassin.manager.AssassinManager;
import com.mmorpg.mir.model.assassin.packet.CM_Assassin_Dice;
import com.mmorpg.mir.model.assassin.packet.CM_Assassin_Ranks;
import com.mmorpg.mir.model.assassin.packet.CM_Enter_Assassin;
import com.mmorpg.mir.model.assassin.packet.CM_Leave_Assassin;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.SessionUtil;
import com.xiaosan.dispatcher.anno.HandlerAnno;
import com.xiaosan.socket.core.TSession;

@Component
public class AssassinFacade {

	private static final Logger logger = LoggerFactory.getLogger(AssassinFacade.class);

	@Autowired
	private AssassinManager assassinManager;

	@HandlerAnno
	public void enterAct(TSession session, CM_Enter_Assassin req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			assassinManager.enterAssassinAct(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("进入荆轲刺秦异常", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void leaveAct(TSession session, CM_Leave_Assassin req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			assassinManager.leaveAssassinAct(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("进入荆轲刺秦异常", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void getCountryTechCopyRanks(TSession session, CM_Assassin_Ranks req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			assassinManager.getCountryTechCopyRanks(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("查看排行榜奖励失败", e);
			PacketSendUtility.sendErrorMessage(player);
		}
	}

	@HandlerAnno
	public void throwDice(TSession session, CM_Assassin_Dice req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			assassinManager.throwDice(player, req.getHpPercent());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("查看排行榜奖励失败", e);
			PacketSendUtility.sendErrorMessage(player);
		}
	}
	
}
