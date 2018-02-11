package com.mmorpg.mir.model.country.model.log;

import java.util.ArrayList;

public class CountryLogValue {

	/** 操作人 */
	private String name;

	/** 物品 */
	private ArrayList<String> items;

	/** 最后操作的时间 */
	private long lastTime;

	private int count;

	public static CountryLogValue valueOf(String name, ArrayList<String> items, long lastTime, int count) {
		CountryLogValue logValue = new CountryLogValue();
		logValue.setName(name);
		logValue.setItems(items);
		logValue.setLastTime(lastTime);
		logValue.setCount(count);
		return logValue;
	}

	public static CountryLogValue valueOf() {
		CountryLogValue logValue = new CountryLogValue();
		return logValue;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<String> getItems() {
		return items;
	}

	public void setItems(ArrayList<String> items) {
		this.items = items;
	}

	public long getLastTime() {
		return lastTime;
	}

	public void setLastTime(long lastTime) {
		this.lastTime = lastTime;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
