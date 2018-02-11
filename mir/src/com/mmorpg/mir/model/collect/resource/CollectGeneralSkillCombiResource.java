package com.mmorpg.mir.model.collect.resource;

import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class CollectGeneralSkillCombiResource {

	@Id
	private String id;
	
	private String[] generalResourceIds;
	
	private int[] skillId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String[] getGeneralResourceIds() {
		return generalResourceIds;
	}

	public void setGeneralResourceIds(String[] generalResourceIds) {
		this.generalResourceIds = generalResourceIds;
	}

	public int[] getSkillId() {
		return skillId;
	}

	public void setSkillId(int[] skillId) {
		this.skillId = skillId;
	}
	
}
