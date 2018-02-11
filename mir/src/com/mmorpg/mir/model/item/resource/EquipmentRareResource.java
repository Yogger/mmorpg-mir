package com.mmorpg.mir.model.item.resource;

import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

/**
 * 稀有属性
 * 
 * @author Kuang Hao
 * @since v1.0 2014-10-13
 * 
 */
@Resource
public class EquipmentRareResource {
	@Id
	private String id;
	private Stat[] stats;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Stat[] getStats() {
		return stats;
	}

	public void setStats(Stat[] stats) {
		this.stats = stats;
	}

}
