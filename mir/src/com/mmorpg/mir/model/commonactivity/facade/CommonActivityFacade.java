package com.mmorpg.mir.model.commonactivity.facade;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.capturetown.TownCaptureEvent;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.commonactivity.CommonActivityConfig;
import com.mmorpg.mir.model.commonactivity.event.IdentifyTreasureRankEvent;
import com.mmorpg.mir.model.commonactivity.manager.CommonActivityManager;
import com.mmorpg.mir.model.commonactivity.model.CommonCheapGiftBag;
import com.mmorpg.mir.model.commonactivity.model.CommonConsumeActive;
import com.mmorpg.mir.model.commonactivity.model.CommonConsumeGift;
import com.mmorpg.mir.model.commonactivity.model.CommonFirstPay;
import com.mmorpg.mir.model.commonactivity.model.CommonGoldTreasury;
import com.mmorpg.mir.model.commonactivity.model.CommonIdentifyTreasure;
import com.mmorpg.mir.model.commonactivity.model.CommonLoginGift;
import com.mmorpg.mir.model.commonactivity.model.CommonMarcoShop;
import com.mmorpg.mir.model.commonactivity.model.CommonRechargeCelebrate;
import com.mmorpg.mir.model.commonactivity.model.CommonRedPack;
import com.mmorpg.mir.model.commonactivity.model.CommonTreasureActive;
import com.mmorpg.mir.model.commonactivity.model.IdentifyTreasureLog;
import com.mmorpg.mir.model.commonactivity.model.RecollectType;
import com.mmorpg.mir.model.commonactivity.model.vo.CommonTreasureActiveVo;
import com.mmorpg.mir.model.commonactivity.packet.CM_CommonMarcoShop_Buy;
import com.mmorpg.mir.model.commonactivity.packet.CM_CommonMarcoShop_Custom_Refresh;
import com.mmorpg.mir.model.commonactivity.packet.CM_CommonMarcoShop_Query;
import com.mmorpg.mir.model.commonactivity.packet.CM_CommonMarcoShop_System_Refresh;
import com.mmorpg.mir.model.commonactivity.packet.CM_Common_Celebrate_Reward_Firework;
import com.mmorpg.mir.model.commonactivity.packet.CM_Common_Cheap_Gift_Bag_Reward;
import com.mmorpg.mir.model.commonactivity.packet.CM_Common_Collect_Word_Reward;
import com.mmorpg.mir.model.commonactivity.packet.CM_Common_Consume_Draw_Reward;
import com.mmorpg.mir.model.commonactivity.packet.CM_Common_First_Pay_Reward;
import com.mmorpg.mir.model.commonactivity.packet.CM_Common_Identify_Treasure_Query;
import com.mmorpg.mir.model.commonactivity.packet.CM_Common_Identify_Treasure_Reward;
import com.mmorpg.mir.model.commonactivity.packet.CM_Common_Login_Gift_Reward;
import com.mmorpg.mir.model.commonactivity.packet.CM_Common_Recharge_Draw_Reward;
import com.mmorpg.mir.model.commonactivity.packet.CM_Common_RedPackActive_Reward;
import com.mmorpg.mir.model.commonactivity.packet.CM_Common_TreasureActive_Reward;
import com.mmorpg.mir.model.commonactivity.packet.CM_Consume_Gift_Reward;
import com.mmorpg.mir.model.commonactivity.packet.CM_Draw_Recollect_Rewards;
import com.mmorpg.mir.model.commonactivity.packet.CM_Get_Common_Consume;
import com.mmorpg.mir.model.commonactivity.packet.CM_Get_Recollect;
import com.mmorpg.mir.model.commonactivity.packet.CM_Gold_Treasury_Query;
import com.mmorpg.mir.model.commonactivity.packet.CM_Gold_Treasury_Query_Master;
import com.mmorpg.mir.model.commonactivity.packet.CM_Gold_Treasury_Reset;
import com.mmorpg.mir.model.commonactivity.packet.CM_Gold_Treasury_Reward;
import com.mmorpg.mir.model.commonactivity.packet.CM_Lucky_Draw_Draw;
import com.mmorpg.mir.model.commonactivity.packet.CM_Lucky_Draw_Reward;
import com.mmorpg.mir.model.commonactivity.packet.CM_Recollect_All;
import com.mmorpg.mir.model.commonactivity.packet.CM_WeekCri_Buy;
import com.mmorpg.mir.model.commonactivity.packet.SM_Can_Recollect_Count;
import com.mmorpg.mir.model.commonactivity.packet.SM_CommonTreasureActive_Count_Change;
import com.mmorpg.mir.model.commonactivity.packet.SM_Common_Collect_Word_Reward;
import com.mmorpg.mir.model.commonactivity.packet.SM_Common_Consume_Gift_Push;
import com.mmorpg.mir.model.commonactivity.packet.SM_Common_Consume_Push;
import com.mmorpg.mir.model.commonactivity.packet.SM_Common_First_Pay_Reward;
import com.mmorpg.mir.model.commonactivity.packet.SM_Common_Identify_Treasure_Reward;
import com.mmorpg.mir.model.commonactivity.packet.SM_Consume_Gift_Reward;
import com.mmorpg.mir.model.commonactivity.packet.SM_Get_Common_Consume;
import com.mmorpg.mir.model.commonactivity.packet.SM_Gold_Treasury_Reset;
import com.mmorpg.mir.model.commonactivity.resource.CommonCheapGiftBagResource;
import com.mmorpg.mir.model.commonactivity.resource.CommonConsumeActiveResource;
import com.mmorpg.mir.model.commonactivity.resource.CommonConsumeGiftResource;
import com.mmorpg.mir.model.commonactivity.resource.CommonFirstPayResource;
import com.mmorpg.mir.model.commonactivity.resource.CommonGoldTreasuryResource;
import com.mmorpg.mir.model.commonactivity.resource.CommonLoginGiftResource;
import com.mmorpg.mir.model.commonactivity.resource.CommonMarcoShopResource;
import com.mmorpg.mir.model.commonactivity.resource.CommonRechargeActiveResource;
import com.mmorpg.mir.model.copy.manager.CopyManager;
import com.mmorpg.mir.model.copy.model.CopyType;
import com.mmorpg.mir.model.copy.resource.CopyResource;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.item.event.TreasureEvent;
import com.mmorpg.mir.model.player.event.AnotherDayEvent;
import com.mmorpg.mir.model.player.event.LoginEvent;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.purse.event.RechargeRewardEvent;
import com.mmorpg.mir.model.purse.model.CurrencyType;
import com.mmorpg.mir.model.quest.event.QuestCompletEvent;
import com.mmorpg.mir.model.quest.manager.QuestManager;
import com.mmorpg.mir.model.quest.model.QuestType;
import com.mmorpg.mir.model.quest.resource.QuestResource;
import com.mmorpg.mir.model.rank.manager.WorldRankManager;
import com.mmorpg.mir.model.rank.model.DayKey;
import com.mmorpg.mir.model.rank.model.RankType;
import com.mmorpg.mir.model.serverstate.ServerState;
import com.mmorpg.mir.model.system.packet.SM_System_Message;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.SessionUtil;
import com.mmorpg.mir.model.warship.event.WarshipEvent;
import com.mmorpg.mir.model.welfare.event.ClawDoneTodayNumEvent;
import com.mmorpg.mir.model.welfare.event.ClawbackEvent;
import com.mmorpg.mir.model.welfare.event.CopyEvent;
import com.mmorpg.mir.model.welfare.event.CountrySacrificeEvent;
import com.mmorpg.mir.model.welfare.event.CurrencyActionEvent;
import com.windforce.common.event.anno.ReceiverAnno;
import com.windforce.common.utility.DateUtils;
import com.xiaosan.dispatcher.anno.HandlerAnno;
import com.xiaosan.socket.core.TSession;

@Component
public class CommonActivityFacade {
	private static final Logger logger = Logger.getLogger(CommonActivityFacade.class);

	@Autowired
	private CommonActivityManager activityManager;

	@Autowired
	private CommonActivityConfig config;

	@HandlerAnno
	public void drawCommonLoginGiftReward(TSession session, CM_Common_Login_Gift_Reward req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			activityManager.drawCommonLoginGift(player, req.getId());
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("领取公共登陆有礼出错", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void drawCommonCheapGiftBagReward(TSession session, CM_Common_Cheap_Gift_Bag_Reward req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			activityManager.drawCommonCheapGiftBag(player, req.getId());
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("领取公共特惠礼包出错", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void getInfoConsume(TSession session, CM_Get_Common_Consume req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			PacketSendUtility.sendPacket(player, SM_Get_Common_Consume.valueOf(player, req.getActivityName()));
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("获取消费活动信息出错", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void drawCommonConsumeRewards(TSession session, CM_Common_Consume_Draw_Reward req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			activityManager.drawConsumeReward(player, req.getId());
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("领取消费活动奖励出错", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void drawcommonRechargeReward(TSession session, CM_Common_Recharge_Draw_Reward req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			activityManager.drawCommonRechargeReward(player, req.getId());
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("领取充值献礼出错", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void rewardCelebrateFirework(TSession session, CM_Common_Celebrate_Reward_Firework req) {
		Player player = SessionUtil.getPlayerBySession(session);
		if (req.getCount() < 1 && req.getCount() > 9999) {
			return;
		}
		try {
			activityManager.rewardCelebrateFirework(player, req.getId(), req.getCount());
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("领取烟火奖励出错", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void drawCommonIdentifyTreasure(TSession session, CM_Common_Identify_Treasure_Reward req) {
		Player player = SessionUtil.getPlayerBySession(session);
		SM_Common_Identify_Treasure_Reward sm = SM_Common_Identify_Treasure_Reward.valueOf(req.getActiveName());
		try {
			activityManager.drawCommonIdentifyTreasureReward(player, sm);
		} catch (ManagedException e) {
			sm.setErrorCode(e.getCode());
		} catch (Exception e) {
			logger.error("领取鉴宝奖励出错", e);
			sm.setErrorCode(ManagedErrorCode.SYS_ERROR);
		}
		PacketSendUtility.sendPacket(player, sm);
	}

	@HandlerAnno
	public void queryCommonIdentifyTreasure(TSession session, CM_Common_Identify_Treasure_Query req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			activityManager.queryCommonIdentifyTreasure(player, req.getActiveName());
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("鉴宝查询出错", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void luckyDraw(TSession session, CM_Lucky_Draw_Draw req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			activityManager.luckyDraw(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("幸运抽奖出错", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void luckyDrawReward(TSession session, CM_Lucky_Draw_Reward req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			activityManager.luckyDrawReward(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("幸运抽奖发奖出错", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void drawCommonFirstPayReward(TSession session, CM_Common_First_Pay_Reward req) {
		Player player = SessionUtil.getPlayerBySession(session);
		SM_Common_First_Pay_Reward sm = SM_Common_First_Pay_Reward.valueOf(req.getId());
		try {
			activityManager.drawCommonFirstPayReward(player, req.getId());
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("首充出错", e);
			sm.setCode(ManagedErrorCode.SYS_ERROR);
		}
		PacketSendUtility.sendPacket(player, sm);
	}

	@HandlerAnno
	public void drawCommonCollectWordActiveReward(TSession session, CM_Common_Collect_Word_Reward req) {
		Player player = SessionUtil.getPlayerBySession(session);
		SM_Common_Collect_Word_Reward sm = SM_Common_Collect_Word_Reward.valueOf(req);
		try {
			activityManager.drawCommonCollectWordActiveReward(player, req.getId(), req.getCount());
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("集字活动出错", e);
			sm.setCode(ManagedErrorCode.SYS_ERROR);
		}
		PacketSendUtility.sendPacket(player, sm);
	}

	@HandlerAnno
	public void buyCriItem(TSession session, CM_WeekCri_Buy req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			activityManager.buyCriItem(player, req.getCount(), req.getSign());
		} catch (ManagedException e) {
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("暴击活动未知异常", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void query(TSession session, CM_CommonMarcoShop_Query req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			activityManager.queryMarcoShop(player, req.getActivityName());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("神秘商店查询未知异常 ！", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void buy(TSession session, CM_CommonMarcoShop_Buy req) {
		Player player = SessionUtil.getPlayerBySession(session);
		if (req.getGridIndex() < 0
				|| req.getGridIndex() >= CommonActivityConfig.getInstance().commonShopStorage.getUnique(
						CommonMarcoShopResource.ACTIVITY_NAME, req.getActivityName()).getGridCount()) {
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.ERROR_MSG);
			return;
		}
		try {
			activityManager.buy(player, req.getActivityName(), req.getGridIndex());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("神秘商店购买未知异常 ！", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void customRefresh(TSession session, CM_CommonMarcoShop_Custom_Refresh req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			activityManager.customRefresh(player, req.getActivityName());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("神秘商店刷新出现未知异常 ！", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void systemRefresh(TSession session, CM_CommonMarcoShop_System_Refresh req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			activityManager.systemRefresh(player, req.getActivityName());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("神秘商店系统刷新出现未知异常 ！", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void rewardRedPacketActive(TSession session, CM_Common_RedPackActive_Reward req) {

		Player player = SessionUtil.getPlayerBySession(session);
		try {
			activityManager.rewardRedPacket(player, req.getId());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("红包抢不停出现未知异常 ！", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void rewardTreasureActive(TSession session, CM_Common_TreasureActive_Reward req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			activityManager.rewardTreasureActivity(player, req.getId());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("全民探宝出现未知异常 ！", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void goldTreasuryReward(TSession session, CM_Gold_Treasury_Reward req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			activityManager.goldTreasuryReward(player, req);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("黄金宝库抽奖出现未知异常 ！", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void goldTreasuryReset(TSession session, CM_Gold_Treasury_Reset req) {
		Player player = SessionUtil.getPlayerBySession(session);
		SM_Gold_Treasury_Reset sm = SM_Gold_Treasury_Reset.valueOf(req);
		try {
			activityManager.goldTreasuryReset(player, req);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("黄金宝库重置未知异常 ！", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
		PacketSendUtility.sendPacket(player, sm);
	}

	@HandlerAnno
	public void goldTreasuryQuery(TSession session, CM_Gold_Treasury_Query req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			activityManager.goldTreasuryQuery(player, req.getActiveName());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("黄金宝库查询出现未知异常 ！", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void goldTreasuryQueryMaster(TSession session, CM_Gold_Treasury_Query_Master req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			activityManager.goldTreasuryQueryMaster(player, req.getActiveName());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("黄金宝库查询最大奖未知异常 ！", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void consumeGiftReward(TSession session, CM_Consume_Gift_Reward req) {
		Player player = SessionUtil.getPlayerBySession(session);
		SM_Consume_Gift_Reward sm = SM_Consume_Gift_Reward.ValueOf(req);
		try {
			activityManager.rewardConsumeGift(player, req);
			PacketSendUtility.sendPacket(player, sm);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("消费献礼未知异常 ！", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@ReceiverAnno
	public void loginEvent(LoginEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		for (CommonConsumeActiveResource resource : config.consumeStorages.getAll()) {
			Map<String, CommonConsumeActive> consumeActives = player.getCommonActivityPool().getConsumeActives();
			if (!consumeActives.containsKey(resource.getActivityName())) {
				consumeActives.put(resource.getActivityName(), CommonConsumeActive.valueOf(resource.getActivityName()));
			}
		}

		for (CommonRechargeActiveResource resource : config.rechargeStorages.getAll()) {
			Map<String, CommonRechargeCelebrate> rechargeActives = player.getCommonActivityPool().getRechargeActives();
			if (!rechargeActives.containsKey(resource.getActiveName())) {
				rechargeActives
						.put(resource.getActiveName(), CommonRechargeCelebrate.valueOf(resource.getActiveName()));
			}
		}
		for (CommonLoginGiftResource resource : config.loginGiftStorage.getAll()) {
			if (!player.getCommonActivityPool().getCommonLoginActives().containsKey(resource.getActiveName())) {
				player.getCommonActivityPool().getCommonLoginActives()
						.put(resource.getActiveName(), CommonLoginGift.valueOf());
			}
		}

		for (CommonCheapGiftBagResource resource : config.cheapGiftBagStorage.getAll()) {
			if (!player.getCommonActivityPool().getCommonCheapActives().containsKey(resource.getActiveName())) {
				player.getCommonActivityPool().getCommonCheapActives()
						.put(resource.getActiveName(), CommonCheapGiftBag.valueOf());
			}
		}

		for (CommonMarcoShopResource resource : config.commonShopStorage.getAll()) {
			if (!player.getCommonActivityPool().getCommonMarcoShop().containsKey(resource.getActivityName())) {
				player.getCommonActivityPool().getCommonMarcoShop()
						.put(resource.getActivityName(), CommonMarcoShop.valueOf(resource.getActivityName()));
			}
		}

		for (CommonFirstPayResource resource : config.firstPayStorage.getAll()) {
			if (!player.getCommonActivityPool().getFirstPays().containsKey(resource.getActiveName())) {
				player.getCommonActivityPool().getFirstPays().put(resource.getActiveName(), CommonFirstPay.valueOf());
			}
		}

		for (String activeName : config.TREASUREACTVIE_TIME_CONDS.getValue().keySet()) {
			if (!player.getCommonActivityPool().getTreasurueActives().containsKey(activeName)) {
				player.getCommonActivityPool().getTreasurueActives()
						.put(activeName, CommonTreasureActive.valueOf(activeName));
			}
		}

		for (String activeName : config.COMMON_REDPACKET_TIME_CONDS.getValue().keySet()) {
			if (!player.getCommonActivityPool().getRedPacketActives().containsKey(activeName)) {
				player.getCommonActivityPool().getRedPacketActives().put(activeName, CommonRedPack.valueOf(activeName));
			}
		}

		for (CommonGoldTreasuryResource resource : config.goldTreasuryStorage.getAll()) {
			if (!player.getCommonActivityPool().getGoldTreasurys().containsKey(resource.getActiveName())) {
				player.getCommonActivityPool().getGoldTreasurys()
						.put(resource.getActiveName(), CommonGoldTreasury.valueOf());
			}
		}

		for (CommonConsumeGiftResource resource : config.consumeGiftStroage.getAll()) {
			if (!player.getCommonActivityPool().getConsumeGifts().containsKey(resource.getActiveName())) {
				player.getCommonActivityPool().getConsumeGifts()
						.put(resource.getActiveName(), CommonConsumeGift.ValueOf());
			}
		}

		activityManager.rewardCommonRecharge(player);
		activityManager.loginCompensateMail(player);
		activityManager.addFirstPayEndMail(player);
		activityManager.compensateMailReward(player);
		if (!DateUtils.isSameDay(new Date(), new Date(player.commonActivityPool.getLuckyDraw().getLastClearTime()))) {
			player.commonActivityPool.getLuckyDraw().dayReset();
		}
		activityManager.addConsumeGiftEndMail(player);
	}

	@ReceiverAnno
	public void rechargeEvent(RechargeRewardEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());

		Map<String, CommonRechargeCelebrate> rechargeActives = player.getCommonActivityPool().getRechargeActives();
		for (CommonRechargeCelebrate recharge : rechargeActives.values()) {
			CommonRechargeActiveResource resource = config.rechargeStorages.getUnique(
					CommonRechargeActiveResource.ACTIVE_NAME_INDEX, recharge.getAcitityName());
			CoreConditions timeConds = CoreConditionManager.getInstance().getCoreConditions(1,
					resource.getActivityTimeConds());
			if (!timeConds.verify(player, false)) {
				continue;
			}
			recharge.addRechargeAmount(player, event.getAmount());
		}

		Map<String, CommonFirstPay> firstPays = player.getCommonActivityPool().getFirstPays();
		for (String activeName : firstPays.keySet()) {
			boolean condition = config.firstPayStorage.getIndex(CommonFirstPayResource.ACTIVE_NAME, activeName).get(0)
					.getOpenCoreConditions().verify(player);
			if (condition) {
				firstPays.get(activeName).addPayCount(event.getAmount());
				// 添加结束事件
				// if(!firstPays.get(activeName).isHasScheduled()){
				// activityManager.addFirstPayEndEvent(player, activeName);
				// firstPays.get(activeName).setHasScheduled(true);
				// }
				// 添加充值推送
				activityManager.addFirstPaySend(player, activeName);
			}
		}

		PlayerManager.getInstance().updatePlayer(player);
	}

	@ReceiverAnno
	public void consumeGoldActiveEvent(CurrencyActionEvent event) {
		if (event.getType() == CurrencyType.GOLD) {
			Player player = PlayerManager.getInstance().getPlayer(event.getOwner());

			Map<String, CommonConsumeActive> consumeActives = player.getCommonActivityPool().getConsumeActives();
			for (CommonConsumeActive consumeActive : consumeActives.values()) {
				CoreConditions timeCondition = config.getConsumeTimeConds(consumeActive.getActivityName());
				if (timeCondition != null && timeCondition.verify(player)) {
					consumeActive.addConsumeGold(event.getValue());
				}

				CommonConsumeActiveResource resource = config.consumeStorages.getIndex(
						CommonConsumeActiveResource.ACTVITY_NAME, consumeActive.getActivityName()).get(0);
				if (resource.getEnterRankConditions().verify(player, false)) {
					event.setActivityValue(consumeActive.getConsumeGold());
					WorldRankManager.getInstance().submitRankRow(player, RankType.valueOf(resource.getRankType()),
							event);
				}

				ArrayList<String> canRecieves = activityManager.getConsumeCanRecievesReward(player,
						consumeActive.getActivityName());
				PacketSendUtility.sendPacket(player,
						SM_Common_Consume_Push.valueOf(consumeActive.getActivityName(), canRecieves));
			}
			player.getCommonActivityPool().getLuckyDraw().addPayCount(event.getValue());
			activityManager.addLuckyDrawSend(player);
			Map<String, CommonConsumeGift> consumeGifts = player.getCommonActivityPool().getConsumeGifts();
			for (String activeName : consumeGifts.keySet()) {
				CommonConsumeGiftResource resource = config.consumeGiftStroage.getIndex(
						CommonConsumeGiftResource.ACTIVITY_NAME, activeName).get(0);
				CommonConsumeGift gift = consumeGifts.get(activeName);
				if (resource.getOpenCoreConditions().verify(player)) {
					gift.addConsumeCount(event.getValue());
				}
				PacketSendUtility.sendPacket(player,
						SM_Common_Consume_Gift_Push.valueOf(activeName, gift.getConsumeCount()));
			}
		}
	}

	@ReceiverAnno
	public void anotherday(AnotherDayEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		for (CommonConsumeActive consumeActive : player.getCommonActivityPool().getConsumeActives().values()) {
			ArrayList<String> canRecieves = activityManager.getConsumeCanRecievesReward(player,
					consumeActive.getActivityName());
			if (!canRecieves.isEmpty()) {
				PacketSendUtility.sendPacket(player,
						SM_Common_Consume_Push.valueOf(consumeActive.getActivityName(), canRecieves));
			}
		}

		// activityManager.loginCompensateMail(player);
		activityManager.rewardCommonRecharge(player);
		Map<String, CommonIdentifyTreasure> identifyTreasures = player.getCommonActivityPool().getIdentifyTreasures();
		for (CommonIdentifyTreasure treasure : identifyTreasures.values()) {
			treasure.clearLuckValue();
		}
		activityManager.addFirstPayEndMail(player);
		activityManager.compensateMailReward(player);

		if (CommonActivityConfig.getInstance().getRecollectRecievedConditions().verify(player, false)) {
			PacketSendUtility.sendPacket(player, SM_Can_Recollect_Count.valueOf(player));
		}
		player.getCommonActivityPool().getLuckyDraw().dayReset();
		activityManager.addConsumeGiftEndMail(player);
	}

	@ReceiverAnno
	public void identifyTreasureRank(IdentifyTreasureRankEvent event) {
		ServerState
				.getInstance()
				.getCommonIdentifyTreasureTotalServers()
				.getTreasureServerByActiveName(event.getActiveName())
				.rankIdentifyTreasure(
						IdentifyTreasureLog.valueOf(event.getPlayerName(), event.getRewardId(), event.isBigTreasure()));
	}

	@ReceiverAnno
	public void treasure(TreasureEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		Map<String, CommonTreasureActive> actives = player.getCommonActivityPool().getTreasurueActives();
		HashMap<String, CommonTreasureActiveVo> treasureActiveVos = new HashMap<String, CommonTreasureActiveVo>();
		for (CommonTreasureActive active : actives.values()) {
			CoreConditions conds = config.getTreasureActiveTimeConds(active.getActivityName());
			if (conds != null && conds.verify(player)) {
				active.addCount(event.getCount());
				treasureActiveVos.put(active.getActivityName(), CommonTreasureActiveVo.valueOf(active));
			}
		}
		if (!treasureActiveVos.isEmpty()) {
			PacketSendUtility.sendPacket(player, SM_CommonTreasureActive_Count_Change.valueOf(treasureActiveVos));
		}
	}

	@HandlerAnno
	public void getRecollectStatus(TSession session, CM_Get_Recollect req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			activityManager.getRecollectStatus(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("查询追回活动的信息", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void recieveRecollectRewards(TSession session, CM_Draw_Recollect_Rewards req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			activityManager.recieveRecollectRewards(player, req.getIds(), req.isUseGold());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("领取追回活动的奖励", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void recieveRecollectRewardsOneKey(TSession session, CM_Recollect_All req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			activityManager.recieveRecollectRewardsOneKey(player, req.isUseGold());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			logger.error("领取追回活动的奖励", e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@ReceiverAnno
	public void clawBackHandle(ClawbackEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		if (event.getType().getRecollectType() != null
				&& CommonActivityConfig.getInstance().getRecollectRecievedConditions().verify(player, false)) {
			long dayKey = DayKey.valueOf(System.currentTimeMillis() - DateUtils.MILLIS_PER_DAY).getLunchTime();
			player.getCommonActivityPool().getCurrentRecollectActive()
					.logClawbackStatus(player, dayKey, event.getType().getRecollectType(), event.getCount());
		}
	}

	@ReceiverAnno
	public void expCopy(CopyEvent event) {
		if (event.getCopyId().equals("Exp_copy0")) { // 基佬炫的需求
			return;
		}
		CopyResource copyRes = CopyManager.getInstance().getCopyResources().get(event.getCopyId(), false);
		if (copyRes == null || copyRes.getType() != CopyType.EXP) {
			return;
		}
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		if (CommonActivityConfig.getInstance().getRecollectLogDataConditions(RecollectType.EXP_COPY)
				.verify(player, false)) {
			Long dayKey = DayKey.valueOf().getLunchTime();
			player.getCommonActivityPool().getCurrentRecollectActive()
					.logDoneStatus(player, dayKey, RecollectType.EXP_COPY, 1);
		}
	}

	@ReceiverAnno
	public void dailyMission(QuestCompletEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		QuestResource questResource = QuestManager.staticQuestResources.get(event.getQuestId(), true);
		if (questResource.getType() == QuestType.DAY
				&& CommonActivityConfig.getInstance().getRecollectLogDataConditions(RecollectType.DAILY_MISSION)
						.verify(player, false)) {
			Long dayKey = DayKey.valueOf().getLunchTime();
			player.getCommonActivityPool().getCurrentRecollectActive()
					.logDoneStatus(player, dayKey, RecollectType.DAILY_MISSION, 1);
		}
	}

	@ReceiverAnno
	public void warship(WarshipEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		if (CommonActivityConfig.getInstance().getRecollectLogDataConditions(RecollectType.WARSHIP)
				.verify(player, false)) {
			Long dayKey = DayKey.valueOf().getLunchTime();
			player.getCommonActivityPool().getCurrentRecollectActive()
					.logDoneStatus(player, dayKey, RecollectType.WARSHIP, 1);
		}
	}

	@ReceiverAnno
	public void countrySacrifice(CountrySacrificeEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		if (CommonActivityConfig.getInstance().getRecollectLogDataConditions(RecollectType.COUNTRY_SACRIFICE)
				.verify(player, false)) {
			Long dayKey = DayKey.valueOf().getLunchTime();
			player.getCommonActivityPool().getCurrentRecollectActive()
					.logDoneStatus(player, dayKey, RecollectType.COUNTRY_SACRIFICE, 1);
		}
	}

	@ReceiverAnno
	public void townCapture(TownCaptureEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		if (CommonActivityConfig.getInstance().getRecollectLogDataConditions(RecollectType.TOWN_CAPTURE)
				.verify(player, false)) {
			Long dayKey = DayKey.valueOf().getLunchTime();
			player.getCommonActivityPool().getCurrentRecollectActive()
					.logDoneStatus(player, dayKey, RecollectType.TOWN_CAPTURE, 1);
		}
	}

	@ReceiverAnno
	public void countryActivityLogEvent(ClawDoneTodayNumEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		if (CommonActivityConfig.getInstance().getRecollectLogDataConditions(event.getType()).verify(player, false)) {
			Long dayKey = DayKey.valueOf().getLunchTime();
			player.getCommonActivityPool().getCurrentRecollectActive()
					.logDoneStatus(player, dayKey, event.getType(), 1);
		}
	}

}
