package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.country.manager.CountryManager;
import com.mmorpg.mir.model.country.model.CountryId;

public class CountryQuestSelectCondition extends AbstractCoreCondition {

	private CountryId countryId;
	private int powerLevel;

	@Override
	public boolean verify(Object object) {
		return CountryManager.getInstance().getCountries().get(countryId).getCountryLevel() == powerLevel;
	}

	@Override
	protected boolean check(AbstractCoreCondition condition) {
		return false;
	}

	@Override
	protected void init(CoreConditionResource resource) {
		super.init(resource);
		this.powerLevel = resource.getValue();
		this.countryId = CountryId.valueOf(Integer.valueOf(resource.getCode()));
	}
	
}
