package com.mmorpg.mir.model.item.storage;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.cliffc.high_scale_lib.NonBlockingHashMap;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.item.core.ItemManager;
import com.windforce.common.utility.DateUtils;

public class PlayerItemStorage extends ItemStorage {

	// for store item use limit
	private transient NonBlockingHashMap<String, Integer> itemDailyLimit;
	
	private transient long lastRefreshTime;
	
	private transient Map<String, Integer> totalLimit;
	
	/** 装备强化幸运值 */
	private Map<String, Integer> enhanceLuckPoints;
	/** 装备强化幸运值的有效时间戳 */
	private Map<String, Long> enhanceLuckTime;

	public PlayerItemStorage() {
		super();
		itemDailyLimit = new NonBlockingHashMap<String, Integer>();
		totalLimit = new HashMap<String, Integer>();
		enhanceLuckPoints = new HashMap<String, Integer>();
		enhanceLuckTime = new HashMap<String, Long>();
	}

	public PlayerItemStorage(int size) {
		super(size);
		itemDailyLimit = new NonBlockingHashMap<String, Integer>();
		totalLimit = new HashMap<String, Integer>();
		enhanceLuckPoints = new HashMap<String, Integer>();
		enhanceLuckTime = new HashMap<String, Long>();
	}

	public static PlayerItemStorage valueOf(int size, int max) {
		PlayerItemStorage storage = new PlayerItemStorage(size);
		storage.setMaxSize(max);
		return storage;
	}

	@JsonIgnore
	public boolean itemNotOverLimit(String itemKey, int itemTotalLimit, int num) {
		if (num > itemTotalLimit)
			return false;
		if (!totalLimit.containsKey(itemKey)) {
			totalLimit.put(itemKey, num);
		} else {
			if (totalLimit.get(itemKey) + num >= itemTotalLimit)
				return false;
			else
				totalLimit.put(itemKey, totalLimit.get(itemKey) + num);
		}
		return true;
	}
	
	@JsonIgnore
	public void cosumeItems(Player player, String code, int value) {
		Integer count = itemDailyLimit.get(code);
		itemDailyLimit.put(code, count == null? value: count + value);
		Integer totalCount = totalLimit.get(code);
		totalLimit.put(code, totalCount == null? value : totalCount + value);
	}
	
	@JsonIgnore
	public void refresh() {
		if (!DateUtils.isToday(new Date(lastRefreshTime))) {
			itemDailyLimit.clear();
			lastRefreshTime = System.currentTimeMillis();
		}
	}

	public Map<String, Integer> getTotalLimit() {
		return totalLimit;
	}

	public void setTotalLimit(Map<String, Integer> totalLimit) {
		this.totalLimit = totalLimit;
	}

	public Map<String, Integer> getEnhanceLuckPoints() {
		return enhanceLuckPoints;
	}

	public void setEnhanceLuckPoints(Map<String, Integer> enhanceLuckPoints) {
		this.enhanceLuckPoints = enhanceLuckPoints;
	}

	public Map<String, Long> getEnhanceLuckTime() {
		return enhanceLuckTime;
	}

	public void setEnhanceLuckTime(Map<String, Long> enhanceLuckTime) {
		this.enhanceLuckTime = enhanceLuckTime;
	}

	@Override
	public void initOpenPackHpStat(Player player, boolean recompute) {
		player.getGameStats().addModifiers(ItemManager.OPEN_PACK, Arrays.asList(hpStat), recompute);
		refresh();
	}

	public long getLastRefreshTime() {
		return lastRefreshTime;
	}

	public void setLastRefreshTime(long lastRefreshTime) {
		this.lastRefreshTime = lastRefreshTime;
	}

	public NonBlockingHashMap<String, Integer> getItemDailyLimit() {
		return itemDailyLimit;
	}

	public void setItemDailyLimit(NonBlockingHashMap<String, Integer> itemDailyLimit) {
		this.itemDailyLimit = itemDailyLimit;
	}
	
	@JsonIgnore
	public int getTodayUseSituation(String[] items) {
		int total = 0;
		for (Entry<String, Integer> entry : itemDailyLimit.entrySet()) {
			for (String s : items) {
				if (s.equals(entry.getKey()))
					total += entry.getValue();
			}
		}
		return total;
	}
	
}