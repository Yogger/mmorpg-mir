package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.achievement.model.AchievementCondition;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.event.KillPlayerEvent;

public class KillKingCondition extends AbstractCoreCondition implements AchievementCondition {

	@Override
	public boolean verify(Object object) {
		Player player = null;

		if (object instanceof Player) {
			player = (Player) object;
		}

		if (player == null) {
			this.errorObject(object);
		}

		if (player.getRp().getKillKingCount() >= value) {
			return true;
		}
		return false;
	}

	@Override
	protected boolean check(AbstractCoreCondition condition) {
		return false;
	}

	@Override
	public Class<?>[] getAchievementEvent() {
		return new Class<?>[] { KillPlayerEvent.class };
	}

}
