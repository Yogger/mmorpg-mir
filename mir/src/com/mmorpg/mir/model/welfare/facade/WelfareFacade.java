package com.mmorpg.mir.model.welfare.facade;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.copy.event.LadderResetEvent;
import com.mmorpg.mir.model.country.event.CountryFlagQuestFinishEvent;
import com.mmorpg.mir.model.country.event.PlayerKillDiplomacyEvent;
import com.mmorpg.mir.model.countrycopy.event.CountryCopyFinishEvent;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.event.MonsterKillEvent;
import com.mmorpg.mir.model.moduleopen.event.ModuleOpenEvent;
import com.mmorpg.mir.model.player.event.AnotherDayEvent;
import com.mmorpg.mir.model.player.event.KillPlayerEvent;
import com.mmorpg.mir.model.player.event.LoginEvent;
import com.mmorpg.mir.model.player.event.LogoutEvent;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.purse.event.CurrencyRewardEvent;
import com.mmorpg.mir.model.purse.model.CurrencyType;
import com.mmorpg.mir.model.quest.event.QuestCompletEvent;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.SessionUtil;
import com.mmorpg.mir.model.vip.event.FirstPayEvent;
import com.mmorpg.mir.model.warship.event.WarshipEvent;
import com.mmorpg.mir.model.welfare.event.BossDieEvent;
import com.mmorpg.mir.model.welfare.event.CopyEvent;
import com.mmorpg.mir.model.welfare.event.CountrySacrificeEvent;
import com.mmorpg.mir.model.welfare.event.CurrencyActionEvent;
import com.mmorpg.mir.model.welfare.event.ExpressEvent;
import com.mmorpg.mir.model.welfare.event.InvestigateEvent;
import com.mmorpg.mir.model.welfare.event.RescueEvent;
import com.mmorpg.mir.model.welfare.event.TempleEvent;
import com.mmorpg.mir.model.welfare.manager.ActiveManager;
import com.mmorpg.mir.model.welfare.manager.EventManager;
import com.mmorpg.mir.model.welfare.manager.GiftCollectManage;
import com.mmorpg.mir.model.welfare.manager.PublicWelfareManager;
import com.mmorpg.mir.model.welfare.model.ActionCurrencyType;
import com.mmorpg.mir.model.welfare.packet.CM_Get_Red_Gift;
import com.mmorpg.mir.model.welfare.packet.CM_One_Off_Gift;
import com.mmorpg.mir.model.welfare.packet.CM_Query_Red_Gift;
import com.mmorpg.mir.model.welfare.packet.CM_Welfare_Active_Open_Panel;
import com.mmorpg.mir.model.welfare.packet.CM_Welfare_Active_Reward;
import com.mmorpg.mir.model.welfare.packet.CM_Welfare_Clawback_Open;
import com.mmorpg.mir.model.welfare.packet.CM_Welfare_Clawback_Reward;
import com.mmorpg.mir.model.welfare.packet.CM_Welfare_Clawback_Reward_Auto;
import com.mmorpg.mir.model.welfare.packet.CM_Welfare_Clawback_Reward_Superme;
import com.mmorpg.mir.model.welfare.packet.CM_Welfare_Draw_FirstPay_Reward;
import com.mmorpg.mir.model.welfare.packet.CM_Welfare_Draw_Sevenday_Reward;
import com.mmorpg.mir.model.welfare.packet.CM_Welfare_Offline_OpenPanel;
import com.mmorpg.mir.model.welfare.packet.CM_Welfare_Offline_Reward;
import com.mmorpg.mir.model.welfare.packet.CM_Welfare_Online_Open;
import com.mmorpg.mir.model.welfare.packet.CM_Welfare_Online_Reward;
import com.mmorpg.mir.model.welfare.packet.CM_Welfare_Sign_Fill_Sign;
import com.mmorpg.mir.model.welfare.packet.CM_Welfare_Sign_Ing;
import com.mmorpg.mir.model.welfare.packet.CM_Welfare_Sign_Open_Panel;
import com.mmorpg.mir.model.welfare.packet.CM_Welfare_Sign_Reward;
import com.mmorpg.mir.model.welfare.packet.CM_Welfare_Tag;
import com.mmorpg.mir.model.welfare.packet.SM_Active_Num;
import com.mmorpg.mir.model.welfare.packet.SM_RedGift_Deprecate;
import com.mmorpg.mir.model.welfare.packet.SM_Welfare_Push_Light_Reward;
import com.mmorpg.mir.model.welfare.service.ActiveService;
import com.mmorpg.mir.model.welfare.service.ClawbackService;
import com.mmorpg.mir.model.welfare.service.GiftRewardService;
import com.mmorpg.mir.model.welfare.service.OfflineExpService;
import com.mmorpg.mir.model.welfare.service.OnlineService;
import com.mmorpg.mir.model.welfare.service.SignService;
import com.windforce.common.event.anno.ReceiverAnno;
import com.windforce.common.utility.DateUtils;
import com.xiaosan.dispatcher.anno.HandlerAnno;
import com.xiaosan.socket.core.TSession;

@Component
public class WelfareFacade {

	private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(WelfareFacade.class);

	@Autowired
	private SignService signService;
	@Autowired
	private OnlineService onlineService;
	@Autowired
	private OfflineExpService offlineExpService;
	@Autowired
	private ActiveService activeService;
	@Autowired
	private ClawbackService clawbackService;
	@Autowired
	private EventManager eventManager;
	@Autowired
	private GiftRewardService giftService;

	@HandlerAnno
	public void openSign(TSession session, CM_Welfare_Sign_Open_Panel req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			signService.openSign(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			LOGGER.error("打开签到 : " + e.getMessage(), e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void signing(TSession session, CM_Welfare_Sign_Ing req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			signService.signing(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			LOGGER.error("签到 : " + e.getMessage(), e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void fillSign(TSession session, CM_Welfare_Sign_Fill_Sign req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			signService.fillSign(player, req.getReqTimeSeconds());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			LOGGER.error("补签 : " + e.getMessage() + " , " + req.getReqTimeSeconds(), e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void signReward(TSession session, CM_Welfare_Sign_Reward req) {
		Player player = SessionUtil.getPlayerBySession(session);
		int days = req.getDays();
		try {
			signService.signReward(player, days);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			LOGGER.error("领取签到奖励 : " + e.getMessage() + " ," + days, e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void openOnline(TSession session, CM_Welfare_Online_Open req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			onlineService.openOnline(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			LOGGER.error("打开在线奖励 : " + e.getMessage(), e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void onlineReward(TSession session, CM_Welfare_Online_Reward req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			onlineService.onlineReward(player, req.getIndex());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			LOGGER.error("领取在线奖励 : " + e.getMessage(), e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void openOfflineExp(TSession session, CM_Welfare_Offline_OpenPanel req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			offlineExpService.openOfflineExp(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			LOGGER.error("打开离线经验:" + e.getMessage(), e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void offlineExpReward(TSession session, CM_Welfare_Offline_Reward req) {
		Player player = SessionUtil.getPlayerBySession(session);

		if (req.getType() < 1 || req.getType() > 3) {
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
			return;
		}

		try {
			offlineExpService.offlineExpReward(player, req.getType());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			LOGGER.error("领取离线经验奖励:" + e.getMessage(), e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void openActiveValue(TSession session, CM_Welfare_Active_Open_Panel req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			activeService.openActiveValue(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			LOGGER.error("打开活跃值 : " + e.getMessage(), e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void activeValueReward(TSession session, CM_Welfare_Active_Reward req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			activeService.activeValueReward(player, req.getActiveValue());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			LOGGER.error("领取活跃值奖励 : " + e.getMessage(), e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void openClawback(TSession session, CM_Welfare_Clawback_Open req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			clawbackService.openClawback(player);
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			LOGGER.error("打开收益追回 : " + e.getMessage(), e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void clawback(TSession session, CM_Welfare_Clawback_Reward req) {
		Player player = SessionUtil.getPlayerBySession(session);
		if (req.getCurrecyType() != ActionCurrencyType.ACTION_COPPER.getType()
				&& req.getCurrecyType() != ActionCurrencyType.ACTION_GOLD.getType()) {
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
			return;
		}
		try {
			clawbackService.clawback(player, req.getEventId(), req.getCurrecyType());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			LOGGER.error("追回收益 : " + e.getMessage(), e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void autoClawback(TSession session, CM_Welfare_Clawback_Reward_Auto req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			clawbackService.autoClawback(player, req.getType());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			LOGGER.error("一键追回 : " + e.getMessage(), e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void supermeClawback(TSession session, CM_Welfare_Clawback_Reward_Superme req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			clawbackService.supermeClawback(player, req.getType());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			LOGGER.error("至尊追回 : " + e.getMessage(), e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void getTagLight(TSession session, CM_Welfare_Tag req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			clawbackService.getTagLight(player, req.getCode());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			LOGGER.error("至尊追回 : " + e.getMessage(), e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void getOneOffGift(TSession session, CM_One_Off_Gift req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			giftService.getOneOffGift(player, req.getGiftType());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			LOGGER.error("领取礼物 : " + e.getMessage(), e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void recieveRedGift(TSession session, CM_Get_Red_Gift req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			giftService.recieveRedGift(player, req.getDeadTime(), req.getSpawnKey());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			LOGGER.error("领取礼物 : " + e.getMessage(), e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void queryRedGift(TSession session, CM_Query_Red_Gift req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			giftService.queryRedGift(player, req.getDeadTime(), req.getSpawnKey());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			LOGGER.error("领取礼物 : " + e.getMessage(), e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	@HandlerAnno
	public void drawSevenDayOnlineReward(TSession session, CM_Welfare_Draw_Sevenday_Reward req) {
		Player player = SessionUtil.getPlayerBySession(session);
		if (req.getDayIndex() > 7 || req.getDayIndex() < 1) {
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
			return;
		}
		try {
			player.getWelfare().drawSevenDayOnlineReward(player, req.getDayIndex());
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			LOGGER.error("领取7天登录奖励 : " + e.getMessage(), e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}

	}

	@HandlerAnno
	public void drawFirstPayReward(TSession session, CM_Welfare_Draw_FirstPay_Reward req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			if (player != null) {
				player.getWelfare().drawFirstPayReward(player);
			}
		} catch (ManagedException e) {
			PacketSendUtility.sendErrorMessage(player, e.getCode());
		} catch (Exception e) {
			LOGGER.error("领取首充奖励 : " + e.getMessage(), e);
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SYS_ERROR);
		}
	}

	/** 上线 */
	@ReceiverAnno
	public void loginEvent(LoginEvent event) {
		eventManager.loginEvent(event);
	}

	@ReceiverAnno
	public void anotherDayEvent(AnotherDayEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		// 增加7天登录记录
		player.getWelfare().getSevenDayReward().addDayIndex();
		ActiveManager.getInstance().sendActiveNotRewardMail(player);
		ActiveManager.getInstance().check(player);
		clawbackService.refreshClawbackData(player);
		// if (player.getVip().isVip()) {
		// int status =
		// player.getWelfare().getActiveValue().getExeStatus(ActiveEnum.ACTIVE_VIP);
		// if (status == ActiveStatusEnum.STATUS_COMPLETED.getStatus()) {
		// return;
		// }
		// ActiveManager.getInstance().exec(player, ActiveEnum.ACTIVE_VIP);
		// }
		/** 微端登录 */
		// if (player.getPlayerEnt().getLoginType() ==
		// LoginType.MINICLIENT.getValue()) {
		// // 活跃度
		// ActiveManager.getInstance().exec(player,
		// ActiveEnum.ACTIVE_WEI_LOGIN);
		// }
		PacketSendUtility.sendPacket(player,
				SM_Welfare_Push_Light_Reward.valueOf(PublicWelfareManager.getInstance().countLightNum(player)));
		PacketSendUtility.sendPacket(player,
				SM_Active_Num.valueOf(PublicWelfareManager.getInstance().countActiveCount(player)));
		PacketSendUtility.sendPacket(player, SM_RedGift_Deprecate.valueOf(player.getWelfare().getSortedGiftList()));
		PacketSendUtility.sendPacket(player, GiftCollectManage.getInstance().getGiftCollect_Open(player));

		if (!DateUtils.isToday(new Date(player.getWelfare().getLastAccLoginNumberTime()))) {
			player.getWelfare().setAccLoginDays(player.getWelfare().getAccLoginDays() + 1);
			player.getWelfare().setLastAccLoginNumberTime(System.currentTimeMillis());
		}
	}

	/** 下线 */
	// @ReceiverAnno
	@Deprecated
	// 本来是用来记录在线时间
	public void loginoutEvent(LogoutEvent event) {
		eventManager.loginoutEvent(event);
	}

	/** 完成每日任务 */
	@ReceiverAnno
	public void questCompletEvent(QuestCompletEvent event) {
		eventManager.questCompletEvent(event);
	}

	/** boss死亡 */
	@ReceiverAnno
	public void bossDieEvent(BossDieEvent event) {
		eventManager.bossDieEvent(event);
	}

	/** 消耗礼金 / 元宝 */
	@ReceiverAnno
	public void currencyActionEvent(CurrencyActionEvent event) {
		eventManager.currencyActionEvent(event);
	}

	/** 副本 */
	@ReceiverAnno
	public void copyEvent(CopyEvent event) {
		if (event.getCopyId().equals("Exp_copy0")) { // 基佬炫的需求
			return;
		}
		eventManager.copyEvent(event);
	}

	@ReceiverAnno
	public void copyRestEvent(LadderResetEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		eventManager.restCopy(player);
	}

	/** 国家购买 */
	// @ReceiverAnno
	// public void countryBuyEvent(CountryBuyEvent event) {
	// eventManager.countryBuyEvent(event);
	// }

	/** 装备强化 */
	// @ReceiverAnno
	// public void enhanceEquipmentEvent(EnhanceEquipmentEvent event) {
	// eventManager.enhanceEquipmentEvent(event);
	// }

	/** 押镖 */
	@ReceiverAnno
	public void expressEvent(ExpressEvent event) {
		eventManager.expressEvent(event);
	}

	/** 刺探 */
	@ReceiverAnno
	public void investigateEvent(InvestigateEvent event) {
		eventManager.investigateEvent(event);
	}

	/** 营救 */
	@ReceiverAnno
	public void rescueEvent(RescueEvent event) {
		eventManager.rescueEvent(event);
	}

	/** 签到 */
	// @ReceiverAnno
	// public void signEvent(SignEvent event) {
	// eventManager.signEvent(event);
	// }

	/** 祭剑 */
	// @ReceiverAnno
	// public void smeltEquipmentEvent(SmeltEquipmentEvent event) {
	// eventManager.smeltEquipmentEvent(event);
	// }

	/** 太庙 */
	@ReceiverAnno
	public void templeEvent(TempleEvent event) {
		eventManager.templeEvent(event);
	}

	@ReceiverAnno
	public void countrySacrifice(CountrySacrificeEvent event) {
		eventManager.countrySacrificeEvent(event);
	}

	// @ReceiverAnno
	// public void vipEvent(VipEvent event) {
	// eventManager.vipEvent(event);
	// }

	@ReceiverAnno
	public void moduleOpenActive(ModuleOpenEvent event) {
		eventManager.moduleOpenEvent(event);
	}

	// @ReceiverAnno
	// public void upgradeCombatSpirit(CombatSpiritUpEvent event) {
	// eventManager.upgradeCombatSpiritEvent(event);
	// }

	@ReceiverAnno
	public void firsttPay(FirstPayEvent event) {
		eventManager.firstPayEvent(event);
	}

	@ReceiverAnno
	public void warshipKing(WarshipEvent event) {
		eventManager.warshipKing(event);
	}

	// @ReceiverAnno
	// public void exercise(ExerciseStartEvent event) {
	// eventManager.exerciseEvent(event);
	// }

	// @ReceiverAnno
	// public void monsterKill(MonsterKillEvent event) {
	// eventManager.monsterHunt(event);
	// }

	// @ReceiverAnno
	// public void giftChange(CurrencyRewardEvent event) {
	// eventManager.giftChange(event);
	// }

	@ReceiverAnno
	public void killDisplomacy(PlayerKillDiplomacyEvent event) {
		eventManager.killDisplomacy(event);
	}

	// @ReceiverAnno
	// public void killCountryFlag(PlayerKillCountryFlagEvent event) {
	// eventManager.killCountryFlag(event);
	// }

	@ReceiverAnno
	public void killPlayer(KillPlayerEvent event) {
		if (!event.isKillOtherCountryPlayer()) { // 排除内奸
			return;
		}
		eventManager.killPlayer(event);
	}

	@ReceiverAnno
	public void dayRecharge(CurrencyRewardEvent event) {
		if (event.getType() != CurrencyType.GOLD && event.getType() != CurrencyType.INTER) {
			return;
		}
		eventManager.dayRecharge(event);
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		PlayerManager.getInstance().updatePlayer(player);
	}

	@ReceiverAnno
	public void countryFlagQuestFinish(CountryFlagQuestFinishEvent event) {
		eventManager.countryFlagQuestFinish(event);
	}

	@ReceiverAnno
	public void countryCopyFinish(CountryCopyFinishEvent event) {
		eventManager.countryCopyFinish(event);
	}

	@ReceiverAnno
	public void monsterKill(MonsterKillEvent event) {
		if (!event.isKnowPlayer()) {
			return;
		}
		eventManager.monsterKillEvent(event);
	}

}
