package com.mmorpg.mir.model.seal.resource;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.consumable.model.CoreActionResource;
import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class SealResource {
	/** 阶数 */
	@Id
	private int id;
	/** 升级所需的次数 */
	private int[] needCount;
	/** 属性数组 */
	private Stat[][] stats;
	/** 需要的材料份数 */
	private int actCount;
	/** 暴击丹 */
	private CoreActionResource[] firstMaterialActions;
	/** 一份材料 */
	private CoreActionResource[] secondmaterialActions;
	/** 一份元宝 */
	private CoreActionResource[] goldActions;

	private int skillBoxCount;
	/** 祝福值是否清0 */
	private int countReset;

	@JsonIgnore
	public boolean isMaxGrade() {
		return this.actCount == 0;
	}

	@JsonIgnore
	public boolean canUpLevel(int level, int count) {
		return count >= this.needCount[level];
	}

	@JsonIgnore
	public boolean isCurrentCountReset() {
		return this.countReset > 0;
	}

	public int getId() {
		return id;
	}

	public int[] getNeedCount() {
		return needCount;
	}

	public Stat[][] getStats() {
		return stats;
	}

	public int getActCount() {
		return actCount;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setNeedCount(int[] needCount) {
		this.needCount = needCount;
	}

	public void setStats(Stat[][] stats) {
		this.stats = stats;
	}

	public void setActCount(int actCount) {
		this.actCount = actCount;
	}

	public CoreActionResource[] getGoldActions() {
		return goldActions;
	}

	public void setGoldActions(CoreActionResource[] goldActions) {
		this.goldActions = goldActions;
	}

	public CoreActionResource[] getFirstMaterialActions() {
		return firstMaterialActions;
	}

	public CoreActionResource[] getSecondmaterialActions() {
		return secondmaterialActions;
	}

	public void setFirstMaterialActions(CoreActionResource[] firstMaterialActions) {
		this.firstMaterialActions = firstMaterialActions;
	}

	public void setSecondmaterialActions(CoreActionResource[] secondmaterialActions) {
		this.secondmaterialActions = secondmaterialActions;
	}

	public int getSkillBoxCount() {
		return skillBoxCount;
	}

	public void setSkillBoxCount(int skillBoxCount) {
		this.skillBoxCount = skillBoxCount;
	}

	public int isCountReset() {
		return countReset;
	}

	public void setCountReset(int countReset) {
		this.countReset = countReset;
	}

}
