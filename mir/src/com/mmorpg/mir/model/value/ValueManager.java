package com.mmorpg.mir.model.value;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.gameobjects.Player;

@Component
public class ValueManager implements IValueManager{

	public Object getObject(Object object, String formula) {
		String head = ValueUtils.getHead(formula);
		if (head.equals("vip")) {
			return getVipValue((Player) object, formula);
		}
		return null;
	}

	public Object getVipValue(Player player, String formula) {
		return player.getVip().getResource().getField(ValueUtils.getBody(formula, 1));
	}

}
