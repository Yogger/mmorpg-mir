package com.mmorpg.mir.model.invest.facade;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.invest.manager.IInvestManager;
import com.mmorpg.mir.model.invest.model.InvestType;
import com.mmorpg.mir.model.invest.packet.CM_Draw_Reward;
import com.mmorpg.mir.model.invest.packet.CM_Get_History_Record;
import com.mmorpg.mir.model.invest.packet.CM_Invest_Buy;
import com.mmorpg.mir.model.invest.packet.SM_HistoryInfo;
import com.mmorpg.mir.model.player.event.AnotherDayEvent;
import com.mmorpg.mir.model.player.event.LoginEvent;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.SessionUtil;
import com.windforce.common.event.anno.ReceiverAnno;
import com.xiaosan.dispatcher.anno.HandlerAnno;
import com.xiaosan.socket.core.TSession;

@Component
public class InvestFacade {

	private static final Logger logger = Logger.getLogger(InvestFacade.class);

	@Autowired
	private IInvestManager investManager;

	@HandlerAnno
	public void buy(TSession session, CM_Invest_Buy req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			investManager.buy(player, InvestType.typeOf(req.getInvestType()));
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("购买理财出现未知异常 ！", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void drawReward(TSession session, CM_Draw_Reward req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			investManager.drawReward(player, InvestType.typeOf(req.getInvestType()), req.getResouceId());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("领取理财奖励出现未知异常 ！", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public SM_HistoryInfo getHistoryRecord(TSession session, CM_Get_History_Record req) {
		// Player player = SessionUtil.getPlayerBySession(session);
		SM_HistoryInfo sm = new SM_HistoryInfo();
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

	@ReceiverAnno
	public void loginEvent(LoginEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		player.getInvestPool().addAccLoginDays();
	}

	@ReceiverAnno
	public void anotherDay(AnotherDayEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		player.getInvestPool().addAccLoginDays();
	}
}
