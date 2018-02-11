package com.mmorpg.mir.model.investigate.facade;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.investigate.manager.InvestigateManager;
import com.mmorpg.mir.model.investigate.packet.CM_Investigate_Accept;
import com.mmorpg.mir.model.investigate.packet.CM_Investigate_ChangeInfo;
import com.mmorpg.mir.model.investigate.packet.CM_Investigate_Complete;
import com.mmorpg.mir.model.investigate.packet.CM_Investigate_TakeInfo;
import com.mmorpg.mir.model.investigate.packet.CM_Investigate_UseItem;
import com.mmorpg.mir.model.investigate.packet.CM_Query_Investigate_Status;
import com.mmorpg.mir.model.player.event.AnotherDayEvent;
import com.mmorpg.mir.model.player.event.LoginEvent;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.SessionUtil;
import com.windforce.common.event.anno.ReceiverAnno;
import com.xiaosan.dispatcher.anno.HandlerAnno;
import com.xiaosan.socket.core.TSession;

@Component
public class InvestigateFacade {

	private static Logger logger = Logger.getLogger(InvestigateFacade.class);
	@Autowired
	private InvestigateManager investigateManager;

	@Autowired
	private PlayerManager playerManager;

	@HandlerAnno
	public void takeInfo(TSession session, CM_Investigate_TakeInfo req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			investigateManager.takeInfo(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("任命官员", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void complete(TSession session, CM_Investigate_Complete req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			investigateManager.complete(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("任命官员", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void accept(TSession session, CM_Investigate_Accept req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			investigateManager.accept(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("任命官员", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void changeInfo(TSession session, CM_Investigate_ChangeInfo req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			investigateManager.change(player, req.isUseGold());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("任命官员", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void useInvestigateItem(TSession session, CM_Investigate_UseItem req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			investigateManager.useInvestigateItem(player, req.getItemId());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("任命官员", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void queryInvestigateStatus(TSession session, CM_Query_Investigate_Status req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			investigateManager.queryInvestigateStatus(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("任命官员", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@ReceiverAnno
	public void anotherDay(AnotherDayEvent anotherDayEvent) {
		Player player = PlayerManager.getInstance().getPlayer(anotherDayEvent.getOwner());
		player.getInvestigate().refresh();
	}

	@ReceiverAnno
	public void login(LoginEvent loginEvent) {
		Player player = playerManager.getPlayer(loginEvent.getOwner());
		player.getInvestigate().refresh();
	}
}
