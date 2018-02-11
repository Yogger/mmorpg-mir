package com.mmorpg.mir.model.core.condition.country;

import com.mmorpg.mir.model.core.condition.AbstractCoreCondition;
import com.mmorpg.mir.model.country.model.Official;
import com.mmorpg.mir.model.country.resource.ConfigValueManager;
import com.mmorpg.mir.model.gameobjects.Player;

/**
 * 国民召集次数限制
 * 
 * @author Kuang Hao
 * @since v1.0 2014-8-21
 * 
 */
public class CountryCalltogeterCountCondition extends AbstractCoreCondition {

	private boolean open = true;

	@Override
	public boolean verify(Object object) {
		Player player = null;
		if (object instanceof Player) {
			player = (Player) object;
		}
		if (player == null) {
			this.errorObject(object);
		}
		if (!open) {
			return true;
		}
		Official king = player.getCountry().getCourt().getKing();
		if (king == null) {
			return false;
		}

		int v = value;
		if (player.getCountry().isWeakCountry()) {
			v += ConfigValueManager.getInstance().WEAK_COUNTRY_CALL_COUNT.getValue();
		}
		if (king.getCallCount(player.getObjectId()) < v) {
			return true;
		}

		return false;
		// throw new ManagedException(ManagedErrorCode.CALLTOGETHER_COUNT);
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}
}
