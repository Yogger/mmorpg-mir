package com.mmorpg.mir.model.fashion.resource;

import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class FashionLevelResource {

	@Id
	private int id;

	private Stat[] stats;

	/** 升级到下一等级要扣除的经验值 */
	private int needExp;

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

	public int getNeedExp() {
		return needExp;
	}

	public void setNeedExp(int needExp) {
		this.needExp = needExp;
	}

}
