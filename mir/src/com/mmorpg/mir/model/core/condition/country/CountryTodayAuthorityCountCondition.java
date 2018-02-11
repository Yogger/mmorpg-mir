package com.mmorpg.mir.model.core.condition.country;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.condition.AbstractCoreCondition;
import com.mmorpg.mir.model.country.model.Official;
import com.mmorpg.mir.model.gameobjects.Player;

/**
 * 官职权限使用次数
 * 
 * @author Kuang Hao
 * @since v1.0 2014-8-21
 * 
 */
public class CountryTodayAuthorityCountCondition extends AbstractCoreCondition {

	@Override
	public boolean verify(Object object) {
		Official offical = null;
		if (object instanceof Player) {
			Player player = (Player) object;
			offical = player.getCountry().getPlayerOffical(player.getObjectId());
			if (offical == null) {
				if (!player.getCountry().getCourt().isGurad(player.getObjectId())) {
					throw new ManagedException(ManagedErrorCode.COUNTRY_TODAY_NOT_AUTHORITY);
				}
			}
		}
		if (object instanceof Official) {
			offical = (Official) object;
		}
		if (offical == null) {
			this.errorObject(object);
		}

		if (offical.getUseAuthorityHistory(code) < value) {
			return true;
		}

		throw new ManagedException(ManagedErrorCode.COUNTRY_TODAY_NOT_AUTHORITY);
	}
}
