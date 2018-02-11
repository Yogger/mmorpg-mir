package com.mmorpg.mir.model.commonactivity.resource;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.commonactivity.model.RecollectType;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.gameobjects.Player;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Index;
import com.windforce.common.resource.anno.Resource;

@Resource
public class CommonRecollectResource {
	
	public static final String ACTIVITYNAME = "ACTIVITYNAME";
	@Id
	private String id;
	/** 事件开启的模块ID **/
	private String opmkId;
	/** 活动的事件类型 **/
	private RecollectType eventType;
	/** 活动的每日基础次数 **/
	private int baseCount;
	/** 活动的ID组 **/
	private String[] recollectGroupIds;
	/** 活动记录数据的时间条件 **/
	private String[] logDataCond;
	/** 活动找回的时间条件 **/
	private String[] clawbackCond;
	@Index(name = ACTIVITYNAME)
	private boolean currentActivity;
	/** 活动的名字 **/	
	private String activityName;
	/** 铜币消耗groupId */
	private String copperActionId;
	/** 金币消耗groupId */
	private String goldActionId;
	/** 奖励chooserGroupId **/
	private String rewardGroupId;
	
	@Transient
	private CoreConditions logDataConditions;
	
	@Transient
	private CoreConditions clawbackConditions;
	
	@JsonIgnore
	public CoreConditions getLogDataConditions() {
		if (logDataConditions == null) {
			logDataConditions = CoreConditionManager.getInstance().getCoreConditions(1, logDataCond);
		}
		return logDataConditions;
	}
	
	@JsonIgnore
	public CoreConditions getClawbackConditions() {
		if (clawbackConditions == null) {
			clawbackConditions = CoreConditionManager.getInstance().getCoreConditions(1, clawbackCond);
		}
		return clawbackConditions;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOpmkId() {
		return opmkId;
	}

	public void setOpmkId(String opmkId) {
		this.opmkId = opmkId;
	}

	public RecollectType getEventType() {
		return eventType;
	}

	public void setEventType(RecollectType eventType) {
		this.eventType = eventType;
	}

	public String[] getLogDataCond() {
		return logDataCond;
	}

	public void setLogDataCond(String[] logDataCond) {
		this.logDataCond = logDataCond;
	}

	public String[] getClawbackCond() {
		return clawbackCond;
	}

	public void setClawbackCond(String[] clawbackCond) {
		this.clawbackCond = clawbackCond;
	}

	public boolean isCurrentActivity() {
		return currentActivity;
	}

	public void setCurrentActivity(boolean currentActivity) {
		this.currentActivity = currentActivity;
	}

	public String getRewardGroupId() {
		return rewardGroupId;
	}

	public void setRewardGroupId(String rewardGroupId) {
		this.rewardGroupId = rewardGroupId;
	}

	public String[] getRecollectGroupIds() {
		return recollectGroupIds;
	}

	public void setRecollectGroupIds(String[] recollectGroupIds) {
		this.recollectGroupIds = recollectGroupIds;
	}

	public String getCopperActionId() {
		return copperActionId;
	}

	public void setCopperActionId(String copperActionId) {
		this.copperActionId = copperActionId;
	}

	public String getGoldActionId() {
		return goldActionId;
	}

	public void setGoldActionId(String goldActionId) {
		this.goldActionId = goldActionId;
	}

	public int getBaseCount() {
		return baseCount;
	}

	public void setBaseCount(int baseCount) {
		this.baseCount = baseCount;
	}
	
	@JsonIgnore
	public int getMaxCount(Player player, long time) {
		return baseCount + eventType.getVipExtraCount(player, time);
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

}
