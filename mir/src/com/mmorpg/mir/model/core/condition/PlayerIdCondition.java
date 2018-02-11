package com.mmorpg.mir.model.core.condition;

import java.util.ArrayList;

import com.mmorpg.mir.model.gameobjects.Player;
import com.windforce.common.utility.JsonUtils;

public class PlayerIdCondition extends AbstractCoreCondition {

	@Override
	@SuppressWarnings("unchecked")
	public boolean verify(Object object) {
		Player player = null;
		if(object instanceof Player){
			 player = (Player) object;
		}
		if(player == null){
			this.errorObject(object);
		}
		ArrayList<Long> ids = JsonUtils.string2Collection(code, ArrayList.class, Long.class);
		if (ids.contains(player.getObjectId())) {
			return true;
		}
		return false;
	}

}
