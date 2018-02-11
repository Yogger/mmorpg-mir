package com.mmorpg.mir.model.welfare.resource;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class GiftResource {

	@Id
	private int id;

	private String[] rewardCondition;
	private String rewardChooserGroup;
	private String type;

	private String mailTitle;

	private String mailText;

	/** 奖励是否已经领取过了的条件 */
	private String[] giftRecievedCondition;

	@Transient
	private CoreConditions conditions;

	@Transient
	private CoreConditions recievedConditions;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String[] getRewardCondition() {
		return rewardCondition;
	}

	public void setRewardCondition(String[] rewardCondition) {
		this.rewardCondition = rewardCondition;
	}

	public String getRewardChooserGroup() {
		return rewardChooserGroup;
	}

	public void setRewardChooserGroup(String rewardChooserGroup) {
		this.rewardChooserGroup = rewardChooserGroup;
	}

	public String[] getGiftRecievedCondition() {
		return giftRecievedCondition;
	}

	public void setGiftRecievedCondition(String[] giftRecievedCondition) {
		this.giftRecievedCondition = giftRecievedCondition;
	}

	@JsonIgnore
	public CoreConditions getConditions() {
		if (conditions == null) {
			if (rewardCondition == null || rewardCondition.length == 0) {
				conditions = new CoreConditions();
			} else {
				conditions = CoreConditionManager.getInstance().getCoreConditions(1, rewardCondition);
			}
		}
		return conditions;
	}

	@JsonIgnore
	public CoreConditions getRecievedConditions() {
		if (recievedConditions == null) {
			if (giftRecievedCondition == null) {
				recievedConditions = new CoreConditions();
			} else {
				recievedConditions = CoreConditionManager.getInstance().getCoreConditions(1, giftRecievedCondition);
			}
		}
		return recievedConditions;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMailTitle() {
		return mailTitle;
	}

	public void setMailTitle(String mailTitle) {
		this.mailTitle = mailTitle;
	}

	public String getMailText() {
		return mailText;
	}

	public void setMailText(String mailText) {
		this.mailText = mailText;
	}

}
