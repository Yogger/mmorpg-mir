package com.mmorpg.mir.model.express.facade;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.express.manager.ExpressManager;
import com.mmorpg.mir.model.express.packet.CM_Ask_For_Help;
import com.mmorpg.mir.model.express.packet.CM_Express_Reset;
import com.mmorpg.mir.model.express.packet.CM_Express_Reward;
import com.mmorpg.mir.model.express.packet.CM_Express_Start;
import com.mmorpg.mir.model.express.packet.CM_Faraway_Status;
import com.mmorpg.mir.model.express.packet.CM_Fly_ToLorry;
import com.mmorpg.mir.model.express.packet.CM_Get_Current_Express;
import com.mmorpg.mir.model.express.packet.CM_Lorry_AttackTime;
import com.mmorpg.mir.model.express.packet.CM_Rob_Specified_Lorry;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.event.LogoutEvent;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.SessionUtil;
import com.windforce.common.event.anno.ReceiverAnno;
import com.xiaosan.dispatcher.anno.HandlerAnno;
import com.xiaosan.socket.core.TSession;

@Component
public class ExpressFacade {

	private static Logger logger = Logger.getLogger(ExpressFacade.class);
	@Autowired
	private ExpressManager expressManager;

	@HandlerAnno
	public void startExpress(TSession session, CM_Express_Start req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			expressManager.express(player, req.getId(), req.getSign());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("运镖", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void resetExpress(TSession session, CM_Express_Reset req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			expressManager.resetSelect(player, req.isGold(), req.isAutoBuy());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("运镖", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void reward(TSession session, CM_Express_Reward req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			expressManager.reward(player, req.getSign());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("运镖", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void checkLorryInVisualRange(TSession session, CM_Faraway_Status req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			expressManager.checkLorryInVisualRange(player, req.isCanSeeLorry());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("运镖", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void getRecentOnAttackTime(TSession session, CM_Lorry_AttackTime req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			expressManager.getRecentOnAttackTime(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("运镖", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void flyToLorry(TSession session, CM_Fly_ToLorry req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			expressManager.flyToLorry(player, req.getSign());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("运镖", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void askForHelp(TSession session, CM_Ask_For_Help req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			expressManager.askForHelp(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("运镖", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void getExpressNavigator(TSession session, CM_Get_Current_Express req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			expressManager.getExpressNavigator(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("运镖", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void robSpecifiedLorry(TSession session, CM_Rob_Specified_Lorry req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			expressManager.robSpecifiedLorry(player, req.getTargetPlayerId(), req.getSign());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("运镖", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@ReceiverAnno
	public void logout(LogoutEvent logoutEvent) {
		Player player = PlayerManager.getInstance().getPlayer(logoutEvent.getOwner());
		if (player.getExpress().getCurrentLorry() != null) {
			// 副本镖车移除
			if (player.getExpress().getCurrentLorry().getSpawnKey().equals("EXPRESS_COPY")) {
				player.getExpress().getCurrentLorry().fail(false);
			}
		}
	}
}
