package com.mmorpg.mir.model.warship.model;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.windforce.common.utility.DateUtils;

public class Warship {

	private int warshipCount;

	private long lastRefreshTime;

	private int currentSelect;

	private int totalWarshipCount;

	@JsonIgnore
	public void refresh() {
		if (!DateUtils.isToday(new Date(lastRefreshTime))) {
			currentSelect = 0;
			warshipCount = 0;
			lastRefreshTime = System.currentTimeMillis();
		}
	}

	public int getCurrentSelect() {
		return currentSelect;
	}

	public void setCurrentSelect(int currentSelect) {
		this.currentSelect = currentSelect;
	}

	public int getWarshipCount() {
		return warshipCount;
	}

	public void setWarshipCount(int warshipCount) {
		this.warshipCount = warshipCount;
	}

	@JsonIgnore
	public int warshipConsume() {
		this.warshipCount++;
		this.totalWarshipCount++;
		return warshipCount;
	}

	public long getLastRefreshTime() {
		return lastRefreshTime;
	}

	public void setLastRefreshTime(long lastRefreshTime) {
		this.lastRefreshTime = lastRefreshTime;
	}

	public int getTotalWarshipCount() {
		return totalWarshipCount;
	}

	public void setTotalWarshipCount(int totalWarshipCount) {
		this.totalWarshipCount = totalWarshipCount;
	}

}
