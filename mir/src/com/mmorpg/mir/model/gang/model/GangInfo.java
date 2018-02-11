package com.mmorpg.mir.model.gang.model;

public class GangInfo {
	private long gangId;
	private String name;
	private String server;

	public static GangInfo valueOf(Gang gang) {
		if (gang == null)
			return null;
		GangInfo sm = new GangInfo();
		sm.gangId = gang.getId();
		sm.name = gang.getName();
		sm.server = gang.getServer();
		return sm;
	}

	public long getGangId() {
		return gangId;
	}

	public void setGangId(long gangId) {
		this.gangId = gangId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

}
