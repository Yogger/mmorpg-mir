package com.mmorpg.mir.model.gameobjects;

import java.util.Map.Entry;

import org.cliffc.high_scale_lib.NonBlockingHashMap;
import org.codehaus.jackson.annotate.JsonIgnore;

public class LevelLog {

	public static LevelLog valueOf() {
		LevelLog ll = new LevelLog();
		ll.logs = new NonBlockingHashMap<Integer, Long>();
		return ll;
	}

	private NonBlockingHashMap<Integer, Long> logs;

	@JsonIgnore
	public void levelUp(int level) {
		if (!logs.containsKey(logs)) {
			logs.put(level, System.currentTimeMillis());
		}
	}

	@JsonIgnore
	public boolean levelUpBefore(int level, long time) {
		for (Entry<Integer, Long> entry : logs.entrySet()) {
			if (entry.getKey() >= level || entry.getValue() <= time) {
				return true;
			}
		}
		return false;
	}

	public NonBlockingHashMap<Integer, Long> getLogs() {
		return logs;
	}

	public void setLogs(NonBlockingHashMap<Integer, Long> logs) {
		this.logs = logs;
	}

}
