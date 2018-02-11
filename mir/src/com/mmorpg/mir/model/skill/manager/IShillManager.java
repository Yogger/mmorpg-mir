package com.mmorpg.mir.model.skill.manager;

import java.util.List;
import java.util.Map;

import com.mmorpg.mir.model.skill.resource.SkillResource;
import com.windforce.common.resource.Storage;

public interface IShillManager {
	SkillResource getResource(int id);

	public Map<Class<?>, List<Integer>> getAcceptEventSkills();

	public void setAcceptEventSkills(Map<Class<?>, List<Integer>> acceptEventSkills);

	public Storage<Integer, SkillResource> getSkillResources();

	public void setSkillResources(Storage<Integer, SkillResource> skillResources);
}
