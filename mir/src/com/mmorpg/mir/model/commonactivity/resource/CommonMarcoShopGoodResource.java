package com.mmorpg.mir.model.commonactivity.resource;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.action.CoreActionManager;
import com.mmorpg.mir.model.core.action.CoreActions;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Index;
import com.windforce.common.resource.anno.Resource;

@Resource
public class CommonMarcoShopGoodResource {
	public static final String ACTIVITY_NAME = "ACTIVITY_NAME";
	@Id
	private String id;
	/** 活动名字 */
	@Index(name = ACTIVITY_NAME)
	private String activityName;
	private int weight;
	private String rewardId;
	private String[] buyActionIds;

	private String i18name;
	/** 限购次数 0为不限购商品 */
	private int restrictCount;

	@Transient
	private transient CoreActions buyActions;

	@JsonIgnore
	public CoreActions getBuyActions() {
		if (buyActions == null) {
			buyActions = CoreActionManager.getInstance().getCoreActions(this.buyActionIds);
		}
		return buyActions;
	}

	@JsonIgnore
	public boolean isRestrictGood() {
		return this.restrictCount > 0;
	}

	public String getId() {
		return id;
	}

	public int getWeight() {
		return weight;
	}

	public String getRewardId() {
		return rewardId;
	}

	public String[] getBuyActionIds() {
		return buyActionIds;
	}

	public String getI18name() {
		return i18name;
	}

	public int getRestrictCount() {
		return restrictCount;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public void setRewardId(String rewardId) {
		this.rewardId = rewardId;
	}

	public void setBuyActionIds(String[] buyActionIds) {
		this.buyActionIds = buyActionIds;
	}

	public void setI18name(String i18name) {
		this.i18name = i18name;
	}

	public void setRestrictCount(int restrictCount) {
		this.restrictCount = restrictCount;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

}
