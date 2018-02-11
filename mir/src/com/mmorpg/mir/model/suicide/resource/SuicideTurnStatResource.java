package com.mmorpg.mir.model.suicide.resource;

import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class SuicideTurnStatResource {

	@Id
	private int id;

	private Stat[] stats;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Stat[] getStats() {
		return stats;
	}

	public void setStats(Stat[] stats) {
		this.stats = stats;
	}

}
