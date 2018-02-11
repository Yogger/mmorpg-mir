package com.mmorpg.mir.model.welfare.manager;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.mail.manager.MailManager;
import com.mmorpg.mir.model.mail.model.Mail;
import com.mmorpg.mir.model.moduleopen.manager.ModuleOpenManager;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.welfare.model.ActiveEnum;
import com.mmorpg.mir.model.welfare.model.ActiveStatusEnum;
import com.mmorpg.mir.model.welfare.packet.SM_Active_Num;
import com.mmorpg.mir.model.welfare.packet.SM_Active_Value;
import com.mmorpg.mir.model.welfare.packet.SM_Welfare_Active_Open_Panel;
import com.mmorpg.mir.model.welfare.packet.SM_Welfare_Push_Light_Reward;
import com.mmorpg.mir.model.welfare.resource.ActiveResource;
import com.mmorpg.mir.model.welfare.resource.ActiveRewardResource;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;
import com.windforce.common.utility.DateUtils;

@Component
public class ActiveManager implements IActiveManager {

	private static ActiveManager instance;

	@Static("WELFARE:ACTIVE_VALUE_INIT_VALUE")
	public ConfigValue<Integer> ACTIVE_VALUE_INIT_VALUE;
	@Static
	public Storage<Integer, ActiveResource> activeStorage;
	@Static
	public Storage<Integer, ActiveRewardResource> activeRewardStorage;

	@PostConstruct
	public void init() {
		instance = this;
	}

	public static ActiveManager getInstance() {
		return instance;
	}

	public ActiveResource getActiveResource(int eventId) {
		return activeStorage.get(eventId, true);
	}

	public boolean containsActiveResource(int eventId) {
		return activeStorage.containsId(eventId);
	}

	public ActiveRewardResource getActiveRewardResource(int activeValue) {
		return activeRewardStorage.get(activeValue, true);
	}

	public void exec(Player player, ActiveEnum activeEnum, int value) {
		int eventId = activeEnum.getEventId();
		if (!containsActiveResource(eventId)) {
			return;
		}
		if (player.getWelfare().getActiveValue().isNull()) {
			initActives(player);
		}
		int status = player.getWelfare().getActiveValue().getExeStatus(activeEnum);
		if (status == ActiveStatusEnum.STATUS_NOT_OPEN.getStatus()) {
			return;
		}
		if (status == ActiveStatusEnum.STATUS_COMPLETED.getStatus()) {
			// if (activeEnum == ActiveEnum.ACTIVE_VIP && value != 0) {
			// player.getWelfare().getActiveValue().addValue(value);
			// PacketSendUtility.sendPacket(player,
			// SM_Welfare_Active_Open_Panel.valueOf(player));
			// PacketSendUtility.sendPacket(player,
			// SM_Active_Num.valueOf(PublicWelfareManager.getInstance().countActiveCount(player)));
			// PacketSendUtility.sendPacket(player,
			// SM_Active_Value.valueOf(activeEnum.getEventId(), value));
			//
			// }
			return;
		}
		int num = player.getWelfare().getActiveValue().getExeNum(activeEnum) + value;
		player.getWelfare().getActiveValue().setLoad(eventId, num);
		ActiveResource res = getActiveResource(eventId);
		if (num >= res.getExeNum()) {
			int addValue = res.getActiveValue();
			// if (activeEnum == ActiveEnum.ACTIVE_VIP) {
			// addValue = player.getVip().getResource().getAddActive();
			// }
			player.getWelfare().getActiveValue().addValue(addValue);
			player.getWelfare().getActiveValue().updateStatus(activeEnum, ActiveStatusEnum.STATUS_COMPLETED);
			PacketSendUtility.sendPacket(player, SM_Welfare_Active_Open_Panel.valueOf(player));
			PacketSendUtility.sendPacket(player,
					SM_Welfare_Push_Light_Reward.valueOf(PublicWelfareManager.getInstance().countLightNum(player)));
			PacketSendUtility.sendPacket(player,
					SM_Active_Num.valueOf(PublicWelfareManager.getInstance().countActiveCount(player)));
			PacketSendUtility.sendPacket(player, SM_Active_Value.valueOf(activeEnum.getEventId(), addValue));
		}
	}

	public void exec(Player player, ActiveEnum activeEnum) {
		exec(player, activeEnum, 1);
	}

	/**
	 * 检查活跃值数据
	 * 
	 * @param player
	 */
	public void check(Player player) {
		// 如果没有数据就初始化
		if (player.getWelfare().getActiveValue().isNull()) {
			initActives(player);
		}
		long playerResetTime = player.getWelfare().getActiveValue().getResetTime();
		long now = System.currentTimeMillis();
		// 上次清理时间到现在已经过了1天,重置
		if (DateUtils.calcIntervalDays(new Date(playerResetTime), new Date(now)) > 0) {
			initActives(player);
		}
	}

	private void initActives(Player player) {
		for (ActiveEnum e : ActiveEnum.values()) {
			int eventId = e.getEventId();
			if (!containsActiveResource(eventId)) {
				continue;
			}
			ActiveResource res = getActiveResource(eventId);
			boolean isOpen = false;
			if (res.getOpenModuleId() == null || res.getOpenModuleId().length() == 0) {
				isOpen = true;
			} else {
				isOpen = ModuleOpenManager.getInstance().isOpenByKey(player, res.getOpenModuleId());
			}
			if (!isOpen) {
				player.getWelfare().getActiveValue().updateStatus(e, ActiveStatusEnum.STATUS_NOT_OPEN);
			} else {
				player.getWelfare().getActiveValue().updateStatus(e, ActiveStatusEnum.STATUS_NOT_COMPLETED);
			}
			player.getWelfare().getActiveValue().setLoad(eventId, 0);
		}
		player.getWelfare().getActiveValue()
				.setActiveValue(0);
		player.getWelfare().getActiveValue().getRewardedList().clear();
		player.getWelfare().getActiveValue().setResetTime(DateUtils.getFirstTime(new Date()).getTime());
	}

	public void moduleOpen(Player player) {
		for (ActiveEnum e : ActiveEnum.values()) {
			int eventId = e.getEventId();
			if (!containsActiveResource(eventId)) {
				continue;
			}
			ActiveResource res = getActiveResource(eventId);
			Integer status = player.getWelfare().getActiveValue().getExeStatusMap().get(e.getEventId());
			if (status != null
					&& (status == ActiveStatusEnum.STATUS_COMPLETED.getStatus() || status == ActiveStatusEnum.STATUS_NOT_COMPLETED
							.getStatus())) {
				continue;
			}
			boolean isOpen = false;
			if (res.getOpenModuleId() == null || res.getOpenModuleId().length() == 0) {
				isOpen = true;
			} else {
				isOpen = ModuleOpenManager.getInstance().isOpenByKey(player, res.getOpenModuleId());
			}
			if (!isOpen) {
				player.getWelfare().getActiveValue().updateStatus(e, ActiveStatusEnum.STATUS_NOT_OPEN);
			} else {
				player.getWelfare().getActiveValue().updateStatus(e, ActiveStatusEnum.STATUS_NOT_COMPLETED);
			}
			player.getWelfare().getActiveValue().setLoad(eventId, 0);
		}
	}

	/**
	 * 没有领取的活跃值奖励邮件
	 * 
	 * @param player
	 */
	public void sendActiveNotRewardMail(Player player) {
		long resetTime = player.getWelfare().getActiveValue().getResetTime();
		if (DateUtils.isToday(new Date(resetTime))) {
			return;
		}
		if (!ModuleOpenManager.getInstance().isOpenByKey(player, "opmk63")) {
			return;
		}
		for (ActiveRewardResource resource : activeRewardStorage.getAll()) {
			if (player.getWelfare().getActiveValue().isRewarded(resource.getActiveValue())) {
				continue;
			}
			int v = player.getWelfare().getActiveValue().getActiveValue();

			if (v < resource.getActiveValue()) {
				continue;
			}
			String groupId = resource.getGroupId();
			List<String> result = ChooserManager.getInstance().chooseValueByRequire(player, groupId);

			Reward reward = RewardManager.getInstance().creatReward(player, result, null);

			I18nUtils titel18n = I18nUtils
					.valueOf(WelfareConfigValueManager.getInstance().ACTIVE_REWARD_MAIL_TITLE_IL18N.getValue());
			I18nUtils contextl18n = I18nUtils
					.valueOf(WelfareConfigValueManager.getInstance().ACTIVE_REWARD_MAIL_CONTENT_IL18N.getValue());
			Mail mail = Mail.valueOf(titel18n, contextl18n, null, reward);
			MailManager.getInstance().sendMail(mail, player.getObjectId());
			player.getWelfare().getGiftCollect().addTotalAmount(resource.getGiftCount());
		}
	}
}
