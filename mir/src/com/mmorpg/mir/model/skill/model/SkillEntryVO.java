package com.mmorpg.mir.model.skill.model;

public class SkillEntryVO {
	private int id;
	private int level;
	private int exp;
	private boolean end;

	public static SkillEntryVO valueOf(SkillEntry se) {
		SkillEntryVO vo = new SkillEntryVO();
		vo.id = se.getId();
		vo.level = se.getLevel();
		vo.exp = se.getExp();
		vo.end = se.isEnd();
		return vo;
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
