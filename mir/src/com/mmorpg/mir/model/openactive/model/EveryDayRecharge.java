package com.mmorpg.mir.model.openactive.model;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.mail.manager.MailManager;
import com.mmorpg.mir.model.mail.model.Mail;
import com.mmorpg.mir.model.openactive.OpenActiveConfig;
import com.mmorpg.mir.model.openactive.packet.SM_EveryDayRecharge_Draw;
import com.mmorpg.mir.model.openactive.packet.SM_EveryDay_Recharge_Draw_Out;
import com.mmorpg.mir.model.openactive.resource.OpenActiveEveryDayRechargeResource;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.windforce.common.utility.DateUtils;
import com.windforce.common.utility.New;

public class EveryDayRecharge {
	@Transient
	private transient Player owner;

	/** 已经领取 */
	private HashSet<String> rewarded;

	/** 没有领取的奖励 */
	private LinkedList<String> notDrawReward;

	/** 重置时间 */
	private long deprecatedTime;

	public static EveryDayRecharge valueOf() {
		EveryDayRecharge result = new EveryDayRecharge();
		result.rewarded = New.hashSet();
		result.notDrawReward = new LinkedList<String>();
		return result;
	}

	@JsonIgnore
	synchronized public void sendNotRewardMail() {
		if (!DateUtils.isToday(new Date(deprecatedTime))) {
			for (String resourceId : this.getNotDrawReward()) {
				OpenActiveEveryDayRechargeResource resource = OpenActiveConfig.getInstance().everyDayRechargeStorage
						.get(resourceId, true);
				List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(owner,
						resource.getRewardChooserGroupId());
				Reward reward = RewardManager.getInstance().creatReward(owner, rewardIds, null);
				I18nUtils titel18n = I18nUtils
						.valueOf(OpenActiveConfig.getInstance().EVERYDAY_RECHARGE_MAIL_TITLE_IL18N.getValue());
				I18nUtils contextl18n = I18nUtils
						.valueOf(OpenActiveConfig.getInstance().EVERYDAY_RECHARGE_MAIL_CONTENT_IL18N.getValue());
				Mail mail = Mail.valueOf(titel18n, contextl18n, null, reward);
				MailManager.getInstance().sendMail(mail, owner.getObjectId());
			}
			this.finishDraw();
			deprecatedTime = System.currentTimeMillis();
		}

	}

	@JsonIgnore
	synchronized public void drawReward(String resourceId) {
		OpenActiveEveryDayRechargeResource resource = OpenActiveConfig.getInstance().everyDayRechargeStorage.get(
				resourceId, true);
		if (!notDrawReward.contains(resource.getId())) {
			// 该档次奖励已经领取
			throw new ManagedException(ManagedErrorCode.EVERYDAY_RECHARGE_WITHOUT_REWARD_CANDRAW);
		}
		if (!resource.getCoreConditions().verify(owner, true)) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		if (!rewarded.contains(resource.getId()) && notDrawReward.contains(resource.getId())) {
			List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(owner,
					resource.getRewardChooserGroupId());
			RewardManager.getInstance().grantReward(owner, rewardIds,
					ModuleInfo.valueOf(ModuleType.EVERYDAY_RECHARGE, SubModuleType.DAILYCHARGE_REWARD, resourceId));
			rewarded.add(resource.getId());
			notDrawReward.remove(resource.getId());
			PacketSendUtility.sendPacket(owner, SM_EveryDayRecharge_Draw.valueOf(resource.getId()));
		}
		if (rewarded.size() >= OpenActiveConfig.getInstance().EVERYDAY_RECHARGE_GRADE.getValue()) {
			PacketSendUtility.sendPacket(owner, new SM_EveryDay_Recharge_Draw_Out());
		}
	}

	@JsonIgnore
	synchronized public void refreshNotReward() {
		sendNotRewardMail();
		this.notDrawReward.clear();
		this.deprecatedTime = System.currentTimeMillis();
		for (OpenActiveEveryDayRechargeResource resource : OpenActiveConfig.getInstance().everyDayRechargeStorage
				.getAll()) {
			if (!rewarded.contains(resource.getId()) && resource.getCoreConditions().verify(owner, false)) {
				notDrawReward.add(resource.getId());
			}
		}
	}

	@JsonIgnore
	synchronized public void finishDraw() {
		rewarded.clear();
		notDrawReward.clear();
	}

	public HashSet<String> getRewarded() {
		return rewarded;
	}

	public void setRewarded(HashSet<String> rewarded) {
		this.rewarded = rewarded;
	}

	public LinkedList<String> getNotDrawReward() {
		return notDrawReward;
	}

	public void setNotDrawReward(LinkedList<String> notDrawReward) {
		this.notDrawReward = notDrawReward;
	}

	@JsonIgnore
	public Player getOwner() {
		return owner;
	}

	@JsonIgnore
	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public long getDeprecatedTime() {
		return deprecatedTime;
	}

	public void setDeprecatedTime(long deprecatedTime) {
		this.deprecatedTime = deprecatedTime;
	}

}
