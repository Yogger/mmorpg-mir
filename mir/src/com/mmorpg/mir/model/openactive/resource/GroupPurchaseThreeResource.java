package com.mmorpg.mir.model.openactive.resource;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class GroupPurchaseThreeResource {
	@Id
	private String id;

	private String[] drawConditionIds;

	private String[] attendConditionIds;

	private String[] sendConditionIds;

	private String mailTitle;

	private String mailContent;

	private String rewardChooserId;

	@Transient
	private CoreConditions drawConditions;

	@Transient
	private CoreConditions attendConditions;

	@Transient
	private CoreConditions sendConditions;

	@JsonIgnore
	public CoreConditions getDrawCondition() {
		if (drawConditions == null) {
			drawConditions = CoreConditionManager.getInstance().getCoreConditions(1, drawConditionIds);
		}
		return drawConditions;
	}

	@JsonIgnore
	public CoreConditions getAttendConditions() {
		if (attendConditions == null) {
			attendConditions = CoreConditionManager.getInstance().getCoreConditions(1, attendConditionIds);
		}
		return attendConditions;
	}

	@JsonIgnore
	public CoreConditions getSendConditions() {
		if (sendConditions == null) {
			sendConditions = CoreConditionManager.getInstance().getCoreConditions(1, sendConditionIds);
		}
		return sendConditions;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String[] getDrawConditionIds() {
		return drawConditionIds;
	}

	public void setDrawConditionIds(String[] drawConditionIds) {
		this.drawConditionIds = drawConditionIds;
	}

	public String[] getAttendConditionIds() {
		return attendConditionIds;
	}

	public void setAttendConditionIds(String[] attendConditionIds) {
		this.attendConditionIds = attendConditionIds;
	}

	public String[] getSendConditionIds() {
		return sendConditionIds;
	}

	public void setSendConditionIds(String[] sendConditionIds) {
		this.sendConditionIds = sendConditionIds;
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

	public String getRewardChooserId() {
		return rewardChooserId;
	}

	public void setRewardChooserId(String rewardChooserId) {
		this.rewardChooserId = rewardChooserId;
	}
	
}
