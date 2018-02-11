package com.mmorpg.mir.model.soul.resource;

import java.util.Comparator;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.action.model.CoreActionResource;
import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

/**
 * 英魂配置表
 * 
 * @author 37wan
 * 
 */
@Resource
public class SoulResource {

	/** 英魂等级-阶段 */
	@Id
	private int id;

	/** 升到下一级需要的次数 */
	private int[] needCount;

	/** 需要材料的份数 */
	private int count;
	/** 暴击丹 */
	private CoreActionResource[] materialActionFirstPri;

	/** 进阶优先所需的材料 */
	private CoreActionResource[] materialActionsPriority;

	/** 一份材料 */
	private CoreActionResource[] materialActions;

	/** 一份元宝 */
	private CoreActionResource[] goldActions;

	/** 固定的铜钱 */
	private CoreActionResource[] copperActions;

	/** 提供的属性 */
	private Stat[][] stats;

	/** 祝福值是否清零 */
	private int countReset;
	
	// 强化道具使用次数限制
	private Map<String, Integer> enhanceItemCount;

	public static class GradeComparator implements Comparator<SoulResource> {

		@Override
		public int compare(SoulResource o1, SoulResource o2) {
			return o1.getId() - o2.getId();
		}

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int[] getNeedCount() {
		return needCount;
	}

	public void setNeedCount(int[] needCount) {
		this.needCount = needCount;
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

	public CoreActionResource[] getCopperActions() {
		return copperActions;
	}

	public void setCopperActions(CoreActionResource[] copperActions) {
		this.copperActions = copperActions;
	}

	public Stat[][] getStats() {
		return stats;
	}

	public void setStats(Stat[][] stats) {
		this.stats = stats;
	}

	public int getCountReset() {
		return countReset;
	}

	public void setCountReset(int countReset) {
		this.countReset = countReset;
	}

	public CoreActionResource[] getMaterialActionFirstPri() {
		return materialActionFirstPri;
	}

	public void setMaterialActionFirstPri(CoreActionResource[] materialActionFirstPri) {
		this.materialActionFirstPri = materialActionFirstPri;
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

	@JsonIgnore
	public boolean isMaxGrade() {
		return count == 0;
	}
	
}
