package com.mmorpg.mir.model.exchange.core;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.item.NullItem;

public class Exchange {

	private static final int EXCHANGE_SIZE = 5;

	private final Player activeplayer;
	private final Player targetPlayer;

	private boolean confirmed;
	private boolean locked;

	private Map<Integer, Integer> currencys = new HashMap<Integer, Integer>();
	private ExchangeItem[] items = new ExchangeItem[EXCHANGE_SIZE];
	private BitSet mark = new BitSet(EXCHANGE_SIZE);

	private Future<?> cleanUpTask;

	public Exchange(Player activeplayer, Player targetPlayer) {
		this.activeplayer = activeplayer;
		this.targetPlayer = targetPlayer;
	}

	public void confirm() {
		confirmed = Boolean.TRUE;
	}

	public boolean isConfirmed() {
		return confirmed;
	}

	public void lock() {
		this.locked = Boolean.TRUE;
	}

	public boolean isLocked() {
		return locked;
	}

	public int getSize() {
		int result = 0;
		for (ExchangeItem temp : items) {
			if (temp != null)
				result++;
		}
		return result;
	}

	public boolean hasEmpty() {
		for (ExchangeItem temp : items) {
			if (temp == null || temp.getItem() == null)
				return true;
		}
		return false;
	}

	public void addItem(ExchangeItem item) {
		for (int i = 0; i < EXCHANGE_SIZE; i++) {
			if (items[i] == null || items[i].getItem() == null) {
				items[i] = item;
				mark.set(i);
				break;
			}
		}
	}

	public void removeItemByIndex(int index) {
		if (index < 0 || index >= EXCHANGE_SIZE)
			return;

		if (items[index] != null) {
			items[index].setItem(null);
			mark.set(index);
		}
	}

	public void setItem(int index, ExchangeItem item) {
		if (index < 0 || index >= EXCHANGE_SIZE)
			return;

		if (items[index] != null && items[index].getItem() != null)
			item.setOldIndex(items[index].getIndex());

		items[index] = item;
		mark.set(index);
	}

	public Player getActiveplayer() {
		return activeplayer;
	}

	public Player getTargetPlayer() {
		return targetPlayer;
	}

	public Map<Integer, Integer> getCurrencys() {
		return currencys;
	}

	public void setCurrencys(Map<Integer, Integer> currencys) {
		this.currencys = currencys;
	}

	public ExchangeItem[] getItems() {
		return items;
	}

	public void setItems(ExchangeItem[] items) {
		this.items = items;
	}

	public boolean contains(int index) {
		if (index > -1) {
			for (ExchangeItem temp : items) {
				if (temp != null && temp.getItem() != null && temp.getIndex() == index)
					return true;
			}
		}
		return false;
	}

	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public Map<Integer, Object> collectUpdate() {
		Map<Integer, Object> result = new HashMap<Integer, Object>();
		for (int i = 0; i < EXCHANGE_SIZE; i++) {
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

	public Future<?> getCleanUpTask() {
		return cleanUpTask;
	}

	public void setCleanUpTask(Future<?> cleanUpTask) {
		this.cleanUpTask = cleanUpTask;
	}

	public void cancelAllTask() {
		if (cleanUpTask != null && (!cleanUpTask.isCancelled())) {
			cleanUpTask.cancel(true);
		}
	}
}
