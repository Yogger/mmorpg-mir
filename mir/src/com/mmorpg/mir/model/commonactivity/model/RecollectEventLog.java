package com.mmorpg.mir.model.commonactivity.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.commonactivity.CommonActivityConfig;
import com.mmorpg.mir.model.commonactivity.resource.CommonRecollectResource;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.moduleopen.manager.ModuleOpenManager;
import com.mmorpg.mir.model.rank.model.DayKey;
import com.windforce.common.utility.DateUtils;

public class RecollectEventLog {
	/** 已经找回的日期 */
	private long dateIter;
	private Map<Long, Integer> doneStats;
	private Map<Long, Integer> clawbackStats;
	
	private int type;

	@JsonIgnore
	public static RecollectEventLog valueOf(Player player, RecollectType type) {
		RecollectEventLog el = new RecollectEventLog();
		el.doneStats = new HashMap<Long, Integer>();
		el.clawbackStats = new HashMap<Long, Integer>();
		el.type = type.getValue();
		return el;
	}
	
	@JsonIgnore
	public boolean hasRecollected() {
		if (DateUtils.calcIntervalDays(new Date(dateIter), new Date()) <= 2) {
			return true;
		}
		return false;
	}
	
	@JsonIgnore
	public int getCanRecolllectCount(Player player) {
		long startIter = 0;
		long startKey = CommonActivityConfig.getInstance().currentCommonRecollectStartDayKey(RecollectType.valueOf(type));
		long endKey = CommonActivityConfig.getInstance().currentCommonRecollectEndDayKey(RecollectType.valueOf(type));
		if (dateIter < startKey) {
			startIter = startKey;
		} else {
			startIter = dateIter + DateUtils.MILLIS_PER_DAY;
		}
		long endIter = DayKey.valueOf(System.currentTimeMillis() - 2 * DateUtils.MILLIS_PER_DAY).getLunchTime();
		if (endKey < endIter) {
			endIter = endKey;
		}
		int count = 0;
		CommonRecollectResource resource = CommonActivityConfig.getInstance().getCurrentCommonRecollectResource(RecollectType.valueOf(type));
		while (startIter <= endIter) {
			if (ModuleOpenManager.getInstance().isOpenByKeyAtThatDay(player, resource.getOpmkId(), startIter)
					&& !clawbackStats.containsKey(startIter)) {
				int maxCount = resource.getMaxCount(player, startIter);
				Integer doneCount = doneStats.get(startIter);
				if (doneCount == null) {
					count += maxCount;
				} else {
					int v = maxCount - doneCount;
					if (v > 0) {
						count += v;
					}
				}
			}
			startIter += DateUtils.MILLIS_PER_DAY;
		}
		return count;
	}
	
	@JsonIgnore
	public void recollectAll() {
		dateIter = DayKey.valueOf(System.currentTimeMillis() - 2 * DateUtils.MILLIS_PER_DAY).getLunchTime();
	}

	public long getDateIter() {
		return dateIter;
	}

	public void setDateIter(long dateIter) {
		this.dateIter = dateIter;
	}

	public Map<Long, Integer> getDoneStats() {
		return doneStats;
	}

	public void setDoneStats(Map<Long, Integer> doneStats) {
		this.doneStats = doneStats;
	}

	public Map<Long, Integer> getClawbackStats() {
		return clawbackStats;
	}

	public void setClawbackStats(Map<Long, Integer> clawbackStats) {
		this.clawbackStats = clawbackStats;
	}

	@JsonIgnore
	public void addFinishedCount(Long key, int count) {
		Integer logCount = doneStats.get(key);
		if (logCount == null) {
			doneStats.put(key, count);
		} else {
			doneStats.put(key, count  + logCount);
		}
	}
	
	@JsonIgnore
	public void addClawbackCount(int count, Long key) {
		Integer logCount = clawbackStats.get(key);
		if (logCount == null) {
			clawbackStats.put(key, count);
		} else {
			clawbackStats.put(key, count  + logCount);
		}
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
