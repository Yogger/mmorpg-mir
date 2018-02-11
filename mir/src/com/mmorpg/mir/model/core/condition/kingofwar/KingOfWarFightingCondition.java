package com.mmorpg.mir.model.core.condition.kingofwar;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.condition.AbstractCoreCondition;
import com.mmorpg.mir.model.kingofwar.manager.KingOfWarManager;

/**
 * 咸阳战进行中
 * 
 * @author Kuang Hao
 * @since v1.0 2014-11-24
 * 
 */
public class KingOfWarFightingCondition extends AbstractCoreCondition {

	@Override
	public boolean verify(Object object) {

		boolean warring = value == 0 ? false : true;
		if (KingOfWarManager.getInstance().isWarring() == warring) {
			return true;
		}
		throw new ManagedException(ManagedErrorCode.KINGOFWAR_FIGHTING);
	}

	@Override
	protected boolean check(AbstractCoreCondition condition) {
		return false;
	}

}
