package com.mmorpg.mir.model.item.resource;

import com.mmorpg.mir.model.core.action.model.CoreActionResource;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class EquipmentEnhanceResource {
	@Id
	private int level;
	/** 下一个强化等级 */
	private int nextLevel;
	/** 强化的条件 */
	private String[] conditionIds;
	/** 强化成功的概率 */
	private int succRate;
	/** 强化的消耗 */
	private CoreActionResource[] actionIds;

	private CoreActionResource[] copperActionIds;
	
	private String[] autoBuyCondition;
	
	private String autoBuyShopId;
	
	private int autoBuyNums;

	private String luckyPointType;

	private String luckyPoint;

	private int luckyMax;
	
	/** 强化所需要的最小次数 **/
	private int minSum;
	
	/** 强化所需要的最大次数 **/
	private int maxSum;
	
	/** BETA19 版本转换后的强化等级 **/
	private int beta19NewEnhanceLevel;

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getNextLevel() {
		return nextLevel;
	}

	public void setNextLevel(int nextLevel) {
		this.nextLevel = nextLevel;
	}

	public String getLuckyPointType() {
		return luckyPointType;
	}

	public void setLuckyPointType(String luckyPointType) {
		this.luckyPointType = luckyPointType;
	}

	public String getLuckyPoint() {
		return luckyPoint;
	}

	public void setLuckyPoint(String luckyPoint) {
		this.luckyPoint = luckyPoint;
	}

	public int getLuckyMax() {
		return luckyMax;
	}

	public void setLuckyMax(int luckyMax) {
		this.luckyMax = luckyMax;
	}

	public String[] getConditionIds() {
		return conditionIds;
	}

	public void setConditionIds(String[] conditionIds) {
		this.conditionIds = conditionIds;
	}

	public int getSuccRate() {
		return succRate;
	}

	public void setSuccRate(int succRate) {
		this.succRate = succRate;
	}

	public CoreActionResource[] getActionIds() {
		return actionIds;
	}

	public void setActionIds(CoreActionResource[] actionIds) {
		this.actionIds = actionIds;
	}

	public String getAutoBuyShopId() {
    	return autoBuyShopId;
    }

	public void setAutoBuyShopId(String autoBuyShopId) {
    	this.autoBuyShopId = autoBuyShopId;
    }

	public int getAutoBuyNums() {
    	return autoBuyNums;
    }

	public void setAutoBuyNums(int autoBuyNums) {
    	this.autoBuyNums = autoBuyNums;
    }

	public CoreActionResource[] getCopperActionIds() {
		return copperActionIds;
	}

	public void setCopperActionIds(CoreActionResource[] copperActionIds) {
		this.copperActionIds = copperActionIds;
	}

	public String[] getAutoBuyCondition() {
    	return autoBuyCondition;
    }

	public void setAutoBuyCondition(String[] autoBuyCondition) {
    	this.autoBuyCondition = autoBuyCondition;
    }

	public int getMinSum() {
		return minSum;
	}

	public void setMinSum(int minSum) {
		this.minSum = minSum;
	}

	public int getMaxSum() {
		return maxSum;
	}

	public void setMaxSum(int maxSum) {
		this.maxSum = maxSum;
	}

	public int getBeta19NewEnhanceLevel() {
		return beta19NewEnhanceLevel;
	}

	public void setBeta19NewEnhanceLevel(int beta19NewEnhanceLevel) {
		this.beta19NewEnhanceLevel = beta19NewEnhanceLevel;
	}

}
