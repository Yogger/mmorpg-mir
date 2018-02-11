package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.seal.SealConfig;
import com.mmorpg.mir.model.seal.resource.SealItemResource;

public class SealItemCountLessCondition extends AbstractCoreCondition {

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
		if (player.getSeal().getItemCount().containsKey(this.code)) {
			count = player.getSeal().getItemCount().get(this.code);
		}
		SealItemResource resource = SealConfig.getInstance().getSealItemResource(this.code);
		int maxCount = resource.getItemCountLimit().get(player.getSeal().getGrade()+"");
		if (count < maxCount) {
			return true;
		}
		throw new ManagedException(ManagedErrorCode.SEAL_ITEM_COUNT_LIMIT);
	}

}
