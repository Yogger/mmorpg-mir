package com.mmorpg.mir.model.openactive.facade;

import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;
import org.cliffc.high_scale_lib.NonBlockingHashMap;
import org.cliffc.high_scale_lib.NonBlockingHashSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.artifact.event.ArtifactUpEvent;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.country.event.ClawbackFlagFinishEvent;
import com.mmorpg.mir.model.country.event.CountryFlagQuestFinishEvent;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.event.BattleScoreRefreshEvent;
import com.mmorpg.mir.model.horse.event.HorseGradeUpEvent;
import com.mmorpg.mir.model.item.event.EquipEquipmentEvent;
import com.mmorpg.mir.model.item.event.ExtendsEquipmentEvent;
import com.mmorpg.mir.model.item.event.UnEquipEquipmentEvent;
import com.mmorpg.mir.model.military.event.MilitaryRankUpEvent;
import com.mmorpg.mir.model.openactive.OpenActiveConfig;
import com.mmorpg.mir.model.openactive.manager.OpenActiveManager;
import com.mmorpg.mir.model.openactive.model.ActivityEnum;
import com.mmorpg.mir.model.openactive.model.CompeteRankValue;
import com.mmorpg.mir.model.openactive.model.GroupPurchase;
import com.mmorpg.mir.model.openactive.model.GroupPurchaseThree;
import com.mmorpg.mir.model.openactive.model.GroupPurchaseTwo;
import com.mmorpg.mir.model.openactive.model.vo.CompeteVO;
import com.mmorpg.mir.model.openactive.model.vo.OldCompeteVO;
import com.mmorpg.mir.model.openactive.packet.CM_ArtifactUpgrade_Draw_Reward;
import com.mmorpg.mir.model.openactive.packet.CM_Celebrate_Reward_Firework;
import com.mmorpg.mir.model.openactive.packet.CM_CollectItem_Receive_Reward;
import com.mmorpg.mir.model.openactive.packet.CM_Draw_CelebrateReward;
import com.mmorpg.mir.model.openactive.packet.CM_Draw_CompeteReward;
import com.mmorpg.mir.model.openactive.packet.CM_Draw_OldCompeteReward;
import com.mmorpg.mir.model.openactive.packet.CM_EnhenacePower_Draw_Reward;
import com.mmorpg.mir.model.openactive.packet.CM_EquipActive_Draw_Reward;
import com.mmorpg.mir.model.openactive.packet.CM_EveryDayRecharge_Draw_Reward;
import com.mmorpg.mir.model.openactive.packet.CM_ExpActive_Draw_Reward;
import com.mmorpg.mir.model.openactive.packet.CM_GroupPurchase_GetInfo;
import com.mmorpg.mir.model.openactive.packet.CM_GroupPurchase_Reward;
import com.mmorpg.mir.model.openactive.packet.CM_GroupPurchase_Three_GetInfo;
import com.mmorpg.mir.model.openactive.packet.CM_GroupPurchase_Three_Reward;
import com.mmorpg.mir.model.openactive.packet.CM_GroupPurchase_Two_GetInfo;
import com.mmorpg.mir.model.openactive.packet.CM_GroupPurchase_Two_Reward;
import com.mmorpg.mir.model.openactive.packet.CM_HorseUpgrade_Draw_Reward;
import com.mmorpg.mir.model.openactive.packet.CM_Old_SoulUpgrade_Draw_Reward;
import com.mmorpg.mir.model.openactive.packet.CM_Public_Test_Gift_Reward;
import com.mmorpg.mir.model.openactive.packet.CM_Query_CompeteStatus;
import com.mmorpg.mir.model.openactive.packet.CM_Query_CountryFlagStatus;
import com.mmorpg.mir.model.openactive.packet.CM_Query_OldCompeteStatus;
import com.mmorpg.mir.model.openactive.packet.CM_SoulUpgrade_Draw_Reward;
import com.mmorpg.mir.model.openactive.packet.SM_CountryFlag_Status;
import com.mmorpg.mir.model.openactive.packet.SM_Draw_CelebrateReward;
import com.mmorpg.mir.model.openactive.packet.SM_GroupPurchaseInfo;
import com.mmorpg.mir.model.openactive.packet.SM_GroupPurchaseThreeInfo;
import com.mmorpg.mir.model.openactive.packet.SM_GroupPurchaseTwoInfo;
import com.mmorpg.mir.model.openactive.packet.SM_OldSevenCompete_Count;
import com.mmorpg.mir.model.openactive.packet.SM_Public_Test_Gift_Reward;
import com.mmorpg.mir.model.openactive.packet.SM_Public_Test_Info;
import com.mmorpg.mir.model.openactive.packet.SM_Query_CompeteStatus;
import com.mmorpg.mir.model.openactive.packet.SM_Query_OldCompeteStatus;
import com.mmorpg.mir.model.openactive.packet.SM_SevenCompete_Count;
import com.mmorpg.mir.model.openactive.packet.SM_TodayRecharge;
import com.mmorpg.mir.model.openactive.resource.OpenActiveCompeteResource;
import com.mmorpg.mir.model.player.event.AnotherDayEvent;
import com.mmorpg.mir.model.player.event.LevelUpEvent;
import com.mmorpg.mir.model.player.event.LoginEvent;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.purse.event.RechargeRewardEvent;
import com.mmorpg.mir.model.purse.model.CurrencyType;
import com.mmorpg.mir.model.rank.manager.WorldRankManager;
import com.mmorpg.mir.model.rank.model.RankType;
import com.mmorpg.mir.model.serverstate.ServerState;
import com.mmorpg.mir.model.soul.event.SoulUpgradeEvent;
import com.mmorpg.mir.model.system.packet.SM_System_Message;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.SessionUtil;
import com.mmorpg.mir.model.welfare.event.CurrencyActionEvent;
import com.mmorpg.mir.model.welfare.event.EnhanceEquipmentEvent;
import com.windforce.common.event.anno.ReceiverAnno;
import com.xiaosan.dispatcher.anno.HandlerAnno;
import com.xiaosan.socket.core.TSession;

@Component
public class OpenActiveFacade {

	private static final Logger logger = Logger.getLogger(OpenActiveFacade.class);

	@Autowired
	private OpenActiveManager openActiveManager;

	@Autowired
	private OpenActiveConfig openActiveConfig;

	@Autowired
	private PlayerManager playerManager;

	@HandlerAnno
	public void drawExpActiveReward(TSession session, CM_ExpActive_Draw_Reward req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			openActiveManager.drawExpActive(player, req.getResourceId(), req.getSign());
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("领取经验放松奖励出现未知异常", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void drawEquipActiveReward(TSession session, CM_EquipActive_Draw_Reward req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			openActiveManager.drawEquipActive(player, req.getResourceId(), req.getSign());
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("领取橙色灵魂礼包出现未知异常", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void drawEnhanceActiveReward(TSession session, CM_EnhenacePower_Draw_Reward req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			openActiveManager.drawEnhanceEquipActive(player, req.getResourceId(), req.getSign());
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("领取橙色灵魂礼包出现未知异常", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void drawEveryDayRechargeReward(TSession session, CM_EveryDayRecharge_Draw_Reward req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			openActiveManager.drawEveryDayRecharge(player, req.getCode());
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("领取每日充值奖励出现未知异常", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void drawHorseUpgradeReward(TSession session, CM_HorseUpgrade_Draw_Reward req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			openActiveManager.drawHorseUpgradeActiveReward(player, req.getCode(), req.getSign());
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("领取全民强化奖励出现未知异常", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void drawSoulUpgradeReward(TSession session, CM_SoulUpgrade_Draw_Reward req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			openActiveManager.drawSoulUpgradeActiveReward(player, req.getCode(), req.getSign());
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("领取全民强化奖励出现未知异常", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void drawOldSoulUpgradeReward(TSession session, CM_Old_SoulUpgrade_Draw_Reward req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			openActiveManager.drawOldSoulUpgradeActiveReward(player, req.getCode());
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("领取全民强化奖励出现未知异常", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void drawArtifactUpgradeReward(TSession session, CM_ArtifactUpgrade_Draw_Reward req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			openActiveManager.drawArtifactUpgradeActiveReward(player, req.getCode(), req.getSign());
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("领取神兵进阶活动奖励出现未知异常", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void recieveCompeteReward(TSession session, CM_Draw_CompeteReward req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			openActiveManager.drawCompeteReward(player, req.getId());
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("领取七日竞技活动奖励出现未知异常", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void recieveOldCompeteReward(TSession session, CM_Draw_OldCompeteReward req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			openActiveManager.drawOldCompeteReward(player, req.getId());
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("领取七日竞技活动奖励出现未知异常", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void getSevenCompeteStatus(TSession session, CM_Query_CompeteStatus req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			Map<Integer, CompeteVO> ret = openActiveManager.getCompeteRewardStatus(player);
			PacketSendUtility.sendPacket(player, SM_Query_CompeteStatus.valueOf(ret));
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("查看七日竞技活动出现未知异常", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void getOldSevenCompeteStatus(TSession session, CM_Query_OldCompeteStatus req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			Map<Integer, OldCompeteVO> ret = openActiveManager.getOldCompeteRewardStatus(player);
			PacketSendUtility.sendPacket(player, SM_Query_OldCompeteStatus.valueOf(ret));
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("查看七日竞技活动出现未知异常", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void getCountryFlagStatus(TSession session, CM_Query_CountryFlagStatus req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			PacketSendUtility.sendPacket(player,
					SM_CountryFlag_Status.valueOf(player.getOpenActive().getCountryFlagActive()));
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("查看七日竞技活动出现未知异常", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void receiveCollectItemReward(TSession session, CM_CollectItem_Receive_Reward req) {
		Player player = SessionUtil.getPlayerBySession(session);
		if (req.getCount() <= 0) {
			return;
		}
		try {
			openActiveManager
					.receiveCollectItemActiveReward(player, req.getSign(), req.getResourceId(), req.getCount());
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("领取集字活动奖励出现未知异常", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void getPublicTestGiftReward(TSession session, CM_Public_Test_Gift_Reward req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			SM_Public_Test_Gift_Reward res = openActiveManager.receiveNextPublicTestGiftReward(player, req.getGiftId());
			PacketSendUtility.sendPacket(player, res);
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("公测献礼活动奖励出现异常", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void receiveGroupPurchaseReward(TSession session, CM_GroupPurchase_Reward req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			openActiveManager.receiveGroupPurchaseReward(player, req.getResourceId());
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("领取公测超值团购活动奖励出现未知异常", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void receiveGroupPurchaseTwoReward(TSession session, CM_GroupPurchase_Two_Reward req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			openActiveManager.receiveGroupPurchaseTwoReward(player, req.getResourceId());
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("领取超值团购活动2奖励出现未知异常", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void receiveGroupPurchaseThreeReward(TSession session, CM_GroupPurchase_Three_Reward req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			openActiveManager.receiveGroupPurchaseThreeReward(player, req.getResourceId());
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("领取超值团购活动2奖励出现未知异常", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void queryGroupPurchaseInfo(TSession session, CM_GroupPurchase_GetInfo req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			SM_GroupPurchaseInfo result = openActiveManager.getGroupPurchaseInfo(player);
			PacketSendUtility.sendPacket(player, result);
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("获取公测超值团购活动信息出现未知异常", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void queryGroupPurchaseTwoInfo(TSession session, CM_GroupPurchase_Two_GetInfo req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			SM_GroupPurchaseTwoInfo result = openActiveManager.getGroupPurchaseTwoInfo(player);
			PacketSendUtility.sendPacket(player, result);
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("获取超值团购活动2信息出现未知异常", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void queryGroupPurchaseThreeInfo(TSession session, CM_GroupPurchase_Three_GetInfo req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			SM_GroupPurchaseThreeInfo result = openActiveManager.getGroupPurchaseThreeInfo(player);
			PacketSendUtility.sendPacket(player, result);
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("获取超值团购活动3信息出现未知异常", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void rewardCelebrateFirework(TSession session, CM_Celebrate_Reward_Firework req) {
		Player player = SessionUtil.getPlayerBySession(session);
		if (req.getCount() < 1 && req.getCount() > 9999) {
			return;
		}
		try {
			openActiveManager.rewardCelebrateFirework(player, req.getCount());
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("获取超值团购活动3信息出现未知异常", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@ReceiverAnno
	public void consumeGoldActiveEvent(CurrencyActionEvent event) {
		if (event.getType() == CurrencyType.GOLD) {
			Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
			OpenActiveCompeteResource resource = openActiveConfig
					.getSpecifiedRankTypeResource(RankType.ACTIVITY_CONSUME_TYPE.getValue());
			if (resource.getLogDataConditions().verify(player)) {
				player.getOpenActive().getConsumeActive().addConsumeGold(event.getValue());
			}

			if (resource.getEnterRankConditions().verify(player, false)) {
				event.setActivityValue(player.getOpenActive().getConsumeActive().getConsumeGold());
				WorldRankManager.getInstance().submitRankRow(player, RankType.ACTIVITY_CONSUME_TYPE, event);
			}

			openActiveManager.checkAndSendRewardStatus(player, CompeteRankValue.CONSUME_RANK.getRankTypeValue());
		}
	}

	@ReceiverAnno
	public void levelUpActiveEvent(LevelUpEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		OpenActiveCompeteResource resource = openActiveConfig.getSpecifiedRankTypeResource(RankType.ACTIVITY_LEVEL_TYPE
				.getValue());
		if (resource.getLogDataConditions().verify(player)) {
			player.getOpenActive().getLevelActive().setLevel(event.getLevel());
		}

		if (resource.getEnterRankConditions().verify(player, false)) {
			WorldRankManager.getInstance().submitRankRow(player, RankType.ACTIVITY_LEVEL_TYPE, event);
		}

		openActiveManager.checkAndSendRewardStatus(player, CompeteRankValue.LEVEL_RANK.getRankTypeValue());
	}

	@ReceiverAnno
	public void rechargeEvent(RechargeRewardEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		PacketSendUtility.sendPacket(player, SM_TodayRecharge.valueOf(player.getVip().dayTotalCharge(new Date())));
		if (OpenActiveConfig.getInstance().getEveryDayRechargeDurationCond().verify(player, false)) {
			player.getOpenActive().getEveryDayRecharge().refreshNotReward();
		}

		if (OpenActiveConfig.getInstance().getGroupPurchaseTimeConds().verify(player, false)) {
			if (ServerState.getInstance().getPlayerGroupPurchases() != null) {
				GroupPurchase groupPurchase = ServerState.getInstance().getPlayerGroupPurchases().get(event.getOwner());
				if (null == groupPurchase) {
					groupPurchase = GroupPurchase.valueOf();
					ServerState.getInstance().getPlayerGroupPurchases().put(event.getOwner(), groupPurchase);
				}
				groupPurchase.addGold(player, event.getAmount());
			}
		}

		if (OpenActiveConfig.getInstance().getGroupPurchaseTwoTimeConds().verify(player, false)) {
			NonBlockingHashMap<String, NonBlockingHashSet<Long>> map = ServerState.getInstance()
					.getGroupPurchasePlayers2();
			if (map != null) {
				GroupPurchaseTwo groupPurchaseTwo = ServerState.getInstance().getPlayerGroupPurchases2()
						.get(event.getOwner());
				if (null == groupPurchaseTwo) {
					groupPurchaseTwo = GroupPurchaseTwo.valueOf();
					ServerState.getInstance().getPlayerGroupPurchases2().put(event.getOwner(), groupPurchaseTwo);
				}
				groupPurchaseTwo.addGold(player, event.getAmount());
			}
		}

		if (OpenActiveConfig.getInstance().getGroupPurchaseThreeTimeConds().verify(player, false)) {
			NonBlockingHashMap<String, NonBlockingHashSet<Long>> map = ServerState.getInstance()
					.getGroupPurchasePlayers3();
			if (map != null) {
				GroupPurchaseThree groupPurchaseThree = ServerState.getInstance().getPlayerGroupPurchases3()
						.get(event.getOwner());
				if (null == groupPurchaseThree) {
					groupPurchaseThree = GroupPurchaseThree.valueOf();
					ServerState.getInstance().getPlayerGroupPurchases3().put(event.getOwner(), groupPurchaseThree);
				}
				groupPurchaseThree.addGold(player, event.getAmount());
			}
		}

		if (ServerState.getInstance().isCelebrateActivityOpen(ActivityEnum.RECHARGE)) {
			ServerState.getInstance().addCelebrateRecharegeConsume(player, event.getAmount());
		}
		playerManager.updatePlayer(player);
	}

	@ReceiverAnno
	public void horseUpgradeEvent(HorseGradeUpEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		if (OpenActiveConfig.getInstance().getHorseActiveDurationCond().verify(player, false)) {
			player.getOpenActive().getHorseUpgradeActive().setMaxGrade(event.getGrade());
		}
		OpenActiveCompeteResource resource = openActiveConfig.getSpecifiedRankTypeResource(RankType.ACTIVITY_HORSE
				.getValue());
		if (resource.getLogDataConditions().verify(player)) {
			player.getOpenActive().getHorseUpgradeActive().setRankMaxGrade(event.getGrade());
		}

		if (resource.getEnterRankConditions().verify(player, false)) {
			WorldRankManager.getInstance().submitRankRow(player, RankType.ACTIVITY_HORSE, event);
		}

		openActiveManager.checkAndSendRewardStatus(player, CompeteRankValue.HORSE_RANK.getRankTypeValue());
	}

	@ReceiverAnno
	public void soulUpgradeEvent(SoulUpgradeEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		openActiveManager.soulUpgradeEventHandler(player, event, true);
	}

	@ReceiverAnno
	public void equipEquipment(EquipEquipmentEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		OpenActiveCompeteResource resource = openActiveConfig
				.getSpecifiedRankTypeResource(RankType.ACTIVITY_ENHANCEEQUIP.getValue());
		int nowEnhance = 0;
		if (resource.getLogDataConditions().verify(player)) {
			nowEnhance = player.getOpenActive().getEnhanceActive().updateMaxCount(player);
		}
		if (resource.getEnterRankConditions().verify(player, false) && nowEnhance != 0) {
			event.setEnhanceAllCount(nowEnhance);
			WorldRankManager.getInstance().submitRankRow(player, RankType.ACTIVITY_ENHANCEEQUIP, event);
		}

		if (openActiveConfig.getStarItemRewardConds().verify(player, false)) {
			openActiveManager.rewardStarItem(player);
		}

		openActiveManager.checkAndSendRewardStatus(player, CompeteRankValue.ENHANCE_RANK.getRankTypeValue());

		if (openActiveConfig.getEnhancePowerLogConditions().verify(player) || !ServerState.getInstance().isOpenServer()) {
			player.getOpenActive().getEnhancePowerActive()
					.updatePower(player.getGameStats().calcEquipmentEnhanceBattleSorce());
		}
	}

	@ReceiverAnno
	public void enhanceUpgrade(EnhanceEquipmentEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		OpenActiveCompeteResource resource = openActiveConfig
				.getSpecifiedRankTypeResource(RankType.ACTIVITY_ENHANCEEQUIP.getValue());
		int nowEnhance = 0;
		if (resource.getLogDataConditions().verify(player)) {
			nowEnhance = player.getOpenActive().getEnhanceActive().updateMaxCount(player);
		}
		if (resource.getEnterRankConditions().verify(player, false) && nowEnhance != 0) {
			event.setEnhanceAllCount(nowEnhance);
			WorldRankManager.getInstance().submitRankRow(player, RankType.ACTIVITY_ENHANCEEQUIP, event);
		}
		if (openActiveConfig.getStarItemRewardConds().verify(player, false)) {
			openActiveManager.rewardStarItem(player);
		}

		openActiveManager.checkAndSendRewardStatus(player, CompeteRankValue.ENHANCE_RANK.getRankTypeValue());

		if (openActiveConfig.getEnhancePowerLogConditions().verify(player) || !ServerState.getInstance().isOpenServer()) {
			player.getOpenActive().getEnhancePowerActive()
					.updatePower(player.getGameStats().calcEquipmentEnhanceBattleSorce());
		}
	}

	@ReceiverAnno
	public void unEquipEquipment(UnEquipEquipmentEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		openActiveManager.checkAndSendRewardStatus(player, CompeteRankValue.ENHANCE_RANK.getRankTypeValue());
	}

	@ReceiverAnno
	public void transferEnhanceLevel(ExtendsEquipmentEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		if (openActiveConfig.getEnhancePowerLogConditions().verify(player)) {
			player.getOpenActive().getEnhancePowerActive()
					.updatePower(player.getGameStats().calcEquipmentEnhanceBattleSorce());
		}
	}

	@ReceiverAnno
	public void artifactUpgrade(ArtifactUpEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		if (OpenActiveConfig.getInstance().getArtifactActiveDurationCond().verify(player, false)) {
			player.getOpenActive().getArtifactActive().setMaxGrade(event.getGrade());
		}
		OpenActiveCompeteResource resource = openActiveConfig.getSpecifiedRankTypeResource(RankType.ACTIVITY_ARTIFACT
				.getValue());
		if (resource.getLogDataConditions().verify(player)) {
			player.getOpenActive().getArtifactActive().setRankMaxGrade(event.getGrade());
		}

		if (resource.getEnterRankConditions().verify(player)) {
			WorldRankManager.getInstance().submitRankRow(player, RankType.ACTIVITY_ARTIFACT, event);
		}

		openActiveManager.checkAndSendRewardStatus(player, CompeteRankValue.ARTIFACT_RANK.getRankTypeValue());
	}

	@ReceiverAnno
	public void battleScoreUp(BattleScoreRefreshEvent event) {
		if (!event.isBecomeMorePower()) {
			return;
		}
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		OpenActiveCompeteResource resource = openActiveConfig.getSpecifiedRankTypeResource(RankType.ACTIVITY_FIGHTPOWER
				.getValue());
		boolean update = false;
		if (resource.getLogDataConditions().verify(player, false)) {
			update = player.getOpenActive().getFightPowerActive().updateFightPower(event);
		}
		if (resource.getEnterRankConditions().verify(player, false) && update) {
			WorldRankManager.getInstance().submitRankRow(player, RankType.ACTIVITY_FIGHTPOWER, event);
		}

		openActiveManager.checkAndSendRewardStatus(player, CompeteRankValue.FIGHTPOWER_RANK.getRankTypeValue());
	}

	@ReceiverAnno
	public void militaryUpgrade(MilitaryRankUpEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		OpenActiveCompeteResource resource = openActiveConfig.getSpecifiedRankTypeResource(RankType.ACTIVITY_MILITARY
				.getValue());
		if (resource.getLogDataConditions().verify(player, false)) {
			player.getOpenActive().getMilitaryActive().setRank(event.getRank());
		}

		if (resource.getEnterRankConditions().verify(player, false)) {
			WorldRankManager.getInstance().submitRankRow(player, RankType.ACTIVITY_MILITARY, event);
		}

		openActiveManager.checkAndSendRewardStatus(player, CompeteRankValue.MILITARY_RANK.getRankTypeValue());
	}

	@ReceiverAnno
	public void compensateRewardByEmail(LoginEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		player.getOpenActive().getEveryDayRecharge().sendNotRewardMail();
		player.getOpenActive().getEnhancePowerActive().sendNotRewardMail(player);
		if (OpenActiveConfig.getInstance().getHorseActiveDurationCond().verify(player, false)) {
			player.getOpenActive().getHorseUpgradeActive().setMaxGrade(player.getHorse().getGrade());
		}
		if (OpenActiveConfig.getInstance().getArtifactActiveDurationCond().verify(player, false)) {
			player.getOpenActive().getArtifactActive().setMaxGrade(player.getArtifact().getLevel());
		}
		if (OpenActiveConfig.getInstance().getSoulActiveDurationCond().verify(player, false)) {
			player.getOpenActive().getSoulActive().setMaxGrade(player.getSoul().getLevel());
		}
		openActiveManager.compensateRewardByEmail(player);
		openActiveManager.soulUpgradeEventHandler(player, SoulUpgradeEvent.valueOf(player), false);
	}

	@ReceiverAnno
	public void anotherday(AnotherDayEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		player.getOpenActive().getEveryDayRecharge().sendNotRewardMail();
		player.getOpenActive().getEnhancePowerActive().sendNotRewardMail(player);
		boolean verifyPublicTest = openActiveConfig.getDoubleElementCeremonyTime().verify(player, false);
		if (verifyPublicTest) {
			PacketSendUtility.sendPacket(player, SM_Public_Test_Info.valueOf(player));
		}

		PacketSendUtility.sendPacket(player, SM_SevenCompete_Count.valueOf(player));
		PacketSendUtility.sendPacket(player, SM_OldSevenCompete_Count.valueOf(player));
	}

	@ReceiverAnno
	public void flagQuestCountChange(CountryFlagQuestFinishEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		if (OpenActiveConfig.getInstance().getCountryFlagRewardConds().verify(player)) {
			player.getOpenActive().getCountryFlagActive().addCount(player);
		}
	}

	@ReceiverAnno
	public void clawBackFlagQuestCountChange(ClawbackFlagFinishEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		if (OpenActiveConfig.getInstance().getCountryFlagRewardConds().verify(player)) {
			player.getOpenActive().getCountryFlagActive().addCount(player);
		}
	}

	@HandlerAnno
	public SM_Draw_CelebrateReward drawCelebrateReward(TSession session, CM_Draw_CelebrateReward req) {
		Player player = SessionUtil.getPlayerBySession(session);
		SM_Draw_CelebrateReward sm = new SM_Draw_CelebrateReward();
		try {
			sm = openActiveManager.drawCelebrateReward(player);
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("领取经验放松奖励出现未知异常", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
		return sm;
	}

}
