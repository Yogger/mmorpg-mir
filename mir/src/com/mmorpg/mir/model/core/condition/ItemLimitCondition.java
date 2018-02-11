package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.gameobjects.Player;

/**
 * code 配物品的ID value 配物品限制的数量
 * 
 * @author zouqiang
 */
public class ItemLimitCondition extends AbstractCoreCondition {

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
		Integer totalCount = player.getPack().getTotalLimit().get(code);
		int currentCount = (totalCount == null? 0: totalCount);
		
		if (op == Operator.GREATER_EQUAL) {
			return currentCount >= value;
		} else if (op == Operator.GREATER) {
			return currentCount > value;
		} else if (op == Operator.EQUAL) {
			return currentCount == value;
		} else if (op == Operator.LESS_EQUAL) {
			return currentCount <= value;
		} else if (op == Operator.LESS) {
			return currentCount < value;
		}
		throw new ManagedException(ManagedErrorCode.ITEM_USE_LIMIT);
	}

	@Override
	protected void init(CoreConditionResource resource) {
		super.init(resource);
		this.op = resource.getOperatorEnum();
	}

}
