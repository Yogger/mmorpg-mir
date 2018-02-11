package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.core.condition.quest.QuestCondition;
import com.mmorpg.mir.model.country.event.WeakCountryEvent;
import com.mmorpg.mir.model.country.model.Country;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.quest.model.Quest;

public class CountryPowerLevelCondition extends AbstractCoreCondition implements QuestCondition {

	private Operator op;

	@Override
	public boolean verify(Object object) {
		Country country = null;
		Player player = null;
		
		if (object instanceof Country) {
			country = (Country) object;
		}

		if (object instanceof Player) {
			player = (Player) object;
			country = player.getCountry();
		}

		if (object instanceof Quest) {
			Quest quest = (Quest) object;
			player = quest.getOwner();
			country = player.getCountry();
		}

		if (country == null) {
			this.errorObject(object);
		}

		if (op == Operator.GREATER_EQUAL) {
			return country.getCountryLevel() >= value;
		} else if (op == Operator.GREATER) {
			return country.getCountryLevel() > value;
		} else if (op == Operator.EQUAL) {
			return country.getCountryLevel() == value;
		} else if (op == Operator.LESS_EQUAL) {
			return country.getCountryLevel() <= value;
		} else if (op == Operator.LESS) {
			return country.getCountryLevel() < value;
		}

		return false;
	}

	@Override
	protected boolean check(AbstractCoreCondition condition) {
		return false;
	}

	@Override
	protected void init(CoreConditionResource resource) {
		super.init(resource);
		this.op = resource.getOperatorEnum();
	}

	@Override
	public Class<?>[] getEvent() {
		return new Class<?>[] { WeakCountryEvent.class };
	}
}
