package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.openactive.model.GroupPurchaseTwo;
import com.mmorpg.mir.model.serverstate.ServerState;

public class GroupPurchaseTwoGoldAmountCondtion extends AbstractCoreCondition {
	private Operator op;

	@Override
	public boolean verify(Object object) {
		Long platerId = null;

		if (object instanceof Long) {
			platerId = (Long) object;
		}
		if (null == platerId) {
			this.errorObject(object);
		}
		if (null == ServerState.getInstance().getPlayerGroupPurchases2()) {
			return false;
		}
		GroupPurchaseTwo groupPurchaseTwo = ServerState.getInstance().getPlayerGroupPurchases2().get(platerId);
		if (null == groupPurchaseTwo) {
			return false;
		}

		long gold = groupPurchaseTwo.getGoldAmount();
		if (op == Operator.EQUAL) {
			return gold == this.value;
		} else if (op == Operator.GREATER) {
			return gold > this.value;
		} else if (op == Operator.GREATER_EQUAL) {
			return gold >= this.value;
		} else if (op == Operator.LESS) {
			return gold < this.value;
		} else if (op == Operator.LESS_EQUAL) {
			return gold <= this.value;
		}
		return false;
	}

	@Override
	protected void init(CoreConditionResource resource) {
		super.init(resource);
		this.op = resource.getOperatorEnum();
	}

}
