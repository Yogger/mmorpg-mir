package com.mmorpg.mir.model.core.condition;

import java.util.LinkedList;
import java.util.List;

import com.mmorpg.mir.model.common.exception.ManagedException;

public class CoreConditions {

	List<AbstractCoreCondition> conditionList = new LinkedList<AbstractCoreCondition>();

	public boolean verify(Object object) {
		return this.verify(object, false);
	}

	@SuppressWarnings("unchecked")
	public <T extends AbstractCoreCondition> T findConditionType(Class<T> clazz) {
		for (AbstractCoreCondition abCondition : conditionList) {
			if (abCondition.getClass() == clazz) {
				return (T) abCondition;
			}
		}
		return null;
	}

	public boolean verify(Object object, boolean isThrowException) throws ManagedException {

		for (AbstractCoreCondition condition : conditionList) {
			try {
				if (!condition.verify(object)) {
					return false;
				}
			} catch (ManagedException e) {
				if (isThrowException) {
					throw e;
				}
				return false;
			}
		}

		return true;
	}

	public boolean verifyOr(Object object) {
		for (AbstractCoreCondition condition : conditionList) {
			try {
				if (condition.verify(object)) {
					return true;
				}
			} catch (ManagedException e) {
				// 不处理
			}
		}

		return false;
	}

	public void addCondition(AbstractCoreCondition condition) {
		AbstractCoreCondition add = condition;
		for (AbstractCoreCondition temp : conditionList) {
			add = temp.add(add);
			if (add == null) {
				break;
			}
		}
		if (add != null) {
			conditionList.add(add);
		}
	}

	public void addConditions(AbstractCoreCondition... conditions) {
		for (AbstractCoreCondition condition : conditions) {
			addCondition(condition);
		}
	}

	public List<AbstractCoreCondition> getConditionList() {
		return conditionList;
	}

	public void setConditionList(List<AbstractCoreCondition> conditionList) {
		this.conditionList = conditionList;
	}

}
