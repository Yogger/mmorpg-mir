package com.mmorpg.mir.model.item.resource;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.consumable.model.CoreActionResource;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Index;
import com.windforce.common.resource.anno.Resource;

@Resource
public class EquipmentEnhanceExResource {

	public static final String EQUIPSTORAGE_LEVEL = "EQUIPSTORAGE_LEVEL";

	@Id
	private String id;
	/** 装备栏类型 */
	private int equipStorageType;
	/** 强化等级 */
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

	@Index(name = EQUIPSTORAGE_LEVEL, unique = true)
	@JsonIgnore
	public String getStorageLevelIndex() {
		return this.level + "_" + this.equipStorageType;
	}

	public static String toStorageLevelIndex(int level, int equipStorageType) {
		return level + "_" + equipStorageType;
	}

	public String getId() {
		return id;
	}

	public int getEquipStorageType() {
		return equipStorageType;
	}

	public int getLevel() {
		return level;
	}

	public int getNextLevel() {
		return nextLevel;
	}

	public String[] getConditionIds() {
		return conditionIds;
	}

	public int getSuccRate() {
		return succRate;
	}

	public CoreActionResource[] getActionIds() {
		return actionIds;
	}

	public CoreActionResource[] getCopperActionIds() {
		return copperActionIds;
	}

	public String[] getAutoBuyCondition() {
		return autoBuyCondition;
	}

	public String getAutoBuyShopId() {
		return autoBuyShopId;
	}

	public int getAutoBuyNums() {
		return autoBuyNums;
	}

	public String getLuckyPointType() {
		return luckyPointType;
	}

	public String getLuckyPoint() {
		return luckyPoint;
	}

	public int getLuckyMax() {
		return luckyMax;
	}

	public int getMinSum() {
		return minSum;
	}

	public int getMaxSum() {
		return maxSum;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setEquipStorageType(int equipStorageType) {
		this.equipStorageType = equipStorageType;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setNextLevel(int nextLevel) {
		this.nextLevel = nextLevel;
	}

	public void setConditionIds(String[] conditionIds) {
		this.conditionIds = conditionIds;
	}

	public void setSuccRate(int succRate) {
		this.succRate = succRate;
	}

	public void setActionIds(CoreActionResource[] actionIds) {
		this.actionIds = actionIds;
	}

	public void setCopperActionIds(CoreActionResource[] copperActionIds) {
		this.copperActionIds = copperActionIds;
	}

	public void setAutoBuyCondition(String[] autoBuyCondition) {
		this.autoBuyCondition = autoBuyCondition;
	}

	public void setAutoBuyShopId(String autoBuyShopId) {
		this.autoBuyShopId = autoBuyShopId;
	}

	public void setAutoBuyNums(int autoBuyNums) {
		this.autoBuyNums = autoBuyNums;
	}

	public void setLuckyPointType(String luckyPointType) {
		this.luckyPointType = luckyPointType;
	}

	public void setLuckyPoint(String luckyPoint) {
		this.luckyPoint = luckyPoint;
	}

	public void setLuckyMax(int luckyMax) {
		this.luckyMax = luckyMax;
	}

	public void setMinSum(int minSum) {
		this.minSum = minSum;
	}

	public void setMaxSum(int maxSum) {
		this.maxSum = maxSum;
	}

}
