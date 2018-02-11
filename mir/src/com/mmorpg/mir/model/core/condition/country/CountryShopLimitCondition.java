package com.mmorpg.mir.model.core.condition.country;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.condition.AbstractCoreCondition;
import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.gameobjects.Player;

public class CountryShopLimitCondition extends AbstractCoreCondition {

	private int buyCount;

	@Override
	public boolean verify(Object object) {
		Player player = null;
		if (object instanceof Player) {
			player = (Player) object;
		}
		if (player == null) {
			this.errorObject(object);
		}
		int buyedCount = player.getCountry().getCountryShop().getCount(player.getObjectId(), code);
		if ((buyedCount + buyCount) <= value) {
			return true;
		}

		throw new ManagedException(ManagedErrorCode.COUNTRY_SHOP_ENOUGH);
	}

	@Override
	protected void init(CoreConditionResource resource) {
		super.init(resource);
	}

	public int getBuyCount() {
		return buyCount;
	}

	public void setBuyCount(int buyCount) {
		this.buyCount = buyCount;
	}

	@Override
	protected void calculate(int num) {
		buyCount = num;
	}
}
