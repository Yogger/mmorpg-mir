package com.mmorpg.mir.model.commonactivity.resource;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Index;
import com.windforce.common.resource.anno.Resource;

@Resource
public class CommonTreasureActivityResource {

	public static final String ACTIVITY_NAME = "ACTIVITY_NAME";

	@Id
	private String id;

	@Index(name = ACTIVITY_NAME)
	private String activityName;

	private String rewardChoserId;

	private String[] mailConds;

	private String titleIl18n;

	private String contentIl18n;

	@JsonIgnore
	public CoreConditions getMailConditions() {
		return CoreConditionManager.getInstance().getCoreConditions(1, this.mailConds);
	}

	public String getId() {
		return id;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getRewardChoserId() {
		return rewardChoserId;
	}

	public void setRewardChoserId(String rewardChoserId) {
		this.rewardChoserId = rewardChoserId;
	}

	public String[] getMailConds() {
		return mailConds;
	}

	public void setMailConds(String[] mailConds) {
		this.mailConds = mailConds;
	}

	public String getTitleIl18n() {
		return titleIl18n;
	}

	public void setTitleIl18n(String titleIl18n) {
		this.titleIl18n = titleIl18n;
	}

	public String getContentIl18n() {
		return contentIl18n;
	}

	public void setContentIl18n(String contentIl18n) {
		this.contentIl18n = contentIl18n;
	}

}
