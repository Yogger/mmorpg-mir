package com.mmorpg.mir.model.openactive.resource;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.action.CoreActionManager;
import com.mmorpg.mir.model.core.action.CoreActions;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class OpenActiveEnhanceResource {
	@Id
	private String id;
	/** 领取的条件 */
	private String[] conditonIds;
	/** 领取消耗 */
	private String[] actionIds;
	/** 奖励 */
	private String rewardChooserId;
	
	private String mailTitle;
	
	private String mailContent;
	
	/** 领取邮件奖励条件 */
	private String[] mailRewardCond;
	
	private String[] logDateCond;
	
	@Transient
	private CoreConditions mailCondition;
	
	@Transient
	private CoreConditions coreConditions;

	@Transient
	private CoreActions coreActions;
	
	@Transient
	private CoreConditions logDateConditions;

	@JsonIgnore
	public CoreConditions getLogDateConditions() {
		if (logDateConditions == null) {
			logDateConditions = CoreConditionManager.getInstance().getCoreConditions(1, logDateCond);
		}
		return logDateConditions;
	}
	
	@JsonIgnore
	public CoreConditions getMailConditions() {
		if (mailCondition == null) {
			mailCondition = CoreConditionManager.getInstance().getCoreConditions(1, mailRewardCond);
		}
		return mailCondition;
	}

	
	@JsonIgnore
	public CoreConditions getCoreConditions() {
		if (coreConditions == null) {
			coreConditions = CoreConditionManager.getInstance().getCoreConditions(1, conditonIds);
		}
		return coreConditions;
	}

	@JsonIgnore
	public void setCoreConditions(CoreConditions coreConditions) {
		this.coreConditions = coreConditions;
	}

	@JsonIgnore
	public CoreActions getCoreActions() {
		if (coreActions == null) {
			coreActions = CoreActionManager.getInstance().getCoreActions(1, actionIds);
		}
		return coreActions;
	}

	@JsonIgnore
	public void setCoreActions(CoreActions coreActions) {
		this.coreActions = coreActions;
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

	public String[] getActionIds() {
		return actionIds;
	}

	public void setActionIds(String[] actionIds) {
		this.actionIds = actionIds;
	}

	public String getRewardChooserId() {
		return rewardChooserId;
	}

	public void setRewardChooserId(String rewardChooserId) {
		this.rewardChooserId = rewardChooserId;
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


	public String[] getMailRewardCond() {
		return mailRewardCond;
	}


	public void setMailRewardCond(String[] mailRewardCond) {
		this.mailRewardCond = mailRewardCond;
	}


	public String[] getLogDateCond() {
		return logDateCond;
	}


	public void setLogDateCond(String[] logDateCond) {
		this.logDateCond = logDateCond;
	}

}
