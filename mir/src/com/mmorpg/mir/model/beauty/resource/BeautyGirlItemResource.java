package com.mmorpg.mir.model.beauty.resource;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class BeautyGirlItemResource {

	@Id
	private String id;

	private Stat[] stat;

	private Range[] limitCount;

	@JsonIgnore
	public int getMaxCount(int sumLevel) {
		for (Range r : limitCount) {
			if (r.getMin() <= sumLevel && sumLevel < r.getMax()) {
				return r.getValue();
			}
		}
		throw new RuntimeException("策划配错表");
	}

	public String getId() {
		return id;
	}

	public Stat[] getStat() {
		return stat;
	}

	public void setStat(Stat[] stat) {
		this.stat = stat;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Range[] getLimitCount() {
		return limitCount;
	}

	public void setLimitCount(Range[] limitCount) {
		this.limitCount = limitCount;
	}
	
	

}
