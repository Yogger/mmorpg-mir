package com.mmorpg.mir.model.world.resource;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.world.Position;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class SafeAreaResource {

	@Id
	private String id;

	/** 安全区所属的国家 */
	private int country;
	/** 安全区存在的条件 */
	private String[] conditionId;
	/** 安全区中心的横坐标 */
	private Position[] safePointSet;

	private int x;
	private int y;

	private int halfRowRange;

	private int halfColRange;

	@Transient
	private CoreConditions existCondition;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String[] getConditionId() {
		return conditionId;
	}

	public void setConditionId(String[] conditionId) {
		this.conditionId = conditionId;
	}

	public Position[] getSafePointSet() {
		return safePointSet;
	}

	public void setSafePointSet(Position[] safePointSet) {
		this.safePointSet = safePointSet;
	}

	public CoreConditions getExistCondition() {
		return existCondition;
	}

	public void setExistCondition(CoreConditions existCondition) {
		this.existCondition = existCondition;
	}

	@JsonIgnore
	public CoreConditions getConditions() {
		if (existCondition == null) {
			if (conditionId == null) {
				existCondition = new CoreConditions();
			} else {
				existCondition = CoreConditionManager.getInstance().getCoreConditions(1, conditionId);
			}
		}
		return existCondition;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public int getCountry() {
		return country;
	}

	public void setCountry(int country) {
		this.country = country;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getHalfRowRange() {
		return halfRowRange;
	}

	public void setHalfRowRange(int halfRowRange) {
		this.halfRowRange = halfRowRange;
	}

	public int getHalfColRange() {
		return halfColRange;
	}

	public void setHalfColRange(int halfColRange) {
		this.halfColRange = halfColRange;
	}
}
