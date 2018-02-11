package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;

public class HasSpecificEffectCondition extends AbstractCoreCondition {

	@Override
	public boolean verify(Object object) {
		Player player = null;
		
		if (object instanceof Player) {
			player= (Player) object;
		}
		
		if (player == null) {
			this.errorObject(object);
		}
		
		if (!player.getEffectController().containsSkill(value))
			throw new ManagedException(ManagedErrorCode.SPECIFIC_SKILL_BUFF_INEXISTENCE);
		
		return true;
	}

}
