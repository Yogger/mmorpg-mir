package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.item.core.ItemManager;

public class ItemCondition extends AbstractCoreCondition {

	@Override
	public boolean verify(Object object) {
		Player player = null;
		if (object instanceof Player) {
			player = (Player) object;

		}
		if(player == null){
			this.errorObject(object);
		}
		if (player.getPack().getItemSizeByKey(code) < value) {
			throw new ManagedException(ItemManager.getInstance().getResource(code).getErrorCode());
		}
		return true;
	}

	@Override
	protected boolean check(AbstractCoreCondition condition) {
		return code.equals(condition.code);
	}
}
