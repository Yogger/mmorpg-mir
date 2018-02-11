package com.mmorpg.mir.model.horse.resource;

import java.util.Map;

import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class HorseGrowItemResource {
	@Id
	private String id;

	private Stat[] stat;

	private Map<String, Integer> itemCountLimit;

	public String getId() {
		return id;
	}

	public Stat[] getStat() {
		return stat;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setStat(Stat[] stat) {
		this.stat = stat;
	}

	public Map<String, Integer> getItemCountLimit() {
		return itemCountLimit;
	}

	public void setItemCountLimit(Map<String, Integer> itemCountLimit) {
		this.itemCountLimit = itemCountLimit;
	}
}
