package com.mmorpg.mir.model.footprint.resource;

import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class FootprintResource {
	@Id
	private int id;
	/** 激活属性 */
	private Stat[][] foreverStats;
	/** 装备属性 */
	private Stat[] openStats;
	///** 最大激活时间 */
	//private int maxOpenTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Stat[][] getForeverStats() {
		return foreverStats;
	}

	public void setForeverStats(Stat[][] foreverStats) {
		this.foreverStats = foreverStats;
	}

	public Stat[] getOpenStats() {
		return openStats;
	}

	public void setOpenStats(Stat[] openStats) {
		this.openStats = openStats;
	}

}
