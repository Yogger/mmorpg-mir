package com.mmorpg.mir.model.value;

import com.mmorpg.mir.model.gameobjects.Player;

public interface IValueManager {
	public Object getObject(Object object, String formula);

	public Object getVipValue(Player player, String formula);
}
