package com.mmorpg.mir.model.core.consumable;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.log.ModuleInfo;

public class HpAction extends AbstractCoreAction {

	@Override
	public void act(Object object, ModuleInfo moduleInfo) {
		Player player = (Player) object;
		player.getLifeStats().reduceHp(value, null, 0);
	}

	@Override
	public boolean verify(Object object) {
		Player player = (Player) object;
		if (player.getLifeStats().getCurrentHp() < value) {
			throw new ManagedException(ManagedErrorCode.NOT_ENOUGH_HP);
		}
		return true;
	}

	@Override
	public AbstractCoreAction clone() {
		HpAction action = new HpAction();
		action.setCode(code);
		action.setValue(value);
		return action;
	}
}
