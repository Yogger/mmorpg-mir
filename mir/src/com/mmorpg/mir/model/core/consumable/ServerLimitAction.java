package com.mmorpg.mir.model.core.consumable;

import com.mmorpg.mir.model.core.condition.CoreConditionType;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.serverstate.ServerState;

public class ServerLimitAction extends AbstractCoreAction {

	@Override
	public boolean verify(Object object) {
		return CoreConditionType.createServerLimitCondition(code).verify(object);
	}

	@Override
	public void act(Object object, ModuleInfo moduleInfo) {
		ServerState.getInstance().addLimit(code);
		/** TODO: log sth?
			Player player = null;
			if (object instanceof Player) {
				player = (Player) object;
		}*/
	}

	@Override
	public AbstractCoreAction clone() {
		ServerLimitAction action = new ServerLimitAction();
		action.code = this.code;
		return action;
	}

}
