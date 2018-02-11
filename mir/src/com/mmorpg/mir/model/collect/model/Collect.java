package com.mmorpg.mir.model.collect.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.mmorpg.mir.model.item.Equipment;

public class Collect {
	
	private Map<Integer, Equipment> collectEquipLog;
	
	private HashSet<String> collectRewarded;
	
	private FamedGeneral famedGeneral;
	
	public static Collect valueOf() {
		Collect c = new Collect();
		c.collectEquipLog = new HashMap<Integer, Equipment>();
		c.collectRewarded = new HashSet<String>();
		c.famedGeneral = FamedGeneral.valueOf();
		return c;
	}

	public Map<Integer, Equipment> getCollectEquipLog() {
		return collectEquipLog;
	}

	public void setCollectEquipLog(Map<Integer, Equipment> collectEquipLog) {
		this.collectEquipLog = collectEquipLog;
	}

	public HashSet<String> getCollectRewarded() {
		return collectRewarded;
	}

	public void setCollectRewarded(HashSet<String> collectRewarded) {
		this.collectRewarded = collectRewarded;
	}

	public FamedGeneral getFamedGeneral() {
		return famedGeneral;
	}

	public void setFamedGeneral(FamedGeneral famedGeneral) {
		this.famedGeneral = famedGeneral;
	}

}
