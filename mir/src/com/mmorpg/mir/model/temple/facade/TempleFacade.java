package com.mmorpg.mir.model.temple.facade;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.country.resource.ConfigValueManager;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.event.AnotherDayEvent;
import com.mmorpg.mir.model.player.event.LoginEvent;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.temple.manager.TempleManager;
import com.mmorpg.mir.model.temple.model.TempleHistory;
import com.mmorpg.mir.model.temple.packet.CM_Query_Temple_Status;
import com.mmorpg.mir.model.temple.packet.CM_Temple_AccpetQuest;
import com.mmorpg.mir.model.temple.packet.CM_Temple_Change_Brick;
import com.mmorpg.mir.model.temple.packet.CM_Temple_Put_Brick;
import com.mmorpg.mir.model.temple.packet.CM_Temple_StartTake_Brick;
import com.mmorpg.mir.model.temple.packet.CM_Temple_Take_Brick;
import com.mmorpg.mir.model.temple.packet.SM_Temple_StartTake_Brick;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.SessionUtil;
import com.windforce.common.event.anno.ReceiverAnno;
import com.windforce.common.utility.DateUtils;
import com.xiaosan.dispatcher.anno.HandlerAnno;
import com.xiaosan.socket.core.TSession;

@Component
public class TempleFacade {
	private static Logger logger = Logger.getLogger(TempleFacade.class);
	@Autowired
	private PlayerManager playerManager;

	@Autowired
	private ConfigValueManager configValueManager;

	@Autowired
	private TempleManager templeManager;

	@HandlerAnno
	public void endTakeBrick(TSession session, CM_Temple_Take_Brick req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			templeManager.endTakeBrick(player, req.getCountry());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("任命官员", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public SM_Temple_StartTake_Brick startTakeBrick(TSession session, CM_Temple_StartTake_Brick req) {
		SM_Temple_StartTake_Brick sm = new SM_Temple_StartTake_Brick();
		try {
			Player player = SessionUtil.getPlayerBySession(session);
			if (player != null) {
				sm = templeManager.startBrick(player, req.getCountry());
			}
			return sm;
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("玩家开始搬砖异常", e);
			sm.setCode(ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}

	@HandlerAnno
	public void accpetQuest(TSession session, CM_Temple_AccpetQuest req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			templeManager.acceptQuest(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("任命官员", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void changeBrick(TSession session, CM_Temple_Change_Brick req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			templeManager.changeBrick(player, req.isUseGold());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("任命官员", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void putBrick(TSession session, CM_Temple_Put_Brick req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			templeManager.putBrick(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("搬砖", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void queryTempleStatus(TSession session, CM_Query_Temple_Status req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			templeManager.queryTempleStatus(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("查看搬砖状态", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@ReceiverAnno
	public void login(LoginEvent loginEvent) {
		Player player = playerManager.getPlayer(loginEvent.getOwner());
		// 板砖过期
		if (!DateUtils.isToday(new Date(player.getTempleHistory().getLastRefreshTime()))) {
			player.getTempleHistory().timeFail(0);
		} else {
			player.getTempleHistory().timeFail(configValueManager.COUNTRY_TEMPLE_BRICK_DEADTIME.getValue());
		}
		player.getTempleHistory().refresh();

		// 没有砖块，去掉标识BUFF
		if (player.getTempleHistory().getCurrentBrick() == null
				&& player.getEffectController().contains(TempleHistory.BRICK_BUFF_GROUP)) {
			player.getEffectController().removeEffect(TempleHistory.BRICK_BUFF_GROUP);
		}
	}

	@ReceiverAnno
	public void anotherDay(AnotherDayEvent anotherDayEvent) {
		Player player = playerManager.getPlayer(anotherDayEvent.getOwner());
		// 板砖过期
		player.getTempleHistory().timeFail(0);
		player.getTempleHistory().refresh();

		// 没有砖块，去掉标识BUFF
		if (player.getTempleHistory().getCurrentBrick() == null
				&& player.getEffectController().contains(TempleHistory.BRICK_BUFF_GROUP)) {
			player.getEffectController().removeEffect(TempleHistory.BRICK_BUFF_GROUP);
		}
	}

}
