package com.mmorpg.mir.model.skill.model;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.skill.manager.SkillManager;
import com.mmorpg.mir.model.skill.resource.SkillResource;

public class SkillEntry {
	private int id;
	private int level;
	private int exp;
	private boolean end;

	public static SkillEntry valueOf(SkillResource skillResource) {
		SkillEntry se = new SkillEntry();
		se.id = skillResource.getSkillId();
		se.level = 1;
		se.exp = 0;
		return se;
	}

	@JsonIgnore
	public void levelUp() {
		level++;
		exp = 0;
	}

	@JsonIgnore
	public void addExp(int add) {
		if (this.getResource().getSkillType() == SkillType.PASSIVE.getValue()) {
			return;
		}
		exp += add;
	}

	@JsonIgnore
	public boolean isMaxLevel() {
		if (getResource().getMaxLevel() <= level) {
			return true;
		}
		return false;
	}

	@JsonIgnore
	public boolean isExpFull() {
		if (getResource().getSkillType() != SkillType.CAST.getValue()) {
			return false;
		}
		if (getResource().getExps() == null) {
			return true;
		}
		if (exp >= getResource().getExps()[level - 1]) {
			return true;
		}
		return false;
	}

	@JsonIgnore
	public SkillResource getResource() {
		return SkillManager.getInstance().getResource(id);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public boolean isEnd() {
		return end;
	}

	public void setEnd(boolean end) {
		this.end = end;
	}

}
