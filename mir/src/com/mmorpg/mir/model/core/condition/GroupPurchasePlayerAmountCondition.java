package com.mmorpg.mir.model.core.condition;

import java.util.Set;

import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.serverstate.ServerState;

public class GroupPurchasePlayerAmountCondition extends AbstractCoreCondition {

	private Operator op;

	@Override
	public boolean verify(Object object) {
		if (ServerState.getInstance().getGroupPurchasePlayers() == null) {
			return false;
		}
		Set<Long> players = ServerState.getInstance().getGroupPurchasePlayers().get(this.code);
		int size = players.size();
		if (op == Operator.EQUAL) {
			return size == this.value;
		} else if (op == Operator.GREATER) {
			return size > this.value;
		} else if (op == Operator.GREATER_EQUAL) {
			return size >= this.value;
		} else if (op == Operator.LESS) {
			return size < this.value;
		} else if (op == Operator.LESS_EQUAL) {
			return size <= this.value;
		}
		return false;
	}

	@Override
	protected void init(CoreConditionResource resource) {
		super.init(resource);
		this.op = resource.getOperatorEnum();
	}

}
