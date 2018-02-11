package com.mmorpg.mir.model.core.condition.country;

import com.mmorpg.mir.model.core.condition.AbstractCoreCondition;
import com.mmorpg.mir.model.core.condition.Operator;
import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.country.model.Country;
import com.mmorpg.mir.model.gameobjects.Player;

public class CountryTechnologyGradeCondition extends AbstractCoreCondition {

	private Operator op;

	@Override
	public boolean verify(Object object) {
		Player player = null;

		int grade = 0;
		if (object instanceof Player) {
			player = (Player) object;
			grade = player.getCountry().getNewTechnology().getGrade();
		} else if (object instanceof Country) {
			grade = ((Country) object).getNewTechnology().getGrade();
		} else if (null == player) {
			this.errorObject(object);
		}
		

		if (Operator.EQUAL == op) {
			return grade == this.value;
		} else if (Operator.GREATER == op) {
			return grade > this.value;
		} else if (Operator.GREATER_EQUAL == op) {
			return grade >= this.value;
		} else if (Operator.LESS == op) {
			return grade < this.value;
		} else if (Operator.LESS_EQUAL == op) {
			return grade <= this.value;
		}

		return false;
	}

	@Override
	protected void init(CoreConditionResource resource) {
		super.init(resource);
		this.op = resource.getOperatorEnum();
	}

}
