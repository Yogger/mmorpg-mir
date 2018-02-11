package com.mmorpg.mir.model.item.resource;

import java.util.ArrayList;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class EquipmentSmeltResource {
	@Id
	private int level;
	
	private int nextLevel;
	
	private int levelMax;
	
	private Stat[] stats;
	
	private Stat[] qualityStats;

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getNextLevel() {
		return nextLevel;
	}

	public void setNextLevel(int nextLevel) {
		this.nextLevel = nextLevel;
	}

	public int getLevelMax() {
		return levelMax;
	}

	public void setLevelMax(int levelMax) {
		this.levelMax = levelMax;
	}

	public Stat[] getStats() {
		return stats;
	}

	public void setStats(Stat[] stats) {
		this.stats = stats;
	}

	public Stat[] getQualityStats() {
		return qualityStats;
	}

	public void setQualityStats(Stat[] qualityStats) {
		this.qualityStats = qualityStats;
	}
	
	@Transient
	private ArrayList<Stat> allStats;
	
	@JsonIgnore
	public ArrayList<Stat> getAllStats() {
		if (allStats == null || allStats.isEmpty()) {
			ArrayList<Stat> tStats = new ArrayList<Stat>();
			if (stats != null) {
				for (Stat s: stats) {
					tStats.add(s);
				}
			}
			if (qualityStats != null) {
				for (Stat s: qualityStats) {
					tStats.add(s);
				}
			}
			allStats = tStats;
		}
		return allStats;
	}

}
