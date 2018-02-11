package com.mmorpg.mir.model.quest.facade;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.event.LevelUpEvent;
import com.mmorpg.mir.model.player.event.LoginEvent;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.quest.manager.QuestManager;
import com.mmorpg.mir.model.quest.packet.CM_Accept_Quest;
import com.mmorpg.mir.model.quest.packet.CM_AtOnce_Complete_Quest;
import com.mmorpg.mir.model.quest.packet.CM_CompleteAllDay_Quest;
import com.mmorpg.mir.model.quest.packet.CM_Complete_Quest;
import com.mmorpg.mir.model.quest.packet.CM_GiveUp_Quest;
import com.mmorpg.mir.model.quest.packet.CM_LevelUpStar_Quest;
import com.mmorpg.mir.model.quest.packet.CM_Reward_Quest;
import com.mmorpg.mir.model.quest.packet.SM_QuestUpdateVO;
import com.mmorpg.mir.model.quest.service.QuestService;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.SessionUtil;
import com.windforce.common.event.anno.ReceiverAnno;
import com.xiaosan.dispatcher.anno.HandlerAnno;
import com.xiaosan.socket.core.TSession;

@Component
public class QuestFacade {
	private static final Logger logger = Logger.getLogger(QuestFacade.class);

	@Autowired
	private QuestService questService;

	@Autowired
	private QuestManager questManager;

	@Autowired
	private PlayerManager playerManager;

	@HandlerAnno
	public void accept(TSession session, CM_Accept_Quest req) {
		SM_QuestUpdateVO sm = new SM_QuestUpdateVO();
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			questService.acceptQuest(player, req.getId());
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
			PacketSendUtility.sendPacket(player, sm);
		} catch (Exception e) {
			logger.error("玩家接取任务异常", e);
			sm.setCode(ManagedErrorCode.SYS_ERROR);
			PacketSendUtility.sendPacket(player, sm);
		}
	}

	@HandlerAnno
	public SM_QuestUpdateVO giveUp(TSession session, CM_GiveUp_Quest req) {
		SM_QuestUpdateVO sm = new SM_QuestUpdateVO();
		try {
			Player player = SessionUtil.getPlayerBySession(session);
			questService.giveUp(player, req.getId());
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("玩家接取任务异常", e);
			sm.setCode(ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}

	@HandlerAnno
	public SM_QuestUpdateVO reward(TSession session, CM_Reward_Quest req) {
		SM_QuestUpdateVO sm = new SM_QuestUpdateVO();
		try {
			Player player = SessionUtil.getPlayerBySession(session);
			questService.reward(player, req.getId());
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("玩家领取任务奖励异常", e);
			sm.setCode(ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}

	@HandlerAnno
	public SM_QuestUpdateVO complete(TSession session, CM_Complete_Quest req) {
		SM_QuestUpdateVO sm = new SM_QuestUpdateVO();
		try {
			Player player = SessionUtil.getPlayerBySession(session);
			questService.clientComplete(player, req.getId());
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("玩家完成任务异常", e);
			sm.setCode(ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}

	@HandlerAnno
	public void atOnceComplete(TSession session, CM_AtOnce_Complete_Quest req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			questService.atOnceComplete(player, req.getId());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("玩家完成任务异常", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void levelUpStar(TSession session, CM_LevelUpStar_Quest req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			questService.levelUpStar(player, req.getId());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("玩家完成任务异常", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void completeAllDay(TSession session, CM_CompleteAllDay_Quest req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			questService.dayQuestAllComplete(player, req.getId());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("玩家完成任务异常", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	// @HandlerAnno
	// public void refreshQuest(TSession session, CM_Refresh_Quest req) {
	// Player player = SessionUtil.getPlayerBySession(session);
	// try {
	// questManager.loginRefresh((LoginEvent)
	// LoginEvent.valueOf(player.getObjectId(), session, 0));
	// } catch (ManagedException e) {
	// PacketSendUtility.sendErrorMessage(player, e.getCode());
	// } catch (Exception e) {
	// logger.error("玩家完成任务异常", e);
	// PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
	// }
	// }

	@ReceiverAnno
	public void loginRefresh(LoginEvent event) {
		questManager.loginRefresh(event);
	}

	// @ReceiverAnno
	public void levelUp(LevelUpEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		QuestManager.getInstance().doLoginRefresh(player);
	}

}
