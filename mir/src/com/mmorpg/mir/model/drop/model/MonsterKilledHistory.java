package com.mmorpg.mir.model.drop.model;

import java.util.ArrayList;
import java.util.Date;

import org.cliffc.high_scale_lib.NonBlockingHashMap;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.h2.util.New;

import com.mmorpg.mir.model.item.core.ItemManager;
import com.windforce.common.utility.DateUtils;

public class MonsterKilledHistory {
	private String objectKey;
	private int killedCount;
	/** 掉落记录 */
	private NonBlockingHashMap<String, Integer> dropHistory = new NonBlockingHashMap<String, Integer>();

	/** 掉落上一次清除的时间记录 */
	private NonBlockingHashMap<String, Long> dropHistoryClear = new NonBlockingHashMap<String, Long>();

	public static MonsterKilledHistory valueOf(String objectKey) {
		MonsterKilledHistory mkh = new MonsterKilledHistory();
		mkh.objectKey = objectKey;
		return mkh;
	}

	@JsonIgnore
	public void refreshDropHistory(ItemManager itemManager) {
		ArrayList<String> removeKeys = New.arrayList();
		for (String itemKey : dropHistory.keySet()) {
			Long time = dropHistoryClear.get(itemKey);
			if (time != null
					&& DateUtils.calcIntervalDays(new Date(time), new Date()) >= itemManager.getResource(itemKey)
							.getMonsterClearDayInterval()) {
				removeKeys.add(itemKey);
			} else if (time == null) {
				removeKeys.add(itemKey);
			}
		}

		for (String removeKey : removeKeys) {
			dropHistoryClear.put(removeKey, System.currentTimeMillis());
			dropHistory.remove(removeKey);
		}
	}

	public void putItem(String itemId, int count) {
		if (dropHistory.containsKey(itemId)) {
			dropHistory.put(itemId, dropHistory.get(itemId) + count);
		} else {
			dropHistory.put(itemId, count);
		}
		dropHistoryClear.putIfAbsent(itemId, System.currentTimeMillis());
	}

	@JsonIgnore
	public int getItemCount(String itemId) {
		if (dropHistory.containsKey(itemId)) {
			return dropHistory.get(itemId);
		} else {
			return 0;
		}
	}

	public void addKilledCount() {
		killedCount++;
	}

	public void clearKilledCount() {
		killedCount = 0;
	}

	public int getKilledCount() {
		return killedCount;
	}

	public void setKilledCount(int killedCount) {
		this.killedCount = killedCount;
	}

	public NonBlockingHashMap<String, Integer> getDropHistory() {
		return dropHistory;
	}

	public void setDropHistory(NonBlockingHashMap<String, Integer> dropHistory) {
		this.dropHistory = dropHistory;
	}

	public String getObjectKey() {
		return objectKey;
	}

	public void setObjectKey(String objectKey) {
		this.objectKey = objectKey;
	}

	public NonBlockingHashMap<String, Long> getDropHistoryClear() {
		return dropHistoryClear;
	}

	public void setDropHistoryClear(NonBlockingHashMap<String, Long> dropHistoryClear) {
		this.dropHistoryClear = dropHistoryClear;
	}

}
