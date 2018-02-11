package com.mmorpg.mir.model.lifegrid.model;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.item.AbstractItem;
import com.mmorpg.mir.model.item.LifeGridItem;
import com.mmorpg.mir.model.item.NullItem;
import com.mmorpg.mir.model.item.AbstractItem.AddResult;

public class LifeGridStorage {
	private int lastOpenLevel;
	private static int DEFAULT_SIZE = 200;
	private int size;
	private LifeGridItem[] items;
	@JsonIgnore
	private transient BitSet mark;

	public LifeGridStorage() {
		this(DEFAULT_SIZE);
	}

	public LifeGridStorage(int size) {
		this.items = new LifeGridItem[size];
		this.size = size;
		mark = new BitSet(size);
	}

	public static LifeGridStorage valueOf(int size) {
		LifeGridStorage result = new LifeGridStorage(size);
		return result;
	}

	@JsonIgnore
	public int getTotalSize() {
		return items.length;
	}

	@JsonIgnore
	public int getCurrentSize() {
		int size = 0;
		for (int i = 0, j = items.length; i < j; i++) {
			if (items[i] != null) {
				size++;
			}
		}
		return size;
	}

	@JsonIgnore
	public boolean isFull() {
		return getCurrentSize() >= size;
	}

	@JsonIgnore
	public AbstractItem getItemByKey(String key) {
		for (int i = 0, j = items.length; i < j; i++) {
			if (items[i] != null) {
				if (items[i].getKey().equals(key)) {
					return items[i];
				}
			}
		}
		return null;
	}

	@JsonIgnore
	public AbstractItem getItemByGuid(long guid) {
		for (int i = 0, j = items.length; i < j; i++) {
			if (items[i] != null) {
				if (items[i].getObjectId().longValue() == guid) {
					return items[i];
				}
			}
		}
		return null;
	}

	@JsonIgnore
	public void addItemByIndex(int index, LifeGridItem item) {
		if (index >= 0 && index < items.length) {
			items[index] = item;
			mark.set(index);
		}
	}

	@JsonIgnore
	public LifeGridItem removeItemByIndex(int index) {
		if (index >= 0 && index < items.length) {
			LifeGridItem result = items[index];
			items[index] = null;
			if (result != null) {
				mark.set(index);
			}
			return result;
		}
		return null;
	}

	@JsonIgnore
	public void removeItemByIndex(Set<Integer> indexs) {
		for (int index : indexs) {
			if (index >= 0 && index < items.length) {
				LifeGridItem result = items[index];
				items[index] = null;
				if (result != null) {
					mark.set(index);
				}
			}
		}
	}

	@JsonIgnore
	public int getEmptySize() {
		return size - getCurrentSize();
	}

	@JsonIgnore
	public void addItems(LifeGridItem... items) {
		addItems(true, items);
	}

	@JsonIgnore
	public void addItems(boolean check, LifeGridItem... items) {
		if (items != null) {
			for (LifeGridItem item : items) {
				if (item != null) {
					addItem(item, check);
				}
			}
		}
	}

	@JsonIgnore
	public void addItems(boolean check, Collection<LifeGridItem> items) {
		if (items != null) {
			for (LifeGridItem item : items) {
				if (item != null) {
					addItem(item, check);
				}
			}
		}
	}

	@JsonIgnore
	private void addItem(LifeGridItem item, boolean check) {
		LifeGridItem addItem = item;
		// 首先在已经有的道具上面添加
		if (addItem.getOverLimit() > 1 && check) {
			for (int i = 0, j = items.length; i < j; i++) {
				if (items[i] != null) {
					AddResult result = items[i].add(item);
					if (result.isSuccess()) {
						mark.set(i);
						addItem = (LifeGridItem) result.getResult();
					}
				}
				if (addItem == null) {
					break;
				}
			}
		}
		// 然后在空的格子上添加
		if (addItem != null) {
			for (int i = 0, j = items.length; i < j; i++) {
				if (items[i] == null) {
					items[i] = addItem;
					mark.set(i);
					addItem = null;
					break;
				}
			}
		}
	}

	@JsonIgnore
	public Map<Integer, Object> collectUpdate() {
		Map<Integer, Object> result = new HashMap<Integer, Object>();
		for (int i = 0, j = mark.length(); i < j; i++) {
			if (mark.get(i)) {
				if (items[i] == null) {
					result.put(i, NullItem.getInstance());
				} else {
					result.put(i, items[i]);
				}
			}
			mark.set(i, false);
		}
		return result;
	}

	@JsonIgnore
	public LifeGridItem getItemByIndex(int index) {
		if (index >= 0 && index < items.length) {
			return items[index];
		}
		return null;
	}

	@JsonIgnore
	public void exchage(int fromIndex, int toIndex) {
		if (fromIndex >= 0 && fromIndex < items.length && toIndex >= 0 && toIndex < items.length) {
			LifeGridItem from = items[fromIndex];
			LifeGridItem to = items[toIndex];
			if (from == null) {
				return;
			}
			if (to == null) {
				items[toIndex] = items[fromIndex];
				items[fromIndex] = null;
				mark.set(fromIndex);
				mark.set(toIndex);
				return;
			}
			if (to.getSize() >= to.getOverLimit() || from.getSize() >= from.getOverLimit()) {
				LifeGridItem temp = items[fromIndex];
				items[fromIndex] = items[toIndex];
				items[toIndex] = temp;
				mark.set(fromIndex);
				mark.set(toIndex);
			} else {
				AddResult addResult = to.add(from);
				if (addResult.isSuccess()) {
					items[fromIndex] = (LifeGridItem) addResult.getResult();
					mark.set(fromIndex);
					mark.set(toIndex);
				} else {
					LifeGridItem temp = items[fromIndex];
					items[fromIndex] = items[toIndex];
					items[toIndex] = temp;
					mark.set(fromIndex);
					mark.set(toIndex);
				}
			}
		}
	}

	@JsonIgnore
	public void extendSize(int level, int extendSize) {
		this.lastOpenLevel = level;
		this.size += extendSize;
		items = Arrays.copyOf(items, size);
	}

	@JsonIgnore
	public boolean checkIsExistSameType(int targetLifeGridType) {
		for (LifeGridItem item : items) {
			if (item == null) {
				continue;
			}
			if (item.getLifeGridType() == targetLifeGridType) {
				return true;
			}
		}
		return false;
	}

	@JsonIgnore
	public void refreshDeprecated() {
		for (int i = 0, j = items.length; i < j; i++) {
			if (items[i] != null) {
				if (items[i].hasDeprecated()) {
					items[i].checkDeprecated();
				}
			}
		}
	}

	/**
	 * 
	 * @param key
	 * @param deprecated
	 *            true-包含过期物品
	 * @return
	 */
	@JsonIgnore
	public long getItemSizeByKey(String key, boolean deprecated) {
		refreshDeprecated();
		long size = 0;
		for (int i = 0, j = items.length; i < j; i++) {
			if (items[i] != null) {
				if (items[i].getKey().equals(key) && (deprecated || !items[i].isDeprecated())) {
					size += items[i].getSize();
				}
			}
		}
		return size;
	}

	@JsonIgnore
	public long getItemSizeByKey(String key) {
		return getItemSizeByKey(key, false);
	}

	@JsonIgnore
	public LifeGridItem equip(LifeGridItem item) {
		int index = -1;
		for (int i = 0; i < items.length; i++) {
			if (items[i] == null) {
				index = i;
				break;
			}
		}

		if (index == -1) {
			throw new ManagedException(ManagedErrorCode.LIFE_GRID_EQUIPPACK_NOT_ENOUGH_SIZE);
		}
		items[index] = item;
		mark.set(index);
		return item;
	}

	@JsonIgnore
	public LifeGridItem unEquip(int index) {
		LifeGridItem item = items[index];
		items[index] = null;
		mark.set(index);
		return item;
	}

	/**
	 * 获取吞噬的目标道具
	 * 
	 * @return
	 */
	@JsonIgnore
	public QueryDevourResult getDevourItem() {
		int quality = 0;
		int index = -1;
		LifeGridItem targetItem = null;
		int level = -1;
		int exp = -1;
		for (int i = 0; i < items.length; i++) {
			LifeGridItem item = items[i];
			if (item == null) {
				continue;
			}
			if (item.isSpecialOne() || item.isMaxLevel()) {
				continue;
			}
			if (item.getResource().getQuality() >= quality) {
				if (item.getResource().getQuality() == quality && item.getResource().getLifeGridLevel() < level) {
					continue;
				}

				if (item.getResource().getQuality() == quality && item.getResource().getLifeGridLevel() == level
						&& item.getExp() <= exp) {
					continue;
				}

				quality = item.getResource().getQuality();
				targetItem = item;
				index = i;
				level = item.getResource().getLifeGridLevel();
				exp = item.getExp();
			}
		}

		if (targetItem != null) {
			return new QueryDevourResult(index, targetItem);
		}
		return null;
	}

	/**
	 * 获取被吞噬的命格
	 * 
	 * @param exceptIndex
	 * @return
	 */
	@JsonIgnore
	public Map<Integer, LifeGridItem> getBeDevourItems(int exceptIndex) {
		Map<Integer, LifeGridItem> result = new TreeMap<Integer, LifeGridItem>();
		for (int i = 0; i < items.length; i++) {
			if (i == exceptIndex) {
				continue;
			}
			LifeGridItem item = items[i];
			if (item != null && !item.isLock() && (!item.isMaxLevel() || item.isSpecialOne())) {
				result.put(i, item);
			}

		}
		return result;
	}

	public class QueryDevourResult {
		private int index;
		private LifeGridItem item;

		private QueryDevourResult(int index, LifeGridItem item) {
			this.index = index;
			this.item = item;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		public LifeGridItem getItem() {
			return item;
		}

		public void setItem(LifeGridItem item) {
			this.item = item;
		}

	}

	public int getSize() {
		return size;
	}

	public LifeGridItem[] getItems() {
		return items;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void setItems(LifeGridItem[] items) {
		this.items = items;
	}

	public int getLastOpenLevel() {
		return lastOpenLevel;
	}

	public void setLastOpenLevel(int lastOpenLevel) {
		this.lastOpenLevel = lastOpenLevel;
	}

}
