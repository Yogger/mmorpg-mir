package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.beauty.model.BeautyGirl;
import com.mmorpg.mir.model.beauty.model.BeautyGirlPool;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.gameobjects.Player;

public class BeautyGirlLevelCondition extends AbstractCoreCondition {

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

		BeautyGirlPool pool = player.getBeautyGirlPool();
		if (!pool.isActive(this.code)) {
			throw new ManagedException(ManagedErrorCode.BEAUTY_TARGET_NOT_ACTIVE);
		}

		BeautyGirl girl = pool.getBeautyGirls().get(this.code);
		if (Operator.EQUAL == op) {
			return girl.getLevel() == this.value;
		} else if (Operator.GREATER == op) {
			return girl.getLevel() > this.value;
		} else if (Operator.GREATER_EQUAL == op) {
			return girl.getLevel() >= this.value;
		} else if (Operator.LESS == op) {
			return girl.getLevel() < this.value;
		} else if (Operator.LESS_EQUAL == op) {
			return girl.getLevel() <= this.value;
		}
		return false;
	}

	@Override
	protected void init(CoreConditionResource resource) {
		super.init(resource);
		this.op = resource.getOperatorEnum();
	}
}
