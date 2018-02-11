package com.mmorpg.mir.model.military.resource;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class MilitaryLevelResource {

	/** 军衔 */
	@Id
	private int rank;
	/** 军衔属性加成 */
	private Stat[] militaryStats;
	/** 国战PVP时军衔属性加成 */
	private Stat[] warStats;
	/** 国战PVP时军衔奖励的加成 */
	private String[] warReward;
	/** 获得荣誉值的条件 */
	private String[] honorConditionId;
	/** 进阶需要完成任务的id */
	private String qiQuestId;
	private String chuQuestId;
	private String zhaoQuestId;

	private String nameI18n;
	private String noticeI18nId;
	private int channelId;
	/** 熔炼额外加成(万分比) */
	private int smeltAddition;

	@JsonIgnore
	public boolean completeTheQuest(int countryValue, String questId) {
		boolean isComplete = false;
		switch (countryValue) {
		case 1:
			isComplete = qiQuestId.equals(questId);
			break;
		case 2:
			isComplete = chuQuestId.equals(questId);
			break;
		case 3:
			isComplete = zhaoQuestId.equals(questId);
			break;
		}
		return isComplete;
	}

	@Transient
	private CoreConditions honorConditions;

	@JsonIgnore
	public CoreConditions getHonorConditions() {
		if (honorConditions == null) {
			if (honorConditionId == null) {
				honorConditions = new CoreConditions();
			} else {
				honorConditions = CoreConditionManager.getInstance().getCoreConditions(1, honorConditionId);
			}
		}
		return honorConditions;
	}

	public String[] getWarReward() {
		return warReward;
	}

	public void setWarReward(String[] warReward) {
		this.warReward = warReward;
	}

	public String[] getHonorConditionId() {
		return honorConditionId;
	}

	public void setHonorConditionId(String[] honorConditionId) {
		this.honorConditionId = honorConditionId;
	}

	public Stat[] getMilitaryStats() {
		return militaryStats;
	}

	public void setMilitaryStats(Stat[] militaryStats) {
		this.militaryStats = militaryStats;
	}

	public Stat[] getWarStats() {
		return warStats;
	}

	public void setWarStats(Stat[] warStats) {
		this.warStats = warStats;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getQiQuestId() {
		return qiQuestId;
	}

	public void setQiQuestId(String qiQuestId) {
		this.qiQuestId = qiQuestId;
	}

	public String getChuQuestId() {
		return chuQuestId;
	}

	public void setChuQuestId(String chuQuestId) {
		this.chuQuestId = chuQuestId;
	}

	public String getZhaoQuestId() {
		return zhaoQuestId;
	}

	public void setZhaoQuestId(String zhaoQuestId) {
		this.zhaoQuestId = zhaoQuestId;
	}

	public String getNoticeI18nId() {
		return noticeI18nId;
	}

	public void setNoticeI18nId(String noticeI18nId) {
		this.noticeI18nId = noticeI18nId;
	}

	public int getChannelId() {
		return channelId;
	}

	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}

	public String getNameI18n() {
		return nameI18n;
	}

	public void setNameI18n(String nameI18n) {
		this.nameI18n = nameI18n;
	}

	public int getSmeltAddition() {
		return smeltAddition;
	}

	public void setSmeltAddition(int smeltAddition) {
		this.smeltAddition = smeltAddition;
	}

}
