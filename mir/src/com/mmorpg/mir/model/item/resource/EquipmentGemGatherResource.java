package com.mmorpg.mir.model.item.resource;

import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class EquipmentGemGatherResource {

	@Id
	private String id;

	private int levelSum;

	private Stat[] stats;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getLevelSum() {
		return levelSum;
	}

	public void setLevelSum(int levelSum) {
		this.levelSum = levelSum;
	}

	public Stat[] getStats() {
		return stats;
	}

	public void setStats(Stat[] stats) {
		this.stats = stats;
	}

}
