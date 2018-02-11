package com.mmorpg.mir.model.item.resource;

import java.util.Comparator;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Index;
import com.windforce.common.resource.anno.Resource;

@Resource
public class CombingExResource {

	public static final String GROUP_INDEX = "GROUP_INDEX";

	@Id
	private String id;

	/** 合成条件 */
	private String[] conditionIds;
	/** 组 */
	@Index(name = GROUP_INDEX, comparatorClz = GradeComparator.class)
	private int group;

	private int specialType;

	private String[] materialItemId;

	private int grade;

	private String targetItemId;

	private int successRate;

	private Stat[] extraStats;

	public static class GradeComparator implements Comparator<CombingExResource> {

		@Override
		public int compare(CombingExResource o1, CombingExResource o2) {
			return o2.grade - o1.grade;
		}

	}

	@JsonIgnore
	public CoreConditions getConditions() {
		return CoreConditionManager.getInstance().getCoreConditions(1, this.conditionIds);
	}

	@JsonIgnore
	public boolean isInMaterialItemId(String materialId) {
		for (String mid : this.materialItemId) {
			if (mid.equals(materialId)) {
				return true;
			}
		}
		return false;
	}

	public String getId() {
		return id;
	}

	public String[] getConditionIds() {
		return conditionIds;
	}

	public int getSuccessRate() {
		return successRate;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setConditionIds(String[] conditionIds) {
		this.conditionIds = conditionIds;
	}

	public void setSuccessRate(int successRate) {
		this.successRate = successRate;
	}

	public Stat[] getExtraStats() {
		return extraStats;
	}

	public void setExtraStats(Stat[] extraStats) {
		this.extraStats = extraStats;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public int getGroup() {
		return group;
	}

	public void setGroup(int group) {
		this.group = group;
	}

	public String[] getMaterialItemId() {
		return materialItemId;
	}

	public void setMaterialItemId(String[] materialItemId) {
		this.materialItemId = materialItemId;
	}

	public int getSpecialType() {
		return specialType;
	}

	public void setSpecialType(int specialType) {
		this.specialType = specialType;
	}

	public String getTargetItemId() {
		return targetItemId;
	}

	public void setTargetItemId(String targetItemId) {
		this.targetItemId = targetItemId;
	}

}
