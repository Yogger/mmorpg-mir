package com.mmorpg.mir.model.core.condition.gangofwar;

import com.mmorpg.mir.model.core.condition.AbstractCoreCondition;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gangofwar.controller.GangOfWarCamp;

/**
 * 家族战攻防守方
 * 
 * @author Kuang Hao
 * @since v1.0 2014-11-11
 * 
 */
public class GangOfWarAttackDefendCondition extends AbstractCoreCondition {

	@Override
	public boolean verify(Object object) {
		Player player = null;
		if(object instanceof Player){
			player = (Player) object;
		}
		if(player == null){
			this.errorObject(object);
		}
		if (player.getController() instanceof GangOfWarCamp) {
			return ((GangOfWarCamp) player.getController()).getCamps().getValue() == value;
		}
		return true;
	}

	@Override
	protected boolean check(AbstractCoreCondition condition) {
		return false;
	}

}
