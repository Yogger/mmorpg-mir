package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.achievement.model.AchievementCondition;
import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.welfare.event.BossDieEvent;

public class HuntBossCountCondition extends AbstractCoreCondition implements AchievementCondition{

	private Operator op;
	
	@Override
	public boolean verify(Object object) {
		Player player = null;

		if (object instanceof Player) {
			player = (Player) object;
		}

		if (player == null) {
			this.errorObject(object);
		}

		return player.getDropHistory().verifyBossHunt(code, op, value);
	}
	
	@Override
	protected void init(CoreConditionResource resource) {
		super.init(resource);
		op = resource.getOperatorEnum();
	}
	
	@Override
	protected boolean check(AbstractCoreCondition condition) {
		return false;
	}

	@Override
	public Class<?>[] getAchievementEvent() {
		return new Class<?>[]{BossDieEvent.class};
	}

}
