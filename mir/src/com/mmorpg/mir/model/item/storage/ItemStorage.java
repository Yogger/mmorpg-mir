package com.mmorpg.mir.model.item.storage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.h2.util.New;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.mmorpg.mir.model.gameobjects.stats.StatEnum;
import com.mmorpg.mir.model.item.AbstractItem;
import com.mmorpg.mir.model.item.NullItem;
import com.mmorpg.mir.model.item.AbstractItem.AddResult;
import com.mmorpg.mir.model.item.core.ItemManager;

public class ItemStorage {

	private static final int DEFAULT_SIZE = 50;

	private int size;

	private int maxSize = 100;

	private AbstractItem[] items;

	@JsonIgnore
	private transient BitSet marks;

	@JsonIgnore
	private long openTime;

	@JsonIgnore
	private long lastMergeTime;

	private transient long remainTime;

	private long waitTime;

	protected transient Stat hpStat;

	public static ItemStorage valueOf(int size, int maxSize) {
		ItemStorage storage = new ItemStorage(size);
		storage.setMaxSize(maxSize);
		return storage;
	}

	public ItemStorage() {
		this(DEFAULT_SIZE);
	}

	public ItemStorage(int size) {
		this.size = size;
		items = new AbstractItem[size];
		marks = new BitSet(size);
		remainTime = 120 * 1000;
		hpStat = new Stat(StatEnum.MAXHP, 0, 0, 0);
	}

	public AbstractItem getItemByKey(String key) {
		refreshDeprecated();
		for (int i = 0, j = items.length; i < j; i++) {
			if (items[i] != null) {
				if (items[i].getKey().equals(key)) {
					return items[i];
				}
			}
		}
		return null;
	}

	public List<AbstractItem> getItemsByKey(String key) {
		refreshDeprecated();
		List<AbstractItem> result = new LinkedList<AbstractItem>();
		for (int i = 0, j = items.length; i < j; i++) {
			if (items[i] != null) {
				if (items[i].getKey().equals(key)) {
					result.add(items[i]);
				}
			}
		}
		return result;
	}

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

	public void refreshDeprecated() {
		for (int i = 0, j = items.length; i < j; i++) {
			if (items[i] != null) {
				if (items[i].hasDeprecated()) {
					items[i].checkDeprecated();
				}
			}
		}
	}

	public AbstractItem getItemByIndex(int index) {
		if (index >= 0 && index < items.length) {
			return items[index];
		}
		return null;
	}

	public void exchage(int fromIndex, int toIndex) {
		refreshDeprecated();
		if (fromIndex >= 0 && fromIndex < items.length && toIndex >= 0 && toIndex < items.length) {
			AbstractItem from = items[fromIndex];
			AbstractItem to = items[toIndex];
			if (from == null) {
				return;
			}
			if (to == null) {
				items[toIndex] = items[fromIndex];
				items[fromIndex] = null;
				marks.set(fromIndex);
				marks.set(toIndex);
				return;
			}
			if (to.getSize() >= to.getOverLimit() || from.getSize() >= from.getOverLimit()) {
				AbstractItem temp = items[fromIndex];
				items[fromIndex] = items[toIndex];
				items[toIndex] = temp;
				marks.set(fromIndex);
				marks.set(toIndex);
			} else {
				AddResult addResult = to.add(from);
				if (addResult.isSuccess()) {
					items[fromIndex] = addResult.getResult();
					marks.set(fromIndex);
					marks.set(toIndex);
				} else {
					AbstractItem temp = items[fromIndex];
					items[fromIndex] = items[toIndex];
					items[toIndex] = temp;
					marks.set(fromIndex);
					marks.set(toIndex);
				}
			}
		}
	}

	public void addItemByIndex(int index, AbstractItem item) {
		if (index >= 0 && index < items.length) {
			items[index] = item;
			marks.set(index);
		}
	}

	public int getIndexByGuid(long guid) {
		for (int i = 0, j = items.length; i < j; i++) {
			if (items[i] != null) {
				if (items[i].getObjectId().longValue() == guid) {
					return i;
				}
			}
		}
		return -1;
	}

	@JsonIgnore
	public boolean isFull() {
		return getCurrentSize() >= size;
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
	public int getEmptySize() {
		return size - getCurrentSize();
	}

	public void addItems(AbstractItem... items) {
		addItems(true, items);
	}

	public void addItems(boolean check, AbstractItem... items) {
		if (items != null) {
			for (AbstractItem item : items) {
				if (item != null) {
					addItem(item, check);
				}
			}
		}
	}

	private void addItem(AbstractItem item, boolean check) {
		AbstractItem addItem = item;
		// 首先在已经有的道具上面添加
		if (addItem.getOverLimit() > 1 && check) {
			for (int i = 0, j = items.length; i < j; i++) {
				if (items[i] != null) {
					AddResult result = items[i].add(item);
					if (result.isSuccess()) {
						marks.set(i);
						addItem = result.getResult();
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
					marks.set(i);
					addItem = null;
					break;
				}
			}
		}
		// 最后拓展
		if (addItem != null) {
			AbstractItem[] newItems = new AbstractItem[items.length * 2];
			System.arraycopy(items, 0, newItems, 0, items.length);
			marks.set(items.length, newItems.length - 1, false);
			newItems[items.length] = addItem;
			marks.set(items.length);
			items = newItems;
		}
	}

	/**
	 * 
	 * @param key
	 * @param deprecated
	 *            true-包含过期物品
	 * @return
	 */
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

	public long getItemSizeByKey(String key) {
		return getItemSizeByKey(key, false);
	}

	public long getItemSizeByIndex(int index) {
		if (items[index] != null) {
			return items[index].getSize();
		}
		return 0;
	}

	public long getItemSizeByGuid(long guid) {
		for (int i = 0, j = items.length; i < j; i++) {
			if (items[i] != null) {
				if (items[i].getObjectId().longValue() == guid) {
					return items[i].getSize();
				}
			}
		}
		return 0;
	}

	public boolean reduceItemByGuid(long guid, int reduce) {
		for (int i = 0, j = items.length; i < j; i++) {
			if (items[i] != null) {
				if (items[i].getObjectId().longValue() == guid) {
					if (items[i].getSize() >= reduce) {
						int size = items[i].getSize() - reduce;
						if (size == 0) {
							items[i] = null;
						} else {
							items[i].setSize(size);
						}
						marks.set(i);
						return true;
					}
				}
			}
		}
		return false;
	}

	public AbstractItem removeItemByGuid(long guid) {
		for (int i = 0, j = items.length; i < j; i++) {
			if (items[i] != null) {
				if (items[i].getObjectId().longValue() == guid) {
					AbstractItem result = items[i];
					items[i] = null;
					marks.set(i);
					return result;
				}
			}
		}
		return null;
	}

	public boolean reduceItemByIndex(int index, int reduce) {
		if (index >= 0 && index < items.length) {
			if (items[index] != null) {
				if (items[index].getSize() >= reduce) {
					int size = items[index].getSize() - reduce;
					if (size == 0) {
						items[index] = null;
					} else {
						items[index].setSize(size);
					}
					marks.set(index);
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}

	public AbstractItem removeItemByIndex(int index) {
		if (index >= 0 && index < items.length) {
			AbstractItem result = items[index];
			items[index] = null;
			if (result != null) {
				marks.set(index);
			}
			return result;
		}
		return null;
	}
	
	public List<RemoveItem> removeItemByKeyNotDeprecated(String key) {
		List<RemoveItem> result = new ArrayList<RemoveItem>();
		if (key != null) {
			for (int i = 0, j = items.length; i < j; i++) {
				if (items[i] != null) {
					if (items[i].getKey().equals(key)) {
						result.add(RemoveItem.valueOf(items[i], items[i].getSize()));
						items[i] = null;
						marks.set(i);
					}
				}
			}
			return result;
		}
		return null;
	}

	public List<RemoveItem> reduceItemByKey(String key, int reduce) {
		refreshDeprecated();
		List<RemoveItem> result = new ArrayList<RemoveItem>();
		if (getItemSizeByKey(key) >= reduce) {
			int needReduce = reduce;
			// 优先要过期的
			for (int i = 0, j = items.length; i < j; i++) {
				if (items[i] != null) {
					if (items[i].getKey().equals(key) && items[i].hasDeprecated() && !items[i].isDeprecated()) {
						if (items[i].getSize() >= needReduce) {
							int size = items[i].getSize() - needReduce;
							result.add(RemoveItem.valueOf(items[i], needReduce));
							if (size == 0) {
								items[i] = null;
							} else {
								items[i].setSize(size);
							}
							needReduce = 0;
							marks.set(i);
							break;
						} else {
							result.add(RemoveItem.valueOf(items[i], items[i].getSize()));
							needReduce -= items[i].getSize();
							items[i] = null;
							marks.set(i);
						}
					}
				}
			}

			if (needReduce == 0) {
				return result;
			}

			// 绑定的
			for (int i = 0, j = items.length; i < j; i++) {
				if (items[i] != null) {
					if (items[i].getKey().equals(key) && items[i].isBind() && !items[i].isDeprecated()) {
						if (items[i].getSize() >= needReduce) {
							int size = items[i].getSize() - needReduce;
							result.add(RemoveItem.valueOf(items[i], needReduce));
							if (size == 0) {
								items[i] = null;
							} else {
								items[i].setSize(size);
							}
							needReduce = 0;
							marks.set(i);
							break;
						} else {
							result.add(RemoveItem.valueOf(items[i], items[i].getSize()));
							needReduce -= items[i].getSize();
							items[i] = null;
							marks.set(i);
						}
					}
				}
			}
			if (needReduce == 0) {
				return result;
			}

			// 剩余的
			for (int i = 0, j = items.length; i < j; i++) {
				if (items[i] != null) {
					if (items[i].getKey().equals(key) && !items[i].isDeprecated()) {
						if (items[i].getSize() >= needReduce) {
							int size = items[i].getSize() - needReduce;
							result.add(RemoveItem.valueOf(items[i], needReduce));
							if (size == 0) {
								items[i] = null;
							} else {
								items[i].setSize(size);
							}
							marks.set(i);
							break;
						} else {
							result.add(RemoveItem.valueOf(items[i], items[i].getSize()));
							needReduce -= items[i].getSize();
							items[i] = null;
							marks.set(i);
						}
					}
				}
			}
			return result;
		}
		return result;
	}

	public void markByGuid(long guid) {
		for (int i = 0, j = items.length; i < j; i++) {
			if (items[i] != null) {
				if (items[i].getObjectId().longValue() == guid) {
					marks.set(i);
				}
			}
		}
	}

	public void markByIndex(int index) {
		if (index >= 0 && index < items.length) {
			marks.set(index);
		}
	}

	public Map<Integer, Object> collectUpdate() {
		Map<Integer, Object> result = new HashMap<Integer, Object>();
		for (int i = 0, j = marks.length(); i < j; i++) {
			if (marks.get(i)) {
				if (items[i] == null) {
					result.put(i, NullItem.getInstance());
				} else {
					result.put(i, items[i]);
				}
			}
			marks.set(i, false);
		}
		return result;
	}

	public AbstractItem[] getItems() {
		return items;
	}

	public void setItems(AbstractItem[] items) {
		this.items = items;
	}

	@JsonIgnore
	public BitSet getMarks() {
		return marks;
	}

	public void setMarks(BitSet marks) {
		this.marks = marks;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
		items = Arrays.copyOf(items, size);
	}

	public boolean isItemEnoughByKey(String itemKey, long itemNum) {
		return getItemSizeByKey(itemKey) >= itemNum;
	}

	public boolean isItemEnoughByGuid(long guid, long itemNum) {
		return getItemSizeByGuid(guid) >= itemNum;
	}

	public boolean isItemEnoughByIndex(int index, long itemNum) {
		return getItemSizeByIndex(index) >= itemNum;
	}

	public boolean isItemEnoughByFilter(IPackFilter filter, long itemNum) {
		return getItemSizeByFilter(filter) >= itemNum;
	}

	public int getItemSizeByFilter(IPackFilter filter) {
		int size = 0;
		for (int i = 0, j = items.length; i < j; i++) {
			if (items[i] != null) {
				if (filter.check(items[i])) {
					size += items[i].getSize();
				}
			}
		}
		return size;
	}

	public List<AbstractItem> getItemsByFilter(IPackFilter filter) {
		List<AbstractItem> result = new LinkedList<AbstractItem>();
		for (int i = 0, j = items.length; i < j; i++) {
			if (items[i] != null) {
				if (filter.check(items[i])) {
					result.add(items[i]);
				}
			}
		}
		return result;
	}

	public boolean removeItemByFilter(IPackFilter filter, int reduce) {
		if (getItemSizeByFilter(filter) >= reduce) {
			int remain = reduce;
			for (int i = 0, j = items.length; i < j; i++) {
				if (items[i] != null) {
					if (filter.check(items[i])) {
						if (items[i].getSize() >= remain) {
							int size = items[i].getSize() - remain;
							if (size == 0) {
								items[i] = null;
							} else {
								items[i].setSize(size);
							}
							marks.set(i);
							break;
						} else {
							remain -= items[i].getSize();
							items[i] = null;
							marks.set(i);
						}
					}
				}
			}
			return true;
		}
		return false;
	}

	public void merge() {
		List<AbstractItem> itemList = New.arrayList();

		// 首先收集一下所有的可叠加的道具
		for (int i = 0, j = items.length; i < j; i++) {
			if (items[i] != null) {
				// 如果是可以叠加的物品
				if (items[i].getOverLimit() > 1) {
					itemList.add(items[i]);
					items[i] = null;
				}
			}
		}

		// 然后再重新生成一下所有的可叠加道具
		addItems(true, itemList.toArray(new AbstractItem[itemList.size()]));

		// 最后再进行一次排序
		Arrays.sort(items, mergeComparator);

		// 清空mark
		marks.set(0, false);
	}

	private static final MergeComparator mergeComparator = new MergeComparator();

	public static final class MergeComparator implements Comparator<AbstractItem> {

		@Override
		public int compare(AbstractItem o1, AbstractItem o2) {
			if (o1 == null) {
				return 1;
			} else if (o2 == null) {
				return -1;
			} else {
				return o1.compareTo(o2);
			}
		}

	}

	public static interface IPackFilter {

		public boolean check(AbstractItem entity);

	}

	public int getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}

	@JsonIgnore
	public long getOpenTime() {
		return openTime;
	}

	public void setOpenTime(long openTime) {
		this.openTime = openTime;
	}

	public long getRemainTime() {
		return remainTime;
	}

	public void setRemainTime(long remainTime) {
		this.remainTime = remainTime;
	}

	@JsonIgnore
	public long getLastMergeTime() {
		return lastMergeTime;
	}

	public void setLastMergeTime(long lastMergeTime) {
		this.lastMergeTime = lastMergeTime;
	}

	public void save() {
		long now = System.currentTimeMillis();
		if (now < this.openTime) {
			this.remainTime = this.openTime - now;
		} else {
			this.remainTime = 0;
		}
	}

	public void reset() {
		if (this.remainTime > 0) {
			this.openTime = System.currentTimeMillis() + this.remainTime;
		}
	}

	public long getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(long waitTime) {
		this.waitTime = waitTime;
	}

	public void calculateWaitTime(long waitTime) {
		this.waitTime = waitTime;
		this.remainTime = waitTime;
	}

	public Stat getHpStat() {
		return hpStat;
	}

	public void setHpStat(Stat hpStat) {
		this.hpStat = hpStat;
	}

	public void initOpenPackHpStat(Player player, boolean recompute) {
		player.getGameStats().addModifiers(ItemManager.OPEN_WAREHOUSE, Arrays.asList(hpStat), recompute);
	}

}
