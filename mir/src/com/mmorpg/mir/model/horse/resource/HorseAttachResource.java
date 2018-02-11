package com.mmorpg.mir.model.horse.resource;

import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class HorseAttachResource {
	// 标识
	@Id
	private int id;
	// 附加属性
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
