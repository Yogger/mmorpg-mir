package com.mmorpg.mir.model.collect.resource;

import java.util.Map;

import com.windforce.common.resource.anno.Id;

// @Resource
public class CollectSuitResource {

	@Id
	private String id;
	/** 有灵魂属性就可以 **/
	private boolean onlyHasSoul;
	/** 装备套等级 **/
	private int level;
	/** 指定装备ID集合 **/
	private String[] equipIds;
	/** 对应指定的灵魂属性 ID **/
	private Map<String, String> soulValue;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
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

	public boolean isOnlyHasSoul() {
		return onlyHasSoul;
	}

	public void setOnlyHasSoul(boolean onlyHasSoul) {
		this.onlyHasSoul = onlyHasSoul;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}
