package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.gameobjects.Player;

public class PlayerOperatorCondition extends AbstractCoreCondition {
	
	private Operator op;
	
	@Override
	protected void init(CoreConditionResource resource) {
		super.init(resource);
		if (resource.getOperator() != null) {
			this.op = resource.getOperatorEnum();
		}
	}
	
	@Override
	public boolean verify(Object object) {
		Player player = null;

		if (object instanceof Player) {
			player = (Player) object;
		}

		if (player == null) {
			this.errorObject(object);
		}
		
		boolean result = player.getPlayerEnt().getOp().equals(code);
		
		return op == null ? result : !result;
	}

}
