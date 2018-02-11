package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.quest.model.Quest;

public class CopyEnterCountExCondition extends AbstractCoreCondition {

	private Operator op;

	@Override
	public boolean verify(Object object) {
		Player player = null;
		if (object instanceof Player) {
			player = (Player) object;
		}
		if (object instanceof Quest) {
			player = ((Quest) object).getOwner();
		}
		if (player == null) {
			this.errorObject(object);
		}
		int count = 0;
		if (player.getCopyHistory().getTodayEnterHistory().containsKey(code)) {
			count += player.getCopyHistory().getTodayEnterHistory().get(code);
		}
		if (player.getCopyHistory().getBuyCounts().containsKey(code)) {
			count -= player.getCopyHistory().getBuyCounts().get(code);
		}

		if (Operator.EQUAL == op) {
			return count == this.value;
		} else if (Operator.GREATER == op) {
			if (count <= this.value) {
				throw new ManagedException(ManagedErrorCode.NOT_ENOUGH_COPY);
			}
			return true;
		} else if (Operator.GREATER_EQUAL == op) {
			if (count < this.value) {
				throw new ManagedException(ManagedErrorCode.NOT_ENOUGH_COPY);
			}
			return true;
		} else if (Operator.LESS == op) {
			return count < this.value;
		} else if (Operator.LESS_EQUAL == op) {
			return count <= this.value;
		}
		return false;
	}

	@Override
	protected void init(CoreConditionResource resource) {
		super.init(resource);
		this.op = resource.getOperatorEnum();
	}
}
