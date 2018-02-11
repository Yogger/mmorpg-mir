package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.gameobjects.Player;
import com.windforce.common.utility.DateUtils;

public class LevelBeforeTimeCondition extends AbstractCoreCondition {

	private long endTime;

	@Override
	public boolean verify(Object object) {
		Player player = null;
		if (object instanceof Player) {
			player = (Player) object;

		}
		if (player == null) {
			this.errorObject(object);
		}
		if (player.getLevelLog().levelUpBefore(value, endTime)) {
			return true;
		}
		return false;
	}

	@Override
	protected void init(CoreConditionResource resource) {
		super.init(resource);
		this.endTime = DateUtils.string2Date(resource.getEndDate(), DateUtils.PATTERN_DATE_TIME).getTime();
	}

	@Override
	protected boolean check(AbstractCoreCondition condition) {
		return false;
	}
}
