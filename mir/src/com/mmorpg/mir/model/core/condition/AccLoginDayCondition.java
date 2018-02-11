package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.gameobjects.Player;

public class AccLoginDayCondition extends AbstractCoreCondition {

	private Operator op;

	@Override
	public boolean verify(Object object) {
		Player player = null;
		if (object instanceof Player) {
			player = (Player) object;
		}

		if (null == player) {
			this.errorObject(object);
		}

		int accLoginDays = player.getWelfare().getAccLoginDays();
		if (Operator.EQUAL == op) {
			return accLoginDays == this.value;
		} else if (Operator.GREATER == op) {
			return accLoginDays > this.value;
		} else if (Operator.GREATER_EQUAL == op) {
			return accLoginDays >= this.value;
		} else if (Operator.LESS == op) {
			return accLoginDays < this.value;
		} else if (Operator.LESS_EQUAL == op) {
			return accLoginDays <= this.value;
		}
		return false;
	}

	@Override
	protected void init(CoreConditionResource resource) {
		super.init(resource);
		this.op = resource.getOperatorEnum();
	}
}
