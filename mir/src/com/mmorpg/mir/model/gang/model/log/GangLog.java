package com.mmorpg.mir.model.gang.model.log;

import java.util.Map;

import org.h2.util.New;

public class GangLog implements Comparable<GangLog> {
	private String type;
	private long time;
	private Map<String, String> contexts = New.hashMap();

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public Map<String, String> getContexts() {
		return contexts;
	}

	public void setContexts(Map<String, String> contexts) {
		this.contexts = contexts;
	}

	@Override
	public int compareTo(GangLog o) {
		return (int) (this.time - o.time);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
