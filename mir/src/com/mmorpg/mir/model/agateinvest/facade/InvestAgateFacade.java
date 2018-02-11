package com.mmorpg.mir.model.agateinvest.facade;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.agateinvest.manager.IInvestAgateManager;
import com.mmorpg.mir.model.agateinvest.model.InvestAgateType;
import com.mmorpg.mir.model.agateinvest.packet.CM_Agate_Draw_Reward;
import com.mmorpg.mir.model.agateinvest.packet.CM_Agate_Get_History_Record;
import com.mmorpg.mir.model.agateinvest.packet.CM_Agate_Invest_Buy;
import com.mmorpg.mir.model.agateinvest.packet.SM_Agate_HistoryInfo;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.SessionUtil;
import com.xiaosan.dispatcher.anno.HandlerAnno;
import com.xiaosan.socket.core.TSession;

@Component
public class InvestAgateFacade {

	private static final Logger logger = Logger.getLogger(InvestAgateFacade.class);

	@Autowired
	private IInvestAgateManager investManager;

	@HandlerAnno
	public void buy(TSession session, CM_Agate_Invest_Buy req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			investManager.buy(player, InvestAgateType.typeOf(req.getInvestType()));
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("购买理财出现未知异常 ！", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void drawReward(TSession session, CM_Agate_Draw_Reward req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			investManager.drawReward(player, InvestAgateType.typeOf(req.getInvestType()), req.getResouceId());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("领取理财奖励出现未知异常 ！", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public SM_Agate_HistoryInfo getHistoryRecord(TSession session, CM_Agate_Get_History_Record req) {
		SM_Agate_HistoryInfo sm = new SM_Agate_HistoryInfo();
		try {
			sm = investManager.getHistoryRecord();
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("领取理财奖励出现未知异常 ！", e);
			sm.setCode(ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}
}
