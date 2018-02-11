package com.mmorpg.mir.model.commonactivity.resource;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Index;
import com.windforce.common.resource.anno.Resource;

@Resource
public class CommonBossResource {

	public static final String ACTIVITY_NAME_INDEX = "ACTIVITY_NAME_INDEX";
	/** spawnId */
	@Id
	private String id;

	@Index(name = ACTIVITY_NAME_INDEX)
	private String activityName;

	private int countryId;

	private String rewardChoserId;

	private String[] bossTimeConds;

	private String[] rewardConds;

	private String noticeIl18nId;

	private Integer noticeChannel;

	private String rewardMailTitleChoserId;

	private String rewardMailContentChoserId;

	private String attackIl18nId;

	private int attackChannel;

	@JsonIgnore
	public CoreConditions getBossTimeCondition() {
		return CoreConditionManager.getInstance().getCoreConditions(1, bossTimeConds);
	}

	@JsonIgnore
	public CoreConditions getRewardCondition() {
		return CoreConditionManager.getInstance().getCoreConditions(1, rewardConds);
	}

	public String[] getBossTimeConds() {
		return bossTimeConds;
	}

	public void setBossTimeConds(String[] bossTimeConds) {
		this.bossTimeConds = bossTimeConds;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getCountryId() {
		return countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	public String getNoticeIl18nId() {
		return noticeIl18nId;
	}

	public void setNoticeIl18nId(String noticeIl18nId) {
		this.noticeIl18nId = noticeIl18nId;
	}

	public Integer getNoticeChannel() {
		return noticeChannel;
	}

	public void setNoticeChannel(Integer noticeChannel) {
		this.noticeChannel = noticeChannel;
	}

	public String[] getRewardConds() {
		return rewardConds;
	}

	public void setRewardConds(String[] rewardConds) {
		this.rewardConds = rewardConds;
	}

	public String getRewardChoserId() {
		return rewardChoserId;
	}

	public void setRewardChoserId(String rewardChoserId) {
		this.rewardChoserId = rewardChoserId;
	}

	public String getRewardMailTitleChoserId() {
		return rewardMailTitleChoserId;
	}

	public void setRewardMailTitleChoserId(String rewardMailTitleChoserId) {
		this.rewardMailTitleChoserId = rewardMailTitleChoserId;
	}

	public String getRewardMailContentChoserId() {
		return rewardMailContentChoserId;
	}

	public void setRewardMailContentChoserId(String rewardMailContentChoserId) {
		this.rewardMailContentChoserId = rewardMailContentChoserId;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getAttackIl18nId() {
		return attackIl18nId;
	}

	public void setAttackIl18nId(String attackIl18nId) {
		this.attackIl18nId = attackIl18nId;
	}

	public int getAttackChannel() {
		return attackChannel;
	}

	public void setAttackChannel(int attackChannel) {
		this.attackChannel = attackChannel;
	}

}
