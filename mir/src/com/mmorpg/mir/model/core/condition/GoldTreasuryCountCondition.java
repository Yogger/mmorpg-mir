package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.gameobjects.Player;

public class GoldTreasuryCountCondition extends AbstractCoreCondition {
	private Operator op;
	private int low;
	private int high;

	@Override
	public boolean verify(Object object) {
		Player player = null;
		if (!(object instanceof Player)) {
			new ManagedException(ManagedErrorCode.NO_ROLE);
		}
		player = (Player) object;
		int count = player.getCommonActivityPool().getGoldTreasurys().get(code).getTreasuryCount(value);
		if (count == -1) {
			return false;
		}
		if (op != null) {
			if (op == Operator.EQUAL) {
				return count == this.value;
			} else if (op == Operator.GREATER) {
				return count > this.value;
			} else if (op == Operator.GREATER_EQUAL) {
				return count >= this.value;
			} else if (op == Operator.LESS) {
				return count < this.value;
			} else if (op == Operator.LESS_EQUAL) {
				return count <= this.value;
			}
		} else {
			if (count >= low && count <= high) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected void init(CoreConditionResource resource) {
		super.init(resource);
		if (resource.getOperator() != null) {
			this.op = resource.getOperatorEnum();
		}
		this.low = resource.getLow();
		this.high = resource.getHigh();
	}

}
