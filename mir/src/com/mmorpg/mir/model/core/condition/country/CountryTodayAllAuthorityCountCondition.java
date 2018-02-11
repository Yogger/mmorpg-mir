package com.mmorpg.mir.model.core.condition.country;

import com.mmorpg.mir.model.core.condition.AbstractCoreCondition;
import com.mmorpg.mir.model.country.manager.CountryManager;
import com.mmorpg.mir.model.gameobjects.Player;

/**
 * 官职权限使用次数
 * 
 * @author Kuang Hao
 * @since v1.0 2014-8-21
 * 
 */
public class CountryTodayAllAuthorityCountCondition extends AbstractCoreCondition {

	@Override
	public boolean verify(Object object) {
		Player player = null;
		if (object instanceof Player) {
			player = (Player) object;
		}
		if (player == null) {
			this.errorObject(object);
		}
		if (!player.getCountry().getCourt().getUseAuthorityHistory().containsKey(code)) {
			return true;
		}

		if (player.getCountry().getCourt().getUseAuthorityHistory().get(code) < value) {
			return true;
		}

		// throw new
		// ManagedException(ManagedErrorCode.COUNTRY_TODAY_NOT_AUTHORITY);
		return CountryManager.getInstance().throwManagedException(code);
	}
}
