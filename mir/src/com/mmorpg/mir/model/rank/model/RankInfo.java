package com.mmorpg.mir.model.rank.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.h2.util.New;

import com.mmorpg.mir.model.gameobjects.Player;

public class RankInfo {

	/** 击杀的历史记录 */
	private Map<Long, Set<Long>> slaughterHistory;
	
	private int layer;
	private int cost = Integer.MAX_VALUE;
	
	@JsonIgnore
	public void refresh() {
		long now = DayKey.valueOf().getLunchTime();// ;System.currentTimeMillis()
		List<Long> keys = New.arrayList();
		for (Long time : slaughterHistory.keySet()) {
			if (now - time > 3 * org.apache.commons.lang.time.DateUtils.MILLIS_PER_DAY) {
				keys.add(time);
			}
		}
		for (Long id: keys) {
			slaughterHistory.remove(id);
		}
	}

	public static RankInfo valueOf(Player player) {
		RankInfo info = new RankInfo();
		info.slaughterHistory = new HashMap<Long, Set<Long>>();
		return info;
	}

	public Map<Long, Set<Long>> getSlaughterHistory() {
    	return slaughterHistory;
    }

	public void setSlaughterHistory(Map<Long, Set<Long>> slaughterHistory) {
    	this.slaughterHistory = slaughterHistory;
    }

	public int getLayer() {
    	return layer;
    }

	public void setLayer(int layer) {
    	this.layer = layer;
    }

	public int getCost() {
    	return cost;
    }

	public void setCost(int cost) {
    	this.cost = cost;
    }

}
