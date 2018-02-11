package com.mmorpg.mir.model.player.resource;

import java.util.List;

import org.h2.util.New;

import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.mmorpg.mir.model.gameobjects.stats.StatEnum;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;
import com.windforce.common.utility.JsonUtils;

@Resource
public class PlayerLevelResource {
	@Id
	private int level;
	private long exp;
	// 所属转生等级
	private int turnNum;
	private int turnLevel;
	private Stat[] archerStats;
	private Stat[] sorcererStats;
	private Stat[] strategistStats;
	private Stat[] warriorStats;

	/** 坐骑等级提供的属性 */
	private Stat[] horseStats;

	public Stat[] getRoleStat(int role) {
		switch (role) {
		case 1:
			return warriorStats;
		case 2:
			return archerStats;
		case 3:
			return strategistStats;
		case 4:
			return sorcererStats;
		default:
			break;
		}
		return null;
	}

	public static void main(String[] args) {
		List<Stat> stats = New.arrayList();
		for (StatEnum stat : StatEnum.values()) {
			stats.add(new Stat(stat, 100, 0, 0));
		}
		System.out.println(JsonUtils.object2String(stats));
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public long getExp() {
		return exp;
	}

	public void setExp(long exp) {
		this.exp = exp;
	}

	public Stat[] getArcherStats() {
		return archerStats;
	}

	public void setArcherStats(Stat[] archerStats) {
		this.archerStats = archerStats;
	}

	public Stat[] getSorcererStats() {
		return sorcererStats;
	}

	public void setSorcererStats(Stat[] sorcererStats) {
		this.sorcererStats = sorcererStats;
	}

	public Stat[] getStrategistStats() {
		return strategistStats;
	}

	public void setStrategistStats(Stat[] strategistStats) {
		this.strategistStats = strategistStats;
	}

	public Stat[] getWarriorStats() {
		return warriorStats;
	}

	public void setWarriorStats(Stat[] warriorStats) {
		this.warriorStats = warriorStats;
	}

	public Stat[] getHorseStats() {
		return horseStats;
	}

	public void setHorseStats(Stat[] horseStats) {
		this.horseStats = horseStats;
	}

	public int getTurnNum() {
		return turnNum;
	}

	public void setTurnNum(int turnNum) {
		this.turnNum = turnNum;
	}

	public int getTurnLevel() {
		return turnLevel;
	}

	public void setTurnLevel(int turnLevel) {
		this.turnLevel = turnLevel;
	}

}
