package com.mmorpg.mir.model.openactive.resource;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class OpenActiveArtifactResource {
	@Id
	private String id;
	/** 领取的条件 */
	private String[] conditonIds;
	/** 奖励 */
	private String rewardId;
	
	private int minGrade;
	
	private String[] mailCondIds;
	
	private String mailTitle;
	
	private String mailContent;
	
	@Transient
	private CoreConditions mailTimeConditions;
	
	@JsonIgnore
	public CoreConditions getMailTimeCondition() {
		if (mailTimeConditions == null) {
			mailTimeConditions = CoreConditionManager.getInstance().getCoreConditions(1, mailCondIds);
		}
		return mailTimeConditions;
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

	public int getMinGrade() {
		return minGrade;
	}

	public void setMinGrade(int minGrade) {
		this.minGrade = minGrade;
	}

	public String[] getMailCondIds() {
		return mailCondIds;
	}

	public void setMailCondIds(String[] mailCondIds) {
		this.mailCondIds = mailCondIds;
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
