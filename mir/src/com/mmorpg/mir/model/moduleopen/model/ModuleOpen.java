package com.mmorpg.mir.model.moduleopen.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.codehaus.jackson.annotate.JsonIgnore;

public class ModuleOpen {
	/** 已经开启的功能 */
	private Map<String, Boolean> openeds;
	
	private Map<String, Long> openDate;

	public static ModuleOpen valueOf() {
		ModuleOpen moduleOpen = new ModuleOpen();
		moduleOpen.openeds = new HashMap<String, Boolean>();
		moduleOpen.openDate = new HashMap<String, Long>();
		return moduleOpen;
	}

	public Map<String, Boolean> getOpeneds() {
		return openeds;
	}

	public void setOpeneds(Map<String, Boolean> openeds) {
		this.openeds = openeds;
	}

	public Map<String, Long> getOpenDate() {
		return openDate;
	}

	public void setOpenDate(Map<String, Long> openDate) {
		this.openDate = openDate;
	}

	@JsonIgnore
	public void initIfNeed() {
		if (openDate == null) {
			openDate = new HashMap<String, Long>();
			
			for (Entry<String, Boolean> entry : openeds.entrySet()) {
				if (!openDate.containsKey(entry.getKey())) {
					openDate.put(entry.getKey(), 0L);
				}
			}
		}
	}

	@JsonIgnore
	public void openModule(String opmkId) {
		Boolean value = openeds.get(opmkId);
		if (value == null || value == Boolean.FALSE) {
			openDate.put(opmkId, System.currentTimeMillis());
			openeds.put(opmkId, Boolean.TRUE);
		}
	}
	
}
