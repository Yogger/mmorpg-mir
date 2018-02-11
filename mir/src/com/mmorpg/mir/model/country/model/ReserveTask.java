package com.mmorpg.mir.model.country.model;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.country.resource.ConfigValueManager;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.mail.manager.MailManager;
import com.mmorpg.mir.model.mail.model.Mail;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.reward.model.Reward;

/**
 * 储君任务
 * 
 * @author 37.com
 * 
 */
public class ReserveTask {
	/** 任务类型 */
	private int taskType;
	/** 完成次数 */
	private int finishCount;

	// 构造函数
	public static ReserveTask valueOf(ReserveTaskEnum taskType) {
		ReserveTask result = new ReserveTask();
		result.taskType = taskType.getCode();
		result.finishCount = 0;
		return result;
	}

	// 业务方法
	@JsonIgnore
	public void addFinishCount() {
		this.finishCount++;
	}

	@JsonIgnore
	public void rewardMail(Player owner) {
		if (finishCount >= ConfigValueManager.getInstance().getTaskRequestCount(ReserveTaskEnum.typeOf(taskType))) {
			Reward r = getRewards(owner);
			I18nUtils titel18n = I18nUtils.valueOf(ConfigValueManager.getInstance().RESERVEKING_REWARD_MAIL_TITLE_IL18N
					.getValue());
			I18nUtils contextl18n = I18nUtils
					.valueOf(ConfigValueManager.getInstance().RESERVEKING_REWARD_MAIL_CONTENT_IL18N.getValue());
			contextl18n.addParm("name", I18nPack.valueOf(owner.getName()));
			contextl18n.addParm("country", I18nPack.valueOf(owner.getCountry().getName()));
			Mail mail = Mail.valueOf(titel18n, contextl18n, null, r);
			MailManager.getInstance().sendMail(mail, owner.getObjectId());
		}
	}

	@JsonIgnore
	public void reward(Player owner) {
		Reward rewards = getRewards(owner);
		RewardManager.getInstance().grantReward(owner, rewards,
				ModuleInfo.valueOf(ModuleType.RESERVEKING, SubModuleType.RESERVEKING_TASK_REWARD));
	}

	@JsonIgnore
	private Reward getRewards(Player owner) {
		String rewardId = ConfigValueManager.getInstance().getReserveKingTaskRewardId(ReserveTaskEnum.typeOf(taskType));
		int precount = ConfigValueManager.getInstance().getTaskRequestCount(ReserveTaskEnum.typeOf(taskType));
		int rewardCount = finishCount / precount;
		finishCount %= precount;
		Reward rewards = Reward.valueOf();
		for (int i = 0; i < rewardCount; i++) {
			rewards.addReward(RewardManager.getInstance().creatReward(owner, rewardId, null));
		}
		return rewards;
	}

	@JsonIgnore
	public int getRewardCount() {
		int precount = ConfigValueManager.getInstance().getTaskRequestCount(ReserveTaskEnum.typeOf(taskType));
		int rewardCount = finishCount / precount;
		return rewardCount;
	}

	// getter - setter
	public int getTaskType() {
		return taskType;
	}

	public void setTaskType(int taskType) {
		this.taskType = taskType;
	}

	public int getFinishCount() {
		return finishCount;
	}

	public void setFinishCount(int finishCount) {
		this.finishCount = finishCount;
	}

}
