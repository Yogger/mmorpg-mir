package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.gameobjects.Player;

public class CommonTreasureActiveCountCondition extends AbstractCoreCondition {

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
		if (!player.getCommonActivityPool().getTreasurueActives().containsKey(this.code)) {
			return false;
		}

		int currentCount = player.getCommonActivityPool().getTreasurueActives().get(this.code).getCount();
		if (op == Operator.EQUAL) {
			return currentCount == this.value;
		} else if (op == Operator.GREATER) {
			return currentCount > this.value;
		} else if (op == Operator.GREATER_EQUAL) {
			return currentCount >= this.value;
		} else if (op == Operator.LESS) {
			return currentCount < this.value;
		} else if (op == Operator.LESS_EQUAL) {
			return currentCount <= this.value;
		}
		return false;
	}

	@Override
	protected void init(CoreConditionResource resource) {
		super.init(resource);
		this.op = resource.getOperatorEnum();
	}
}
