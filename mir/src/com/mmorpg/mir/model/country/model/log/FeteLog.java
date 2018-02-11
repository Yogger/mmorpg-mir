package com.mmorpg.mir.model.country.model.log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.country.resource.ConfigValueManager;

public class FeteLog {
	private LinkedList<FeteLogValue> logValues = new LinkedList<FeteLogValue>();

	public static FeteLog valueOf() {
		FeteLog logger = new FeteLog();
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
	public synchronized void record(String server, long objId, int type, String name, int honor, long lastTime, int countryValue) {
		if (logValues.size() >= ConfigValueManager.getInstance().SACRIFICE_TOTAL_SIZE.getValue()) {
			logValues.removeFirst();
		}
		logValues.add(FeteLogValue.valueOf(server, objId, type, name, honor, lastTime, countryValue));
	}

	@JsonIgnore
	public synchronized ArrayList<FeteLogValue> getLogValues(boolean all) {
		ArrayList<FeteLogValue> dest = null;
		if (all) {
			dest = new ArrayList<FeteLogValue>(logValues);
		} else {
			dest = new ArrayList<FeteLogValue>();
			int count = 0;
			Iterator<FeteLogValue> iterator = logValues.descendingIterator();
			while (iterator.hasNext()) {
				FeteLogValue element = iterator.next();
				dest.add(FeteLogValue.valueOf(element.getServer(), element.getPlayerId(), element.getType(), element.getName(),
				        element.getHonor(), element.getTimeMills(), element.getCountryValue()));
				count++;
				if (count >= ConfigValueManager.getInstance().SACRIFICE_LASTED_SIZE.getValue()) {
					break;
				}
			}
		}

		return dest;
	}

	public synchronized LinkedList<FeteLogValue> getLogValues() {
		return logValues;
	}

	public void setLogValues(LinkedList<FeteLogValue> logValues) {
		this.logValues = logValues;
	}

}
