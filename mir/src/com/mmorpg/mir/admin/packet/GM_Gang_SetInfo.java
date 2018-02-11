package com.mmorpg.mir.admin.packet;

public class GM_Gang_SetInfo {
	private String info;
	private long gangId;

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public long getGangId() {
		return gangId;
	}

	public void setGangId(long gangId) {
		this.gangId = gangId;
	}

}
