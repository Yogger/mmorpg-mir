package com.mmorpg.mir.model.item.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.item.AbstractItem;
import com.mmorpg.mir.model.item.core.ItemManager;
import com.mmorpg.mir.model.item.model.TreasureType;

/**
 * 探宝仓库
 * 
 * @author 37.com
 * 
 */
public class TreasureItemStorage extends ItemStorage {
	/** 收集记录 */
	private LinkedList<AbstractItem> collectRecord;

	/** 探宝次数 */
	private HashMap<Integer, Integer> treasureCount;

	public static TreasureItemStorage valueOf(int size, int maxSize) {
		TreasureItemStorage result = new TreasureItemStorage(size, maxSize);
		return result;
	}

	public TreasureItemStorage() {
		super();
		collectRecord = new LinkedList<AbstractItem>();
		treasureCount = new HashMap<Integer, Integer>();
	}

	public TreasureItemStorage(int size, int maxSize) {
		super(size);
		setMaxSize(maxSize);
		collectRecord = new LinkedList<AbstractItem>();
		treasureCount = new HashMap<Integer, Integer>();
	}

	@JsonIgnore
	public void addCount(TreasureType type, int v) {
		if (!treasureCount.containsKey(type.getValue())) {
			treasureCount.put(type.getValue(), 0);
		}
		treasureCount.put(type.getValue(), treasureCount.get(type.getValue()) + v);
	}

	@JsonIgnore
	public void addCollectRecord(AbstractItem item) {
		collectRecord.addFirst(item.copy());
		int count = ItemManager.getInstance().TREASURE_STORAGE_COLLECT_RECORD_COUNT.getValue();
		if (collectRecord.size() <= 2 * count) {
			return;
		}
		ArrayList<AbstractItem> list = new ArrayList<AbstractItem>(collectRecord.subList(0, count));
		ArrayList<AbstractItem> tempRecord = new ArrayList<AbstractItem>(collectRecord);
		for (AbstractItem itemTemp : tempRecord) {
			if (!list.contains(itemTemp)) {
				collectRecord.remove(itemTemp);
			}
		}
	}

	public LinkedList<AbstractItem> getCollectRecord() {
		return collectRecord;
	}

	public void setCollectRecord(LinkedList<AbstractItem> collectRecord) {
		this.collectRecord = collectRecord;
	}

	public HashMap<Integer, Integer> getTreasureCount() {
		return treasureCount;
	}

	public void setTreasureCount(HashMap<Integer, Integer> treasureCount) {
		this.treasureCount = treasureCount;
	}

}
