package com.mmorpg.mir.model.collect.packet;

import java.util.ArrayList;
import java.util.Map;

public class SM_FamedGeneral_Update {
	private Map<String, ArrayList<String>> collectStatus;

	public Map<String, ArrayList<String>> getCollectStatus() {
		return collectStatus;
	}

	public void setCollectStatus(Map<String, ArrayList<String>> collectStatus) {
		this.collectStatus = collectStatus;
	}

	public static SM_FamedGeneral_Update valueOf(Map<String, ArrayList<String>> map) {
		SM_FamedGeneral_Update update = new SM_FamedGeneral_Update();
		update.collectStatus = map;
		return update;
	}
}
