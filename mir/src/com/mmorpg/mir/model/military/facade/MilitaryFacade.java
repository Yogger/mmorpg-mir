package com.mmorpg.mir.model.military.facade;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.military.manager.MilitaryManager;
import com.mmorpg.mir.model.military.packet.CM_Break_MilitaryStratege;
import com.mmorpg.mir.model.military.packet.CM_Military_Star_Change;
import com.mmorpg.mir.model.military.packet.CM_strategy_upgrade;
import com.mmorpg.mir.model.military.packet.SM_Military_Star_Change;
import com.mmorpg.mir.model.military.resource.MilitaryLevelResource;
import com.mmorpg.mir.model.military.service.MilitaryService;
import com.mmorpg.mir.model.player.event.AnotherDayEvent;
import com.mmorpg.mir.model.player.event.KillPlayerEvent;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.quest.event.QuestCompletEvent;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.SessionUtil;
import com.windforce.common.event.anno.ReceiverAnno;
import com.xiaosan.dispatcher.anno.HandlerAnno;
import com.xiaosan.socket.core.TSession;

@Component
public class MilitaryFacade {

	private static final Logger logger = Logger.getLogger(MilitaryFacade.class);

	@Autowired
	private MilitaryService militaryService;
	@Autowired
	private PlayerManager playerManager;
	@Autowired
	private MilitaryManager militaryManager;

	// 完成副本任务，自动升阶
	@HandlerAnno
	public void upgradeMilitaryRank(TSession session, CM_Military_Star_Change req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			militaryService.upgradeMilitaryStar(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("玩家军衔升阶失败", e);
			PacketSendUtility.sendErrorMessage(player);
		}
	}

	@HandlerAnno
	public void upgradeMilitaryStrategy(TSession session, CM_strategy_upgrade req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			militaryService.upgradeMilitaryStrategy(player, req);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("玩家兵法升阶失败", e);
			PacketSendUtility.sendErrorMessage(player);
		}
	}
	
	@HandlerAnno
	public void breakMilitaryStrategy(TSession session, CM_Break_MilitaryStratege req){
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			militaryService.breakMilitaryStrategy(player, req);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("玩家兵法突破失败", e);
			PacketSendUtility.sendErrorMessage(player);
		}
	}

	@ReceiverAnno
	public void event(QuestCompletEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		MilitaryLevelResource resource = militaryManager.getResource(player.getMilitary().getRank());
		if (resource.getQiQuestId() == null || resource.getQiQuestId().length() == 0) {
			return;
		}
		if (resource.completeTheQuest(player.getCountryValue(), event.getQuestId())) {
			try {
				militaryService.upgradeMilitaryRank(player);
			} catch (ManagedException e) {
				PacketSendUtility.sendErrorMessage(player, e.getCode());
			}
		}
	}

	@ReceiverAnno
	public void addHonorByKill(KillPlayerEvent event) {
		Player attacker = playerManager.getPlayer(event.getOwner());
		try {
			Player player = playerManager.getPlayer(event.getKilledPlayerId());
			militaryService.killAddHonor(attacker, player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(attacker, e.getCode());
		} catch (Exception e) {
			logger.error("杀人得荣誉", e);
			PacketSendUtility.sendErrorMessage(attacker);
		}
	}
	
	@ReceiverAnno
	public void changeStarStats(AnotherDayEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		if (!militaryManager.checkNeedCost(player)) {
			return;
		}
	
		String oldStar = player.getMilitary().getStarId();
		String newStar = militaryManager.doCostDailyHonor(player);
		
		if (!oldStar.equals(newStar)) {
			player.getMilitary().setStarId(newStar);
			PacketSendUtility.sendPacket(player, SM_Military_Star_Change.valueOf(1, newStar));
			militaryManager.refreshPlayerMilitaryStarBuff(player);
		}
		
	}
}
