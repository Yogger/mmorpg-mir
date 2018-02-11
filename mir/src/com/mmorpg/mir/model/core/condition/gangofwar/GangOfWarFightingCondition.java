package com.mmorpg.mir.model.core.condition.gangofwar;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.condition.AbstractCoreCondition;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gangofwar.manager.GangOfWar;
import com.mmorpg.mir.model.gangofwar.manager.GangOfWarManager;

/**
 * 咸阳战进行中
 * 
 * @author Kuang Hao
 * @since v1.0 2014-11-24
 * 
 */
public class GangOfWarFightingCondition extends AbstractCoreCondition {

	@Override
	public boolean verify(Object object) {
		Player player = null;
		if(object instanceof Player){
			player = (Player) object;
		}
		if(player == null){
			this.errorObject(object);
		}
		GangOfWar gangOfWar = GangOfWarManager.getInstance().getGangOfwars().get(player.getCountry().getId());
		boolean warring = (value == 0 ? false : true);
		if (gangOfWar.isWarring() == warring) {
			return true;
		}
		throw new ManagedException(ManagedErrorCode.GANGOFWAR_FIGHTING);
	}

	@Override
	protected boolean check(AbstractCoreCondition condition) {
		return false;
	}

}
