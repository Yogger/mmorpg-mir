package com.mmorpg.mir.model.country.model.log;

import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.h2.util.New;

public class CountryLogger {

	private ArrayList<CountryLogValue> logValues = New.arrayList();

	public static CountryLogger valueOf() {
		CountryLogger logger = new CountryLogger();
		return logger;
	}

	/**
	 * 记录
	 * 
	 * @param name
	 * @param item
	 * @param lastTime
	 */
	@JsonIgnore
	public void record(String name, ArrayList<String> items, long lastTime,int count) {
		if (items == null || items.isEmpty()) {
			return;
		}
		logValues.add(CountryLogValue.valueOf(name, items, lastTime,count));
	}

	@JsonIgnore
	public boolean isEmpty() {
		return logValues.isEmpty();
	}

	@JsonIgnore
	public void remove(int index) {
		if (index < 0 || index >= logValues.size()) {
			return;
		}
		logValues.remove(index);
	}
	
	@JsonIgnore
	public int getSize() {
		return logValues.size();
	}

	/**
	 * 最后一条记录
	 * 
	 * @return
	 */
	@JsonIgnore
	public CountryLogValue getLastLog() {
		if (logValues.isEmpty()) {
			return CountryLogValue.valueOf();
		}
		return logValues.get(logValues.size() - 1);
	}

	/**
	 * 最第一条记录
	 * 
	 * @return
	 */
	@JsonIgnore
	public CountryLogValue getFirstLog() {
		if (logValues.isEmpty()) {
			return CountryLogValue.valueOf();
		}
		return logValues.get(0);
	}

	public ArrayList<CountryLogValue> getLogValues() {
		return logValues;
	}

	public void setLogValues(ArrayList<CountryLogValue> logValues) {
		this.logValues = logValues;
	}

}
