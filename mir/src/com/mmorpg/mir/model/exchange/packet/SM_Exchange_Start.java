package com.mmorpg.mir.model.exchange.packet;

public class SM_Exchange_Start {
	private String name;
	private int level;
	private String server;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public static SM_Exchange_Start valueOf(String name, int level, String server) {
		SM_Exchange_Start req = new SM_Exchange_Start();
		req.name = name;
		req.level = level;
		return req;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}
}
