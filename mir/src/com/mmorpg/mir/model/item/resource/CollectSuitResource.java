package com.mmorpg.mir.model.item.resource;

import java.util.Map;

import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class CollectSuitResource {

	@Id
	private Integer id;
	
	private String[] equipIds;
	
	private Map<String, String> soulValue;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String[] getEquipIds() {
		return equipIds;
	}

	public void setEquipIds(String[] equipIds) {
		this.equipIds = equipIds;
	}

	public Map<String, String> getSoulValue() {
		return soulValue;
	}

	public void setSoulValue(Map<String, String> soulValue) {
		this.soulValue = soulValue;
	}
	
}
