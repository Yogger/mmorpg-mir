package com.mmorpg.mir.model.copy.facade;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.copy.manager.CopyManager;
import com.mmorpg.mir.model.copy.model.CopyType;
import com.mmorpg.mir.model.copy.packet.CM_Clear_ExpCopyMonster;
import com.mmorpg.mir.model.copy.packet.CM_Clear_HorseEquip_Monster;
import com.mmorpg.mir.model.copy.packet.CM_Copy_Batch;
import com.mmorpg.mir.model.copy.packet.CM_Copy_Boss_Reward;
import com.mmorpg.mir.model.copy.packet.CM_Copy_BuyCount;
import com.mmorpg.mir.model.copy.packet.CM_Copy_LadderReset;
import com.mmorpg.mir.model.copy.packet.CM_Copy_LadderReward;
import com.mmorpg.mir.model.copy.packet.CM_Copy_Reward;
import com.mmorpg.mir.model.copy.packet.CM_Copy_Show_Reward;
import com.mmorpg.mir.model.copy.packet.CM_Encourage_Copy;
import com.mmorpg.mir.model.copy.packet.CM_Enter_Copy;
import com.mmorpg.mir.model.copy.packet.CM_HorseEquip_Copy_Reset;
import com.mmorpg.mir.model.copy.packet.CM_IndividualBoss_Reset;
import com.mmorpg.mir.model.copy.packet.CM_Leave_Copy;
import com.mmorpg.mir.model.copy.packet.CM_MingJiangBoss_Reset;
import com.mmorpg.mir.model.copy.packet.CM_WarBook_Copy_Reward;
import com.mmorpg.mir.model.copy.packet.CM_WarbookCopy_Reset;
import com.mmorpg.mir.model.copy.resource.CopyResource;
import com.mmorpg.mir.model.copy.service.CopyService;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.event.MonsterKillEvent;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.player.event.AnotherDayEvent;
import com.mmorpg.mir.model.player.event.LoginEvent;
import com.mmorpg.mir.model.player.event.LogoutEvent;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.quest.event.QuestCompletEvent;
import com.mmorpg.mir.model.quest.manager.QuestManager;
import com.mmorpg.mir.model.quest.resource.QuestResource;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.SessionUtil;
import com.mmorpg.mir.model.world.WorldMapInstance;
import com.windforce.common.event.anno.ReceiverAnno;
import com.xiaosan.dispatcher.anno.HandlerAnno;
import com.xiaosan.socket.core.TSession;

@Component
public class CopyFacade {

	private static Logger logger = Logger.getLogger(CopyFacade.class);
	@Autowired
	private CopyService copyService;

	@Autowired
	private PlayerManager playerManager;

	@HandlerAnno
	public void enterCopy(TSession session, CM_Enter_Copy req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			copyService.enterCopy(req.getId(), player, false);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("进入副本异常", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void warBookReward(TSession session, CM_WarBook_Copy_Reward req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			copyService.warBookReward(player, req.getId());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("购买副本双倍奖励异常", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void ladderReset(TSession session, CM_Copy_LadderReset req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			copyService.ladderReset(player, req.getSign());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("进入副本异常", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void individualBossReset(TSession session, CM_IndividualBoss_Reset req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			copyService.individualBossReset(player, req.getId());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("进入副本异常", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void mingJiangBossReset(TSession session, CM_MingJiangBoss_Reset req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			copyService.mingJiangBossReset(player, req.getId());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("名将自动扫描异常", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	// 所接收的事件
	@ReceiverAnno
	public void addMonsterKill(MonsterKillEvent event) {
		if (!event.isKnowPlayer()) {
			return;
		}
		Player player = playerManager.getPlayer(event.getOwner());
		WorldMapInstance worldMapInstance = player.getCopyHistory().getCurrentMapInstance();
		if (worldMapInstance != null) {
			worldMapInstance.getCopyInfo().addKillMonster();
		}
	}

	@HandlerAnno
	public void ladderBatch(TSession session, CM_Copy_Batch req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			copyService.batchLadder(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("进入副本异常", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void ladderReward(TSession session, CM_Copy_LadderReward req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			copyService.ladderReward(player, req.getId());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("进入副本异常", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	// @HandlerAnno
	public void bossReward(TSession session, CM_Copy_Boss_Reward req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			copyService.bossCopyReward(player, req.getId());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("进入副本异常", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void leaveCopy(TSession session, CM_Leave_Copy req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			copyService.leaveCopy(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("离开副本异常", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void encourage(TSession session, CM_Encourage_Copy req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			copyService.encourge(player, req.isGold());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("副本鼓舞异常", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void clearExpCopyMonster(TSession session, CM_Clear_ExpCopyMonster req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			copyService.clearExpCopyMonster(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("副本鼓舞异常", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void reward(TSession session, CM_Copy_Reward req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			copyService.reward(req.getId(), player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("离开副本异常", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void showReward(TSession session, CM_Copy_Show_Reward req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			copyService.showReward(player, req.getId());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("离开副本异常", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void buyCount(TSession session, CM_Copy_BuyCount req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			copyService.buyCount(player, req.getId());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("离开副本异常", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void horseEquipCopyReset(TSession session, CM_HorseEquip_Copy_Reset req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			copyService.horseCopyReset(player, req.getId());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("离开副本异常", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void clearHorseEquipMonster(TSession session, CM_Clear_HorseEquip_Monster req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			copyService.clearHorseEquipMonster(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("离开副本异常", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void warbookReset(TSession session, CM_WarbookCopy_Reset req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			copyService.warbookReset(player, req.getId(), req.isDoubled());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("离开副本异常", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@ReceiverAnno
	public void playerLogout(LogoutEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		CopyResource copyResource = player.getCopyHistory().getCurrentCopyResource();
		if (copyResource != null && copyResource.getType() == CopyType.EXP) {
			return;
		}
		copyService.leaveCopy(player);
	}

	@ReceiverAnno
	public void anotherDayEvent(AnotherDayEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		player.getCopyHistory().refresh();
	}

	@ReceiverAnno
	public void playerLogin(LoginEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		PlayerManager.getInstance().doPlayerPositionFix(player);
	}

	@ReceiverAnno
	public void questComplete(QuestCompletEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		QuestResource questResource = QuestManager.getInstance().questResources.get(event.getQuestId(), true);
		if (questResource.getCopyId() != null) {
			CopyResource copyResource = CopyManager.getInstance().copyResources.get(questResource.getCopyId(), true);
			if (copyResource.getType() == CopyType.LADDER) {
				Reward reward = copyService.mutiLadderReward(player, copyResource.getId());
				RewardManager.getInstance().grantReward(player, reward,
						ModuleInfo.valueOf(ModuleType.QUEST, SubModuleType.QUEST_REWARD));
				player.getCopyHistory().removeLadderCurrenctResetCount(copyResource.getIndex());
			} else if (copyResource.getType() == CopyType.HORSEEQUIP) {
				int questId = Integer.parseInt(event.getQuestId());
				player.getCopyHistory().setCurHorseEquipQuest(questId);
				Integer horseEquipMaxQuestHis = player.getCopyHistory().getHorseEquipMaxQuestHis()
						.get(copyResource.getId());

				if (horseEquipMaxQuestHis == null || horseEquipMaxQuestHis < questId) {
					player.getCopyHistory().getHorseEquipMaxQuestHis().put(copyResource.getId(), questId);
				}
			}
		}
	}
}
