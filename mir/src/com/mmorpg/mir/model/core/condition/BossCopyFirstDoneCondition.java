package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.gameobjects.Player;

public class BossCopyFirstDoneCondition extends AbstractCoreCondition {

	@Override
	public boolean verify(Object object) {
		Player player = null;
		
		if (object instanceof Player) {
			player = (Player) object;
		}
		
		if (player == null) {
			this.errorObject(object);
		}
		
		if (player.getCopyHistory().getCopyFirstDoneTime().containsKey(code)) {
			long time = player.getCopyHistory().getCopyFirstDoneTime().get(code);
			if (Math.abs(System.currentTimeMillis() - time) < value) {
				return true;
			} else {
				return false;
			}
		}
		
		return true;
	}

}
