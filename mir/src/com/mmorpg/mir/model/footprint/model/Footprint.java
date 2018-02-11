package com.mmorpg.mir.model.footprint.model;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.footprint.config.FootprintConfig;
import com.mmorpg.mir.model.footprint.resource.FootprintResource;

public class Footprint {
	private int id;
	private long endTime;
	private int star;

	public static Footprint valueOf(int id, long endTime) {
		Footprint fp = new Footprint();
		fp.id = id;
		fp.endTime = endTime;
		return fp;
	}

	@JsonIgnore
	public boolean isDeprecated() {
		return endTime <= System.currentTimeMillis();
	}

	@JsonIgnore
	public void increase(long time) {
		increase(time, 1);
	}
	
	@JsonIgnore
	public void increase(long time, int addStar) {
		if (endTime < System.currentTimeMillis()) {
			endTime = time + System.currentTimeMillis();
		} else {
			endTime += time;
		}

	/*	if ((endTime - System.currentTimeMillis()) > getResource().getMaxOpenTime() * DateUtils.MILLIS_PER_MINUTE) {
			endTime = getResource().getMaxOpenTime() * DateUtils.MILLIS_PER_MINUTE + System.currentTimeMillis();
		}*/

		if ((star + addStar) < FootprintConfig.getInstance().MAX_ACTIVATE_COUNT.getValue()) {
			star += (addStar);
		} else {
			star = FootprintConfig.getInstance().MAX_ACTIVATE_COUNT.getValue() - 1;
		}
	}
	
	@JsonIgnore
	public FootprintResource getResource() {
		return FootprintConfig.getInstance().getResource(id);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public int getStar() {
		return star;
	}

	public void setStar(int star) {
		this.star = star;
	}

}
