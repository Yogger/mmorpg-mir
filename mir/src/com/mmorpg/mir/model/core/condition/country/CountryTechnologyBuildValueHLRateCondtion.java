package com.mmorpg.mir.model.core.condition.country;

import com.mmorpg.mir.model.core.condition.AbstractCoreCondition;
import com.mmorpg.mir.model.core.condition.Operator;
import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.country.manager.CountryManager;
import com.mmorpg.mir.model.country.model.Country;
import com.mmorpg.mir.model.gameobjects.Player;

public class CountryTechnologyBuildValueHLRateCondtion extends AbstractCoreCondition {

	private Operator op;

	@Override
	public boolean verify(Object object) {
		Player player = null;

		int self = 0;
		if (object instanceof Player) {
			player = (Player) object;
			self = player.getCountry().getNewTechnology().getBuildValue();
		} else if (object instanceof Country) {
			self = ((Country) object).getNewTechnology().getBuildValue();
		} else if (null == player) {
			this.errorObject(object);
		}
		
		int buildValueMax = CountryManager.getInstance().getMaxBuildValueAmongCountries();
		int buildValueMin = self <= 0? 1 : self;
		
		int rate = buildValueMax / buildValueMin;
		if (Operator.EQUAL == op) {
			return rate == this.value;
		} else if (Operator.GREATER == op) {
			return rate > this.value;
		} else if (Operator.GREATER_EQUAL == op) {
			return rate >= this.value;
		} else if (Operator.LESS == op) {
			return rate < this.value;
		} else if (Operator.LESS_EQUAL == op) {
			return rate <= this.value;
		}

		return false;
	}

	@Override
	protected void init(CoreConditionResource resource) {
		super.init(resource);
		this.op = resource.getOperatorEnum();
	}

}
