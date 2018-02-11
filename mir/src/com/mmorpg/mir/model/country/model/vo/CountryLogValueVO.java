package com.mmorpg.mir.model.country.model.vo;

import java.util.ArrayList;

import com.mmorpg.mir.model.country.model.log.CountryLogValue;

public class CountryLogValueVO {

	/** 操作人 */
	private String name;

	/** 物品 */
	private ArrayList<String> items;

	/** 最后操作的时间 */
	private long lastTime;
	
	private int count;

	public static CountryLogValueVO valueOf(CountryLogValue logValue) {
		CountryLogValueVO vo = new CountryLogValueVO();
		vo.setName(logValue.getName());
		vo.setItems(logValue.getItems());
		vo.setLastTime(logValue.getLastTime());
		vo.setCount(logValue.getCount());
		return vo;
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
