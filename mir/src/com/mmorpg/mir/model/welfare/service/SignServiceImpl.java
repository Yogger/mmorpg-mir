package com.mmorpg.mir.model.welfare.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.core.action.CoreActionType;
import com.mmorpg.mir.model.core.action.CurrencyAction;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.purse.model.CurrencyType;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.welfare.event.SignEvent;
import com.mmorpg.mir.model.welfare.manager.PublicWelfareManager;
import com.mmorpg.mir.model.welfare.manager.SignManager;
import com.mmorpg.mir.model.welfare.packet.SM_Active_Num;
import com.mmorpg.mir.model.welfare.packet.SM_Welfare_Push_Light_Reward;
import com.mmorpg.mir.model.welfare.packet.SM_Welfare_Sign_Fill_Sign;
import com.mmorpg.mir.model.welfare.packet.SM_Welfare_Sign_Ing;
import com.mmorpg.mir.model.welfare.packet.SM_Welfare_Sign_Open_Panel;
import com.mmorpg.mir.model.welfare.packet.SM_Welfare_Sign_Reward;
import com.mmorpg.mir.model.welfare.resource.SignResource;
import com.mmorpg.mir.model.welfare.util.Util;
import com.windforce.common.event.core.EventBusManager;
import com.windforce.common.utility.DateUtils;

@Component
public class SignServiceImpl implements SignService {

	@Autowired
	private SignManager signManager;

	/**
	 * 打开签到面板
	 * 
	 * @param player
	 * @param reqTimeMillis
	 *            前端请求的当前时间
	 */
	public void openSign(Player player) {
		// 清理 ----- 签到满30次才清零...
		boolean todaySinged = DateUtils.isToday(new Date(player.getWelfare().getSign().getLastTime()));
		if ((!todaySinged) && player.getWelfare().getSign().getTotalSignNum() >= 30) {
			player.getWelfare().getSign().clearAll();
		}
		if (Util.getInstance().passMonth(System.currentTimeMillis(), player.getWelfare().getSign().getLastTime())) {
			player.getWelfare().getSign().clearWhenPassMonth();
		}
		// 检查前端的今天和后端今天是否一致
		// if (!DateUtils.isToday(new Date(reqTimeMillis))) {
		// PacketSendUtility.sendErrorMessage(player,
		// ManagedErrorCode.WELFARE_SIGN_CURRENT_DAY_NOT_SAME);
		// return;
		// }
		PacketSendUtility.sendPacket(player, SM_Welfare_Sign_Open_Panel.valueOf(player));
	}

	/**
	 * 签到 前当天的
	 * 
	 * @param player
	 * @param reqTimeMillis
	 *            前端请求的当前时间
	 */
	public void signing(Player player) {
		if (player.getWelfare().getSign().getTotalSignNum() >= 30) {
			player.getWelfare().getSign().clearAll();
		}
		long reqTimeMillis = System.currentTimeMillis();
		if (player.getWelfare().getSign().addSign(reqTimeMillis)) {// 签到成功
			// LogManager
			PacketSendUtility.sendPacket(player, SM_Welfare_Sign_Ing.valueOf(player));
			// signManager.broadcast(player);
			// vip直接发放奖励
			if (player.getVip().getResource().isExtraSignedReward()) {
				SignResource resource = signManager.getSignResource(Util.getInstance().getDay(reqTimeMillis), false);
				String[] vipRewards = resource.getVipRewards();
				if (vipRewards != null && vipRewards.length > 0) {
					RewardManager.getInstance().grantReward(player, Arrays.asList(vipRewards),
							ModuleInfo.valueOf(ModuleType.SIGN, SubModuleType.SIGN_VIP_REWARD));
				}
			}
			EventBusManager.getInstance().submit(SignEvent.valueOf(player));
			PacketSendUtility.sendPacket(player,
					SM_Welfare_Push_Light_Reward.valueOf(PublicWelfareManager.getInstance().countLightNum(player)));
			PacketSendUtility.sendPacket(player,
					SM_Active_Num.valueOf(PublicWelfareManager.getInstance().countActiveCount(player)));
		} else {
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.WELFARE_SIGN_REPEAT);
		}
	}

	/**
	 * 补签
	 * 
	 * @param player
	 * @param reqTimeMillis
	 *            前端请求补签那天的时间-毫秒,可以是那天的任意时间点
	 */
	public void fillSign(Player player, int reqTimeSeconds) {
		if (player.getWelfare().getSign().getTotalSignNum() >= 30) {
			player.getWelfare().getSign().clearAll();
		}
		long reqTimeMillis = reqTimeSeconds * 1000L;
		if (!player.getVip().getResource().isCanFillSign()) {
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.WELFARE_SIGN__NOT_VIP_FILLSIGN);
			return;
		}
		if (reqTimeMillis < player.getPlayerEnt().getStat().getCreatedOn().getTime()) {
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.WELFARE_SIGN_CREATE_TIME_TOO_SMALL);
			return;
		}
		long now = System.currentTimeMillis();// 后端当前系统时间
		if (!Util.getInstance().isSameMonth(reqTimeMillis, now) || DateUtils.isToday(new Date(reqTimeMillis))
				|| DateUtils.calcIntervalDays(new Date(reqTimeMillis), new Date(now)) < 1) {
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.WELFARE_SIGN_CURRENT_DAY_NOT_SAME);
			return;
		}

		// 补签次数用完
		if (!player.getWelfare().getSign().canFillSign(player)) {
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.WELFARE_SIGN_FILL_SIGN_NUM_NOT_ENGOUTH);
		} else if (!player.getWelfare().getSign().canSign(reqTimeMillis)) {
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.WELFARE_SIGN_REPEAT);
		} else {
			CurrencyAction actions = CoreActionType.createCurrencyCondition(CurrencyType.GOLD, player.getVip()
					.getResource().getGoldAct());
			actions.verify(player);
			actions.act(player, ModuleInfo.valueOf(ModuleType.VIP, SubModuleType.FILL_SIGN));
			player.getWelfare().getSign().addSign(reqTimeMillis);
			player.getWelfare().getSign().addCurrentFillSignNum();
			int signTotalTimes = player.getWelfare().getSign().getTotalSignNum();

			if (player.getVip().getResource().isExtraSignedReward()) {
				SignResource resource = signManager.getSignResource(signTotalTimes, false);
				String[] vipRewards = resource.getVipRewards();
				if (vipRewards != null && vipRewards.length > 0) {
					RewardManager.getInstance().grantReward(player, Arrays.asList(vipRewards),
							ModuleInfo.valueOf(ModuleType.SIGN, SubModuleType.SIGN_VIP_REWARD));
				}
			}
			// 补签成功
			PacketSendUtility.sendPacket(
					player,
					SM_Welfare_Sign_Fill_Sign.valueOf(reqTimeMillis, signTotalTimes, player.getWelfare().getSign()
							.getTotalSignCount()));
			// signManager.broadcast(player);
			PacketSendUtility.sendPacket(player,
					SM_Welfare_Push_Light_Reward.valueOf(PublicWelfareManager.getInstance().countLightNum(player)));
			PacketSendUtility.sendPacket(player,
					SM_Active_Num.valueOf(PublicWelfareManager.getInstance().countActiveCount(player)));

		}
	}

	/**
	 * 领取签到奖励
	 * 
	 * @param player
	 * @param days
	 *            累计签到的天数
	 */
	public void signReward(Player player, int days) {
		// 累计天数不够
		if (player.getWelfare().getSign().getTotalSignNum() < days) {
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.WELFARE_SIGN_NOT_PASS);
			return;
		}
		// 已经领取过奖励
		if (player.getWelfare().getSign().isRewarded(days)) {
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.WELFARE_SIGN_REWARDED_DEF);
			return;
		}
		// 发放奖励
		SignResource resource = signManager.getSignResource(days, false);
		String rewardChooserGroupId = resource.getDefaultRewadChooserGroup();
		if (rewardChooserGroupId != null && rewardChooserGroupId.length() > 0) {
			List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(player, rewardChooserGroupId);
			player.getWelfare().getSign().rewarded(days);
			RewardManager.getInstance().grantReward(player, rewardIds,
					ModuleInfo.valueOf(ModuleType.SIGN, SubModuleType.SIGN_REWARD));
			PacketSendUtility.sendPacket(player, SM_Welfare_Sign_Reward.valueOf(days));
			PacketSendUtility.sendPacket(player,
					SM_Welfare_Push_Light_Reward.valueOf(PublicWelfareManager.getInstance().countLightNum(player)));
			PacketSendUtility.sendPacket(player,
					SM_Active_Num.valueOf(PublicWelfareManager.getInstance().countActiveCount(player)));
		} else {
			PacketSendUtility.sendPacket(player, SM_Welfare_Sign_Reward.valueOf(-1));
		}
	}

}
