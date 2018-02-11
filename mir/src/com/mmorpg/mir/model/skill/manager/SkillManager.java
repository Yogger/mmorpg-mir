package com.mmorpg.mir.model.skill.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.ResourceReload;
import com.mmorpg.mir.model.skill.resource.SkillResource;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;
import com.windforce.common.utility.New;

@Component
public final class SkillManager implements ResourceReload,IShillManager {
	@Static
	private Storage<Integer, SkillResource> skillResources;

	private static SkillManager instance;

	private Map<Class<?>, List<Integer>> acceptEventSkills;

	@PostConstruct
	public void init() {
		instance = this;
		this.reload();
	}

	public static SkillManager getInstance() {
		return instance;
	}

	public SkillResource getResource(int id) {
		return getSkillResources().get(id, true);
	}

	@Override
	public void reload() {
		Map<Class<?>, List<Integer>> acceptEventSkillsTemp = New.hashMap();
		for (SkillResource resource : getSkillResources().getAll()) {
			for (Class<?> clazz : resource.getAllAcceptEvent()) {
				if (!acceptEventSkillsTemp.containsKey(clazz)) {
					acceptEventSkillsTemp.put(clazz, new ArrayList<Integer>());
				}
				acceptEventSkillsTemp.get(clazz).add(resource.getSkillId());
			}
		}

		this.setAcceptEventSkills(acceptEventSkillsTemp);
	}

	public Map<Class<?>, List<Integer>> getAcceptEventSkills() {
		return acceptEventSkills;
	}

	public void setAcceptEventSkills(Map<Class<?>, List<Integer>> acceptEventSkills) {
		this.acceptEventSkills = acceptEventSkills;
	}

	public Storage<Integer, SkillResource> getSkillResources() {
		return skillResources;
	}

	public void setSkillResources(Storage<Integer, SkillResource> skillResources) {
		this.skillResources = skillResources;
	}

	@Override
	public Class<?> getResourceClass() {
		return SkillResource.class;
	}

}
