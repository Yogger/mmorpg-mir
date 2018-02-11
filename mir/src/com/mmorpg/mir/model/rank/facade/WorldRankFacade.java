package com.mmorpg.mir.model.rank.facade;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.cliffc.high_scale_lib.NonBlockingHashSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.artifact.event.ArtifactUpEvent;
import com.mmorpg.mir.model.combatspirit.event.CombatSpiritUpEvent;
import com.mmorpg.mir.model.combatspirit.manager.CombatSpiritManager;
import com.mmorpg.mir.model.combatspirit.model.CombatSpiritStorage.CombatSpiritType;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.copy.event.LadderNewRecordEvent;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.event.BattleScoreRefreshEvent;
import com.mmorpg.mir.model.gang.event.PlayerGangChangeEvent;
import com.mmorpg.mir.model.horse.event.HorseGradeUpEvent;
import com.mmorpg.mir.model.military.event.MilitaryRankUpEvent;
import com.mmorpg.mir.model.moduleopen.manager.ModuleOpenManager;
import com.mmorpg.mir.model.openactive.manager.OpenActiveManager;
import com.mmorpg.mir.model.player.event.AnotherDayEvent;
import com.mmorpg.mir.model.player.event.LevelUpEvent;
import com.mmorpg.mir.model.player.event.LoginEvent;
import com.mmorpg.mir.model.player.event.PlayerDieEvent;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.promote.event.PromotionEvent;
import com.mmorpg.mir.model.rank.manager.WorldRankManager;
import com.mmorpg.mir.model.rank.model.DayKey;
import com.mmorpg.mir.model.rank.model.RankRow;
import com.mmorpg.mir.model.rank.model.RankType;
import com.mmorpg.mir.model.rank.packet.CM_Get_Hero_Reward;
import com.mmorpg.mir.model.rank.packet.CM_Get_Power_Player;
import com.mmorpg.mir.model.rank.packet.CM_Get_Rank;
import com.mmorpg.mir.model.seal.event.SealUpGradeEvent;
import com.mmorpg.mir.model.soul.event.SoulUpgradeEvent;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.SessionUtil;
import com.mmorpg.mir.model.warbook.event.WarbookUpGradeEvent;
import com.windforce.common.event.anno.ReceiverAnno;
import com.windforce.common.utility.DateUtils;
import com.xiaosan.dispatcher.anno.HandlerAnno;
import com.xiaosan.socket.core.TSession;

@Component
public class WorldRankFacade {
	private static Logger logger = Logger.getLogger(WorldRankFacade.class);

	@Autowired
	private PlayerManager playerManager;

	@Autowired
	private WorldRankManager worldRankManager;

	@Autowired
	private ModuleOpenManager moduleOpenManager;

	@ReceiverAnno
	public void handleLevelUp(LevelUpEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		worldRankManager.submitRankRow(player, RankType.LEVEL, event);
	}

	@ReceiverAnno
	public void handleBattleScoreUp(BattleScoreRefreshEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		int countryValue = player.getCountryValue();
		if (!player.getOperatorPool().getGmPrivilege().isGm() && event.isBecomeMorePower()) {
			worldRankManager.submitCountryRankRow(countryValue, event);
		}
		if (event.isBecomeMorePower()) {
			worldRankManager.submitOpenServerRankRow(countryValue, event);
		}
		if (player.getLevel() >= worldRankManager.requiredLevel.getValue()) {
			RankType type = RankType.COMPOSITE_BATTLESCORE;
			worldRankManager.submitRankRow(player, type, event);
		}
	}

	@HandlerAnno
	public void getCountryPowerPlayer(TSession session, CM_Get_Power_Player req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			worldRankManager.searchCountryPowerPlayer(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("取排行榜出错了", e);
			PacketSendUtility.sendErrorMessage(player);
		}
	}

	@ReceiverAnno
	public void horseGradeUp(HorseGradeUpEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		worldRankManager.submitRankRow(player, RankType.HORSE, event);
	}

	@ReceiverAnno
	public void warbookUp(WarbookUpGradeEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		worldRankManager.submitRankRow(player, RankType.WARBOOK, event);
	}

	@ReceiverAnno
	public void militaryGradeUp(MilitaryRankUpEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		worldRankManager.submitRankRow(player, RankType.MILITARY, event);
	}

	@ReceiverAnno
	public void artifactGradeUp(ArtifactUpEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		worldRankManager.submitRankRow(player, RankType.ARTIFACT, event);
	}

	@ReceiverAnno
	public void soulGradeUp(SoulUpgradeEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		worldRankManager.submitRankRow(player, RankType.SOUL, event);
	}

	@ReceiverAnno
	public void updateHeroRank(PlayerDieEvent event) {
		Player killed = playerManager.getPlayer(event.getOwner());
		Player killer = playerManager.getPlayer(event.getAttackerId());
		if (killer.getLevel() < worldRankManager.requiredLevel.getValue()) {
			return;
		}
		if (killed.getLevel() < worldRankManager.requiredLevel.getValue()) {
			return;
		}
		Long key = DayKey.valueOf().getLunchTime();
		if (!killer.getRankInfo().getSlaughterHistory().containsKey(key)) {
			killer.getRankInfo().getSlaughterHistory().put(key, new NonBlockingHashSet<Long>());
		}
		if (killer.getRankInfo().getSlaughterHistory().get(key).add(killed.getObjectId())) {
			int todayKill = killer.getRankInfo().getSlaughterHistory().get(key).size();
			event.setExtra(todayKill);
			event.setPlayerId(killer.getObjectId());
			int countryValue = killer.getCountryValue();
			RankType type = worldRankManager.getHeroType(countryValue, true);
			if (DateUtils.isSameDay(new Date(worldRankManager.getRankTypeRefreshTime(type)), new Date(key))) {
				worldRankManager.submitRankRow(killer, type, event);
			}
			OpenActiveManager.getInstance().handleCountryHero(event, killer);
		}
	}

	@ReceiverAnno
	public void ladderNewRecord(LadderNewRecordEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		if (player.getLevel() < worldRankManager.requiredLevel.getValue()) {
			return;
		}
		if ((event.getLadderLayer() == player.getRankInfo().getLayer() && event.getConsumeTime() < player.getRankInfo()
				.getCost()) || event.getLadderLayer() > player.getRankInfo().getLayer()) {
			player.getRankInfo().setCost(event.getConsumeTime());
			player.getRankInfo().setLayer(event.getLadderLayer());
			worldRankManager.submitRankRow(player, RankType.LADDER, event);
		}
	}

	@ReceiverAnno
	public void combatSpiritRank(CombatSpiritUpEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		RankType type = null;
		if (event.getType() == CombatSpiritType.MEDAL.getValue()) {
			type = RankType.MEDAL;
		} else if (event.getType() == CombatSpiritType.PROTECTRUNE.getValue()) {
			type = RankType.PROTECTURE;
		} else if (event.getType() == CombatSpiritType.TREASURE.getValue()) {
			type = RankType.TREASURE;
		}
		worldRankManager.submitRankRow(player, type, event);
	}
	
	@ReceiverAnno
	public void sealUpRank(SealUpGradeEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		worldRankManager.submitRankRow(player, RankType.SEAL, event);
	}

	@ReceiverAnno
	public void changeHeroElement(PlayerGangChangeEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		Map<Integer, RankRow> rank = worldRankManager.getHeroRank(player, true);
		if (rank != null) {
			RankType type = worldRankManager.getHeroType(player.getCountryValue(), true);
			worldRankManager.changeMyRankRowInfo(type, event);
		}
		Map<Integer, RankRow> yesterdayRank = worldRankManager.getHeroRank(player, false);
		if (yesterdayRank != null) {
			RankType type = worldRankManager.getHeroType(player.getCountryValue(), false);
			worldRankManager.changeMyRankRowInfo(type, event);
		}
	}

	@ReceiverAnno
	public void changeRankIfExist(PromotionEvent event) {
		worldRankManager.refreshByPromotionEvent(event);
	}

	@HandlerAnno
	public void getWorldRank(TSession session, CM_Get_Rank req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			worldRankManager.getRankByType(player,
					RankType.valueOf(req.getRankType()).getCountryRank(player.getCountryValue()), req.getPage());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("取排行榜出错了", e);
			PacketSendUtility.sendErrorMessage(player);
		}
	}

	@HandlerAnno
	public void getYesterdayHeroReward(TSession session, CM_Get_Hero_Reward req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			worldRankManager.rewardHero(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("取排行榜出错了", e);
			PacketSendUtility.sendErrorMessage(player);
		}
	}

	@ReceiverAnno
	public void refreshCountryRank(AnotherDayEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		if (player.getOperatorPool().getGmPrivilege().isGm()) {
			return;
		}
		BattleScoreRefreshEvent refreshEvent = BattleScoreRefreshEvent.valueOf(event.getOwner(), player.getLevel(),
				player.getGameStats().calcBattleScore(), true);
		worldRankManager.submitCountryRankRow(player.getCountryValue(), refreshEvent);
	}

	@ReceiverAnno
	public void mergeServerLoginRefresh(LoginEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		Map<String, String> map = CombatSpiritManager.getInstance().COMBAT_SPIRIT_INIT.getValue();
		for (Entry<String, String> entry : map.entrySet()) {
			if (moduleOpenManager.isOpenByKey(player, entry.getKey())) {
				CombatSpiritType type = CombatSpiritType.valueOf(entry.getValue());
				combatSpiritRank((CombatSpiritUpEvent) CombatSpiritUpEvent.valueOf(player.getObjectId(), player
						.getCombatSpiritStorage().getCombatSpiritCollection().get(type.getValue())
						.getCombatResourceId(), type.getValue()));
			}
		}

		if (moduleOpenManager.isOpenByModuleKey(player, ModuleKey.HORSE)) {
			worldRankManager.submitRankRow(player, RankType.HORSE, HorseGradeUpEvent.valueOf(player));
		}
		if (moduleOpenManager.isOpenByModuleKey(player, ModuleKey.SOUL_PF)) {
			worldRankManager.submitRankRow(player, RankType.SOUL, SoulUpgradeEvent.valueOf(player));
		}
		if (moduleOpenManager.isOpenByModuleKey(player, ModuleKey.ARTIFACT)) {
			worldRankManager.submitRankRow(player, RankType.ARTIFACT, ArtifactUpEvent.valueOf(player));
		}
		if (moduleOpenManager.isOpenByModuleKey(player, ModuleKey.WARBOOK)) {
			worldRankManager.submitRankRow(player, RankType.WARBOOK, WarbookUpGradeEvent.valueOf(player));
		}
		worldRankManager.submitRankRow(player, RankType.LEVEL,
				LevelUpEvent.valueOf(player.getObjectId(), player.getLevel(), player.getName()));

		if (player.getMilitary().getRank() != 0) {
			worldRankManager.submitRankRow(player, RankType.MILITARY,
					MilitaryRankUpEvent.valueOf(player.getObjectId(), player.getMilitary().getRank()));
		}
		
		if (moduleOpenManager.isOpenByModuleKey(player, ModuleKey.SEAL)) {
			worldRankManager.submitRankRow(player, RankType.SEAL, SealUpGradeEvent.valueOf(player));
		}

		if (player.getRankInfo().getLayer() != 0) {
			ladderNewRecord(LadderNewRecordEvent.valueOf(player.getObjectId(), player.getRankInfo().getLayer(), player
					.getRankInfo().getCost()));
		}
	}

}
