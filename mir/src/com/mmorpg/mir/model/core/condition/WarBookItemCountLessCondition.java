package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.warbook.WarbookConfig;
import com.mmorpg.mir.model.warbook.resource.WarbookItemResource;

public class WarBookItemCountLessCondition extends AbstractCoreCondition {

	@Override
	public boolean verify(Object object) {
		Player player = null;
		if (object instanceof Player) {
			player = (Player) object;
		}

		if (player == null) {
			this.errorObject(object);
		}

		int count = 0;
		if (player.getWarBook().getItemCount().containsKey(this.code)) {
			count = player.getWarBook().getItemCount().get(this.code);
		}
		WarbookItemResource resource = WarbookConfig.getInstance().getWarbookItemResourceById(this.code);
		int maxCount = resource.getItemCountLimit().get(player.getWarBook().getGrade()+"");
		if (count < maxCount) {
			return true;
		}
		throw new ManagedException(ManagedErrorCode.WARBOOK_ITEM_COUNT_LIMIT);
	}
}
