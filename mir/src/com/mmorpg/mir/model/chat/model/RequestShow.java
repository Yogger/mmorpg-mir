package com.mmorpg.mir.model.chat.model;

import java.util.HashMap;

public class RequestShow {
	private int showType;
	private HashMap<String, String> parms;

	public HashMap<String, String> getParms() {
		return parms;
	}

	public void setParms(HashMap<String, String> parms) {
		this.parms = parms;
	}

	public int getShowType() {
		return showType;
	}

	public void setShowType(int showType) {
		this.showType = showType;
	}

}
