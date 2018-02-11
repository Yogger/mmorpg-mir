package com.mmorpg.mir.model.core.condition.country;

import com.mmorpg.mir.model.core.condition.AbstractCoreCondition;
import com.mmorpg.mir.model.core.condition.Operator;
import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.country.model.countryact.HiddenMissionType;
import com.mmorpg.mir.model.gameobjects.Player;

public class CountryMissionFinishCountCondition extends AbstractCoreCondition {

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

		HiddenMissionType type = HiddenMissionType.valueOf(this.code);
		int finishCount = player.getPlayerCountryHistory().getMissionFinishCount(type);
		if (op == Operator.EQUAL) {
			return finishCount == this.value;
		} else if (op == Operator.GREATER) {
			return finishCount > this.value;
		} else if (op == Operator.GREATER_EQUAL) {
			return finishCount >= this.value;
		} else if (op == Operator.LESS) {
			return finishCount < this.value;
		} else if (op == Operator.LESS_EQUAL) {
			return finishCount <= this.value;
		}
		return false;
	}

	@Override
	protected void init(CoreConditionResource resource) {
		super.init(resource);
		this.op = resource.getOperatorEnum();
	}

}
