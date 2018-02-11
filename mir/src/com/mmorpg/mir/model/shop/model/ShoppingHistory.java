package com.mmorpg.mir.model.shop.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.shop.manager.ShopManager;
import com.windforce.common.utility.DateUtils;
import com.windforce.common.utility.New;

/**
 * 购买记录
 * 
 * @author Kuang Hao
 * @since v1.0 2013-5-13
 * 
 */
public class ShoppingHistory {
	private Map<String, Integer> history;

	private Map<String, Integer> totalHistory;

	/** 最后刷新时间 */
	private long lastRefreshTime;
	public static final String SHOPPING_COUNT = "SHOPPING_COUNT";

	public static ShoppingHistory valueOf() {
		ShoppingHistory sh = new ShoppingHistory();
		sh.setHistory(new HashMap<String, Integer>());
		sh.setTotalHistory(new HashMap<String, Integer>());
		return sh;
	}

	@JsonIgnore
	public void refresh() {
		if (!DateUtils.isToday(new Date(lastRefreshTime))) {
			history.clear();
			lastRefreshTime = System.currentTimeMillis();
		}
	}
	
	@JsonIgnore
	public void refreshSpecialType() {
		refresh();
		Map<String, Integer> hisMap = new HashMap<String, Integer>(history);
		ArrayList<String> removeIds = New.arrayList();
		for (Entry<String, Integer> entry : hisMap.entrySet()) {
			if (ShopManager.getInstance().getShopResource(entry.getKey()).isSpecialType()) {
				removeIds.add(entry.getKey());
			}
		}
		for (String id : removeIds) {
			hisMap.remove(id);
		}
		history = hisMap;
	}

	/**
	 * 获取数量
	 * 
	 * @param sampleId
	 * @return
	 */
	@JsonIgnore
	public int getCount(String sampleId) {
		if (!history.containsKey(sampleId)) {
			return 0;
		} else {
			return history.get(sampleId);
		}
	}

	/**
	 * 获取数量
	 * 
	 * @param sampleId
	 * @return
	 */
	@JsonIgnore
	public int getTotalCount(String sampleId) {
		if (!totalHistory.containsKey(sampleId)) {
			return 0;
		} else {
			return totalHistory.get(sampleId);
		}
	}

	/**
	 * 添加记录
	 * 
	 * @param sampleId
	 */
	@JsonIgnore
	public void addHistory(String sampleId, int count) {
		if (history.containsKey(sampleId)) {
			history.put(sampleId, history.get(sampleId) + count);
		} else {
			history.put(sampleId, count);
		}
		this.addTotalHistory(sampleId, count);
	}

	/**
	 * 添加记录
	 * 
	 * @param sampleId
	 */
	@JsonIgnore
	public void addTotalHistory(String sampleId, int count) {
		if (totalHistory.containsKey(sampleId)) {
			totalHistory.put(sampleId, totalHistory.get(sampleId) + count);
		} else {
			totalHistory.put(sampleId, count);
		}
	}

	public Map<String, Integer> getHistory() {
		return history;
	}

	public void setHistory(Map<String, Integer> history) {
		this.history = history;
	}

	public long getLastRefreshTime() {
		return lastRefreshTime;
	}

	public void setLastRefreshTime(long lastRefreshTime) {
		this.lastRefreshTime = lastRefreshTime;
	}

	public Map<String, Integer> getTotalHistory() {
		return totalHistory;
	}

	public void setTotalHistory(Map<String, Integer> totalHistory) {
		this.totalHistory = totalHistory;
	}

}
