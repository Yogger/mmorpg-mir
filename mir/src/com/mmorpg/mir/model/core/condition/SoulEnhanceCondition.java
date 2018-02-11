package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.soul.core.SoulManager;
import com.mmorpg.mir.model.soul.resource.SoulResource;

public class SoulEnhanceCondition extends AbstractCoreCondition {

	@Override
	public boolean verify(Object object) {
		Player player = null;

		if (object instanceof Player) {
			player = (Player) object;
		}

		if (null == player) {
			this.errorObject(object);
		}

		Integer count = player.getSoul().getEnhanceItemCount().get(this.code);

		if (count == null) {
			count = 0;
		}

		SoulResource resource = SoulManager.getInstance().getSoulResource(player.getSoul().getLevel());
		Integer maxCount = resource.getEnhanceItemCount().get(this.code);
		if (maxCount == null) {
			maxCount = 0;
		}

		return count < maxCount;

	}

}
