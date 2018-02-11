package com.mmorpg.mir.model.commonactivity.resource;

import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Index;
import com.windforce.common.resource.anno.Resource;

@Resource
public class CommonRechargeActiveResource {

	public static final String ACTIVE_NAME_INDEX = "ACTIVE_NAME_INDEX";
	@Id
	private String id;
	/** 活动名字 */
	@Index(name = ACTIVE_NAME_INDEX, unique = true)
	private String activeName;

	/** 最小元宝要求 */
	private int miniGoldRequired;

	/** 奖励选择器id */
	private String rewardGroupId;

	/** 活动时间 */
	private String[] activityTimeConds;

	/** 发放邮件时间条件 */
	private String[] compentConds;

	private String rewardTitleIl18nId;

	private String rewardContentIl18nId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getActiveName() {
		return activeName;
	}

	public void setActiveName(String activeName) {
		this.activeName = activeName;
	}

	public int getMiniGoldRequired() {
		return miniGoldRequired;
	}

	public void setMiniGoldRequired(int miniGoldRequired) {
		this.miniGoldRequired = miniGoldRequired;
	}

	public String getRewardGroupId() {
		return rewardGroupId;
	}

	public void setRewardGroupId(String rewardGroupId) {
		this.rewardGroupId = rewardGroupId;
	}

	public String[] getCompentConds() {
		return compentConds;
	}

	public void setCompentConds(String[] compentConds) {
		this.compentConds = compentConds;
	}

	public String getRewardTitleIl18nId() {
		return rewardTitleIl18nId;
	}

	public void setRewardTitleIl18nId(String rewardTitleIl18nId) {
		this.rewardTitleIl18nId = rewardTitleIl18nId;
	}

	public String getRewardContentIl18nId() {
		return rewardContentIl18nId;
	}

	public void setRewardContentIl18nId(String rewardContentIl18nId) {
		this.rewardContentIl18nId = rewardContentIl18nId;
	}

	public String[] getActivityTimeConds() {
		return activityTimeConds;
	}

	public void setActivityTimeConds(String[] activityTimeConds) {
		this.activityTimeConds = activityTimeConds;
	}

}
