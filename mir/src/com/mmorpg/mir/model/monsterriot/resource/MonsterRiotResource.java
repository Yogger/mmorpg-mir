package com.mmorpg.mir.model.monsterriot.resource;

import java.util.Map;

import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Index;
import com.windforce.common.resource.anno.Resource;

@Resource
public class MonsterRiotResource {

	public static final String COUNTRY_INDEX = "country";
	@Id
	private String id;
	/** 刷新的怪物 */
	private Map<Integer, String[]> spawnIdMaps;
	/** 开始时间的cron表达式 */
	private String startCron;
	/** 开始时间的cron表达式 */
	private String deleteCron;
	/** 第几波怪 */
	private int round;
	/** 这波怪的总共的数量 */
	private int monsterCount;
	/** 所属国家 */
	@Index(name = COUNTRY_INDEX)
	private Integer country;
	/** 刷新的时候的广播 */
	private Map<String, Integer> i18nNotice;
	/** 每波对应档次的奖励 */
	private String[] rankChooserGroup;

	public String getId() {
		return id;
	}

	public Map<Integer, String[]> getSpawnIdMaps() {
		return spawnIdMaps;
	}

	public void setSpawnIdMaps(Map<Integer, String[]> spawnIdMaps) {
		this.spawnIdMaps = spawnIdMaps;
	}

	public String getStartCron() {
		return startCron;
	}

	public Integer getCountry() {
		return country;
	}

	public Map<String, Integer> getI18nNotice() {
		return i18nNotice;
	}

	public int getRound() {
		return round;
	}

	public String getDeleteCron() {
		return deleteCron;
	}

	public String[] getRankChooserGroup() {
		return rankChooserGroup;
	}

	public int getMonsterCount() {
		return monsterCount;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setStartCron(String startCron) {
		this.startCron = startCron;
	}

	public void setDeleteCron(String deleteCron) {
		this.deleteCron = deleteCron;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public void setMonsterCount(int monsterCount) {
		this.monsterCount = monsterCount;
	}

	public void setCountry(Integer country) {
		this.country = country;
	}

	public void setI18nNotice(Map<String, Integer> i18nNotice) {
		this.i18nNotice = i18nNotice;
	}

	public void setRankChooserGroup(String[] rankChooserGroup) {
		this.rankChooserGroup = rankChooserGroup;
	}

}
