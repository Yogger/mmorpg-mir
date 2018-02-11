package com.mmorpg.mir.model.horse.resource;

import java.util.Comparator;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.action.model.CoreActionResource;
import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class HorseResource {
	// 阶数
	@Id
	private int id;
	// chooser
	// 需要消耗的份数
	private int count;
	// 暴击丹
	private CoreActionResource[] materialActionFirstPri;
	// 进阶优先所需的材料
	private CoreActionResource[] materialActionsPriority;
	// 进阶所需的材料
	private CoreActionResource[] materialActions;
	// 材料不足需要消耗的元宝
	private CoreActionResource[] goldActions;
	// 附加属性
	private Stat[][] stats;
	// 速度属性
	private Stat[][] speedStat;
	// 等级上限
	private int maxLv;
	// 升到下一级
	private int needCount[];
	// 祝福值是否清零
	private int countReset;
	// 强化道具使用次数限制
	private Map<String, Integer> enhanceItemCount;

	// // 进阶最低人物等级
	// private String conditionId;
	// // 进阶最小次数
	// private int minUpgradeTimes;
	// // 是否公告
	// private boolean broadcast;

	private int skillBoxCount;

	public static class GradeComparator implements Comparator<HorseResource> {

		@Override
		public int compare(HorseResource o1, HorseResource o2) {
			return o1.getId() - o2.getId();
		}

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMaxLv() {
		return maxLv;
	}

	public void setMaxLv(int maxLv) {
		this.maxLv = maxLv;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public CoreActionResource[] getMaterialActionsPriority() {
		return materialActionsPriority;
	}

	public void setMaterialActionsPriority(CoreActionResource[] materialActionsPriority) {
		this.materialActionsPriority = materialActionsPriority;
	}

	public CoreActionResource[] getMaterialActions() {
		return materialActions;
	}

	public void setMaterialActions(CoreActionResource[] materialActions) {
		this.materialActions = materialActions;
	}

	public CoreActionResource[] getGoldActions() {
		return goldActions;
	}

	public void setGoldActions(CoreActionResource[] goldActions) {
		this.goldActions = goldActions;
	}

	public Stat[][] getStats() {
		return stats;
	}

	public void setStats(Stat[][] stats) {
		this.stats = stats;
	}

	public Stat[][] getSpeedStat() {
		return speedStat;
	}

	public void setSpeedStat(Stat[][] speedStat) {
		this.speedStat = speedStat;
	}

	public int getCountReset() {
		return countReset;
	}

	public void setCountReset(int countReset) {
		this.countReset = countReset;
	}

	@JsonIgnore
	public boolean isCountReset() {
		return countReset > 0;
	}

	public Map<String, Integer> getEnhanceItemCount() {
		return enhanceItemCount;
	}

	public void setEnhanceItemCount(Map<String, Integer> enhanceItemCount) {
		this.enhanceItemCount = enhanceItemCount;
	}

	public int getSkillBoxCount() {
		return skillBoxCount;
	}

	public void setSkillBoxCount(int skillBoxCount) {
		this.skillBoxCount = skillBoxCount;
	}

	public int[] getNeedCount() {
		return needCount;
	}

	public void setNeedCount(int[] needCount) {
		this.needCount = needCount;
	}

	public CoreActionResource[] getMaterialActionFirstPri() {
		return materialActionFirstPri;
	}

	public void setMaterialActionFirstPri(CoreActionResource[] materialActionFirstPri) {
		this.materialActionFirstPri = materialActionFirstPri;
	}

	@JsonIgnore
	public boolean isMaxGrade() {
		return count == 0;
	}

}
