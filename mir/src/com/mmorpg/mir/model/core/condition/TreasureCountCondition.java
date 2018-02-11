package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.gameobjects.Player;

public class TreasureCountCondition extends AbstractCoreCondition {

	private Operator op;

	@Override
	public boolean verify(Object object) {
		Player player = null;

		if (object instanceof Player) {
			player = (Player) object;
		}

		if (player == null) {
			this.errorObject(object);
		}
		Integer count = player.getTreasureWareHouse().getTreasureCount().get(Integer.parseInt(this.code));
		int countValue = count == null? 0: count;

		if (op == Operator.GREATER_EQUAL) {
			return countValue >= this.value;
		} else if (op == Operator.GREATER) {
			return countValue > this.value;
		} else if (op == Operator.EQUAL) {
			return countValue == this.value;
		} else if (op == Operator.LESS_EQUAL) {
			return countValue <= this.value;
		} else if (op == Operator.LESS) {
			return countValue < this.value;
		}
		return false;
	}

	@Override
	protected void init(CoreConditionResource resource) {
		super.init(resource);
		this.op = resource.getOperatorEnum();
	}

}
