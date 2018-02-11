package com.mmorpg.mir.model.core.condition;

import java.util.Date;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.gameobjects.Player;

public class TodayRechargeCondition extends AbstractCoreCondition {

	@Override
	public boolean verify(Object object) {
		Player player = null;

		if (object instanceof Player) {
			player = (Player) object;
		}

		if (player == null) {
			this.errorObject(object);
		}

		if (player.getVip().dayTotalCharge(new Date()) >= value) {
			return true;
		}

		throw new ManagedException(ManagedErrorCode.RECHARGE_LESS);
	}

	@Override
	protected void init(CoreConditionResource resource) {
		super.init(resource);
	}

	@Override
	protected boolean check(AbstractCoreCondition condition) {
		return false;
	}
}
