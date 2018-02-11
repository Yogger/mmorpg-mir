package com.mmorpg.mir.model.core.condition;

import java.util.ArrayList;

import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.gameobjects.Player;
import com.windforce.common.utility.JsonUtils;

public class PlayerNameCondition extends AbstractCoreCondition {

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
		ArrayList<String> ids = JsonUtils.string2Collection(code, ArrayList.class, String.class);
		if (ids.contains(player.getName())) {
			return true;
		}
		return false;
	}

	public static void main(String[] args) {
		ArrayList<String> ids = new ArrayList<String>();
		ids.add("name1");
		ids.add("name2");
		ids.add("name3");
		CoreConditionResource r = new CoreConditionResource();
		r.setCode(JsonUtils.object2String(ids));
		System.out.println(JsonUtils.object2String(r));
	}

}
