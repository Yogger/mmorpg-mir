package com.mmorpg.mir.model.boss.resource;

import java.util.Map;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class BossResource {

	private static final int OUTDOOR_BOSS_GROUP = 4;

	@Id
	private String id;
	/** 刷新信息的chooserGroup */
	private String spawnChooserGroupId;
	/** 刷新时间 */
	private int refreshTime;
	/** BOSS分组 */
	private int group;
	/** 伤害奖励 */
	private String rewardChooserGroupId;
	/** 奖励邮件内容i18n，id */
	private String mailContextChooserGroupId;
	/** 奖励邮件标题i18n，id */
	private String mailTitleChooserGroupId;
	/** 精英 */
	private boolean elite;
	/** 刷新广播 */
	private Map<String, Integer> spawnNotice;
	/** 击杀广播 */
	private Map<String, Integer> killNotice;
	/** 墓碑的ID */
	private String tombstoneSpawnId;
	/** 红包Chooser */
	private String giftChooserGroup;

	private String[] firstKillConditionIds;
	/** 首杀的奖励chooserGroup */
	private String rewardChooserGroup;
	/** 转国后的bossId */
	private String[] mergerBoosIds;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSpawnChooserGroupId() {
		return spawnChooserGroupId;
	}

	public void setSpawnChooserGroupId(String spawnChooserGroupId) {
		this.spawnChooserGroupId = spawnChooserGroupId;
	}

	public int getGroup() {
		return group;
	}

	public void setGroup(int group) {
		this.group = group;
	}

	public String getRewardChooserGroupId() {
		return rewardChooserGroupId;
	}

	public void setRewardChooserGroupId(String rewardChooserGroupId) {
		this.rewardChooserGroupId = rewardChooserGroupId;
	}

	public String getMailTitleChooserGroupId() {
		return mailTitleChooserGroupId;
	}

	public void setMailTitleChooserGroupId(String mailTitleChooserGroupId) {
		this.mailTitleChooserGroupId = mailTitleChooserGroupId;
	}

	public String getMailContextChooserGroupId() {
		return mailContextChooserGroupId;
	}

	public void setMailContextChooserGroupId(String mailContextChooserGroupId) {
		this.mailContextChooserGroupId = mailContextChooserGroupId;
	}

	public int getRefreshTime() {
		return refreshTime;
	}

	public void setRefreshTime(int refreshTime) {
		this.refreshTime = refreshTime;
	}

	public boolean isElite() {
		return elite;
	}

	public void setElite(boolean elite) {
		this.elite = elite;
	}

	public String getTombstoneSpawnId() {
		return tombstoneSpawnId;
	}

	public void setTombstoneSpawnId(String tombstoneSpawnId) {
		this.tombstoneSpawnId = tombstoneSpawnId;
	}

	public String getGiftChooserGroup() {
		return giftChooserGroup;
	}

	public void setGiftChooserGroup(String giftChooserGroup) {
		this.giftChooserGroup = giftChooserGroup;
	}

	public String[] getFirstKillConditionIds() {
		return firstKillConditionIds;
	}

	public void setFirstKillConditionIds(String[] firstKillConditionIds) {
		this.firstKillConditionIds = firstKillConditionIds;
	}

	@Transient
	private CoreConditions fbRewardConditions;

	@JsonIgnore
	public CoreConditions getFBRewardConditions() {
		if (fbRewardConditions == null) {
			fbRewardConditions = CoreConditionManager.getInstance().getCoreConditions(1, firstKillConditionIds);
		}
		return fbRewardConditions;
	}

	public String getRewardChooserGroup() {
		return rewardChooserGroup;
	}

	public void setRewardChooserGroup(String rewardChooserGroup) {
		this.rewardChooserGroup = rewardChooserGroup;
	}

	@JsonIgnore
	public boolean isCountryBoss() {
		return getGroup() < OUTDOOR_BOSS_GROUP;
	}

	public String[] getMergerBoosIds() {
		return mergerBoosIds;
	}

	public void setMergerBoosIds(String[] mergerBoosIds) {
		this.mergerBoosIds = mergerBoosIds;
	}

	public Map<String, Integer> getSpawnNotice() {
		return spawnNotice;
	}

	public void setSpawnNotice(Map<String, Integer> spawnNotice) {
		this.spawnNotice = spawnNotice;
	}

	public Map<String, Integer> getKillNotice() {
		return killNotice;
	}

	public void setKillNotice(Map<String, Integer> killNotice) {
		this.killNotice = killNotice;
	}
	
	

}
