package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.horse.manager.HorseManager;
import com.mmorpg.mir.model.horse.model.Horse;
import com.mmorpg.mir.model.horse.resource.HorseResource;

public class HorseEnhanceCondition extends AbstractCoreCondition {

	@Override
	public boolean verify(Object object) {
		Player player = null;

		if (object instanceof Player) {
			player = (Player) object;
		}

		if (null == player) {
			this.errorObject(object);
		}

		Horse horse = player.getHorse();
		Integer count = horse.getEnhanceItemCount().get(this.code);

		if (count == null) {
			count = 0;
		}

		HorseResource resource = HorseManager.getInstance().getHorseResource(horse.getGrade());
		Integer maxCount = resource.getEnhanceItemCount().get(this.code);
		if (maxCount == null) {
			maxCount = 0;
		}

		return count < maxCount;

	}

	@Override
	protected void init(CoreConditionResource resource) {
		super.init(resource);
	}
}
