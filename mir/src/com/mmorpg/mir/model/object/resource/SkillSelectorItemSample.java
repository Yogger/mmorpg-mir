package com.mmorpg.mir.model.object.resource;

import com.windforce.common.utility.JsonUtils;

/**
 * 技能释放
 * 
 * @author Kuang Hao
 * @since v1.0 2015-2-7
 * 
 */
public class SkillSelectorItemSample {
	/** 越大优先级越高 */
	private int priority;

	private int skillId;

	private String[] useConditions;

	private String[] targetConditions;

	public static void main(String[] args) {
		SkillSelectorItemSample s1 = new SkillSelectorItemSample();
		s1.setPriority(10);
		s1.setSkillId(12);
		s1.setUseConditions(new String[] { "condition1", "condition2", "condition3" });

		SkillSelectorItemSample[] ssi = new SkillSelectorItemSample[] { s1, s1 };
		System.out.println(JsonUtils.object2String(ssi));
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getSkillId() {
		return skillId;
	}

	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}

	public String[] getUseConditions() {
		return useConditions;
	}

	public void setUseConditions(String[] useConditions) {
		this.useConditions = useConditions;
	}

	public String[] getTargetConditions() {
		return targetConditions;
	}

	public void setTargetConditions(String[] targetConditions) {
		this.targetConditions = targetConditions;
	}

}
