package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.gameobjects.Player;

public class PackItemCountCondition extends AbstractCoreCondition {

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

		long itemCount = player.getPack().getItemSizeByKey(this.code);

		if (op == Operator.EQUAL) {
			return itemCount == this.value;
		} else if (op == Operator.GREATER) {
			if (itemCount <= this.value) {
				throw new ManagedException(ManagedErrorCode.NOT_ENOUGH_ITEM);
			}
			return true;
		} else if (op == Operator.GREATER_EQUAL) {
			if (itemCount < this.value) {
				throw new ManagedException(ManagedErrorCode.NOT_ENOUGH_ITEM);
			}
			return true;
		} else if (op == Operator.LESS) {
			return itemCount < this.value;
		} else if (op == Operator.LESS_EQUAL) {
			return itemCount <= this.value;
		}
		return false;
	}

	@Override
	protected void init(CoreConditionResource resource) {
		super.init(resource);
		this.op = resource.getOperatorEnum();
	}
}
