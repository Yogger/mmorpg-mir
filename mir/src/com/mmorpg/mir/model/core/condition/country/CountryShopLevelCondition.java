package com.mmorpg.mir.model.core.condition.country;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.condition.AbstractCoreCondition;
import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.gameobjects.Player;

public class CountryShopLevelCondition extends AbstractCoreCondition {

	private int low;
	private int high;
	
	@Override
	public boolean verify(Object object) {
		Player player = null;
		
		if (object instanceof Player) {
			player = (Player) object;
		}
		
		if (player == null) {
			this.errorObject(object);
		}
		
		int countryShopLevel = player.getCountry().getCountryShop().getLevel();
		if (countryShopLevel >= this.low && countryShopLevel <= high) {
			return true;
		}
		throw new ManagedException(ManagedErrorCode.COUNTRYSHOP_LEVEL_NOT_SATISFY);
	}
	
	@Override
	protected void init(CoreConditionResource resource) {
		super.init(resource);
		this.low = resource.getLow();
		this.high = resource.getHigh();
	}

}
