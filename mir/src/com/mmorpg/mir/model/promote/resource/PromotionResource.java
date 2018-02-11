package com.mmorpg.mir.model.promote.resource;

import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class PromotionResource {

	@Id
	private int id;

	/** 任务的ID集合 */
	private String[] questIds;

	private Stat[] warriorStats;
	private Stat[] archerStats;
	private Stat[] strategistStats;
	
	/** 公告职业的 */
	private Map<String, String> job;
	
	/** 广播I18N ID */
	private String broadcastId;
	/** 广播I18N 频道 */
	private int realmOfbroadcast;
	/** tv广播I18N ID */
	private String tvId;
	/** tv广播I18N 频道 */
	private int realmOfTV;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String[] getQuestIds() {
		return questIds;
	}

	public void setQuestIds(String[] questIds) {
		this.questIds = questIds;
	}
	
	@JsonIgnore
	public Stat[] getRoleStats(int role) {
		switch (role) {
			case 1:
				return warriorStats;
			case 2:
				return archerStats;
			case 3:
				return strategistStats;
			default:
				break;
		}
		return null;
	}

	public Stat[] getWarriorStats() {
		return warriorStats;
	}

	public void setWarriorStats(Stat[] warriorStats) {
		this.warriorStats = warriorStats;
	}

	public Stat[] getArcherStats() {
		return archerStats;
	}

	public void setArcherStats(Stat[] archerStats) {
		this.archerStats = archerStats;
	}

	public Stat[] getStrategistStats() {
		return strategistStats;
	}

	public void setStrategistStats(Stat[] strategistStats) {
		this.strategistStats = strategistStats;
	}

	public String getBroadcastId() {
		return broadcastId;
	}

	public void setBroadcastId(String broadcastId) {
		this.broadcastId = broadcastId;
	}

	public int getRealmOfbroadcast() {
		return realmOfbroadcast;
	}

	public void setRealmOfbroadcast(int realmOfbroadcast) {
		this.realmOfbroadcast = realmOfbroadcast;
	}

	public String getTvId() {
		return tvId;
	}

	public void setTvId(String tvId) {
		this.tvId = tvId;
	}

	public int getRealmOfTV() {
		return realmOfTV;
	}

	public void setRealmOfTV(int realmOfTV) {
		this.realmOfTV = realmOfTV;
	}

	public Map<String, String> getJob() {
		return job;
	}

	public void setJob(Map<String, String> job) {
		this.job = job;
	}

}
