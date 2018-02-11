package com.mmorpg.mir.model.core.condition.gangofwar;

import com.mmorpg.mir.model.core.condition.AbstractCoreCondition;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gangofwar.controller.Phase;
import com.mmorpg.mir.model.gangofwar.manager.GangOfWar;
import com.mmorpg.mir.model.gangofwar.manager.GangOfWarManager;

/**
 * 家族战攻防守方
 * 
 * @author Kuang Hao
 * @since v1.0 2014-11-11
 * 
 */
public class GangOfWarWinCondition extends AbstractCoreCondition {

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
		if (value == 0) {
			if (gangOfWar.getDefendGang() == player.getGang()) {
				return true;
			}
		} else {
			if (gangOfWar.getPhase() == Phase.ATTACK || gangOfWar.getDefendGang() != player.getGang()) {
				return true;
			}
		}

		return false;
	}

	@Override
	protected boolean check(AbstractCoreCondition condition) {
		return false;
	}

}
