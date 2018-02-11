package com.mmorpg.mir.model.express.resource;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class ExpressResource {

	@Id
	private String id;
	/** 颜色的ID(同一个国家,同一个等级段) */
	private String[] colorGroup;
	/** 奖励id */
	private String chooserId;
	/** 劫镖奖励 */
	private String robChooserId;
	/** 镖车id */
	private String lorrySpawnId;
	/** 镖车的品质 */
	private int color;
	/** 押镖需要满足的条件 */
	private String[] expressConditionIds;
	/** 运镖的保证金 */
	private int guaranteeMoney;
	/** 运镖的保证金货币类型 */
	private int guaranteeType;
	/** 是否增加次数 */
	private boolean addLorryCount;

	@Transient
	private CoreConditions expressCondition;

	@JsonIgnore
	public CoreConditions getExpressCondition() {
		if (expressCondition == null) {
			if (expressConditionIds == null) {
				expressCondition = new CoreConditions();
			} else {
				expressCondition = CoreConditionManager.getInstance().getCoreConditions(1, expressConditionIds);
			}
		}
		return expressCondition;
	}

	public int getGuaranteeMoney() {
		return guaranteeMoney;
	}

	public void setGuaranteeMoney(int guaranteeMoney) {
		this.guaranteeMoney = guaranteeMoney;
	}

	public int getGuaranteeType() {
		return guaranteeType;
	}

	public void setGuaranteeType(int guaranteeType) {
		this.guaranteeType = guaranteeType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getChooserId() {
		return chooserId;
	}

	public void setChooserId(String chooserId) {
		this.chooserId = chooserId;
	}

	public String getRobChooserId() {
		return robChooserId;
	}

	public void setRobChooserId(String robChooserId) {
		this.robChooserId = robChooserId;
	}

	public String getLorrySpawnId() {
		return lorrySpawnId;
	}

	public void setLorrySpawnId(String lorrySpawnId) {
		this.lorrySpawnId = lorrySpawnId;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	@JsonIgnore
	public boolean isGod() {
		return this.color == LorryColor.ORANGE.getValue();
	}

	public boolean isAddLorryCount() {
		return addLorryCount;
	}

	public void setAddLorryCount(boolean addLorryCount) {
		this.addLorryCount = addLorryCount;
	}

	/**
	 * 镖车的颜色
	 * 
	 * @author 37wan
	 */
	public static enum LorryColor {
		WHITE(1), GREEN(2), BLUE(3), PURPLE(4), ORANGE(5);

		private final int value;

		
		LorryColor(int c) {
			this.value = c;
		}

		public int getValue() {
			return value;
		}
	}

	public String[] getExpressConditionIds() {
		return expressConditionIds;
	}

	public void setExpressConditionIds(String[] expressConditionIds) {
		this.expressConditionIds = expressConditionIds;
	}

	public String[] getColorGroup() {
		return colorGroup;
	}

	public void setColorGroup(String[] colorGroup) {
		this.colorGroup = colorGroup;
	}

	@JsonIgnore
	public String getCurrentIndex(int colorIndex) {
		return colorGroup[colorIndex - 1];
	}
	
}
