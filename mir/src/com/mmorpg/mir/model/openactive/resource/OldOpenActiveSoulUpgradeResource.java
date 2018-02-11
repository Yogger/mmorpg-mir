package com.mmorpg.mir.model.openactive.resource;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class OldOpenActiveSoulUpgradeResource {

	@Id
	private String id;
	/** 领取的条件 */
	private String[] conditonIds;
	/** 奖励 */
	private String rewardId;

	/** 领取邮件奖励条件 */
	private String[] mailRewardCond;
	/** 领取邮件标题 */
	private String mailTitle;
	/** 领取邮件内容 */
	private String mailContent;

	@Transient
	private CoreConditions mailCondition;

	@JsonIgnore
	public CoreConditions getMailConditions() {
		if (mailCondition == null) {
			mailCondition = CoreConditionManager.getInstance().getCoreConditions(1, mailRewardCond);
		}
		return mailCondition;
	}

	@Transient
	private CoreConditions coreConditions;

	@JsonIgnore
	public CoreConditions getCoreConditions() {
		if (coreConditions == null) {
			coreConditions = CoreConditionManager.getInstance().getCoreConditions(1, conditonIds);
		}
		return coreConditions;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String[] getConditonIds() {
		return conditonIds;
	}

	public void setConditonIds(String[] conditonIds) {
		this.conditonIds = conditonIds;
	}

	public String getRewardId() {
		return rewardId;
	}

	public void setRewardId(String rewardId) {
		this.rewardId = rewardId;
	}

	public String[] getMailRewardCond() {
		return mailRewardCond;
	}

	public void setMailRewardCond(String[] mailRewardCond) {
		this.mailRewardCond = mailRewardCond;
	}

	public String getMailTitle() {
		return mailTitle;
	}

	public void setMailTitle(String mailTitle) {
		this.mailTitle = mailTitle;
	}

	public String getMailContent() {
		return mailContent;
	}

	public void setMailContent(String mailContent) {
		this.mailContent = mailContent;
	}

}
