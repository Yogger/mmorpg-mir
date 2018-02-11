package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.achievement.model.AchievementCondition;
import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.welfare.event.TempleEvent;

public class BrickTotalCountCondition extends AbstractCoreCondition implements AchievementCondition {

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

		int count = player.getTempleHistory().getCountAll();
		if (op == Operator.EQUAL) {
			return count == this.value;
		} else if (op == Operator.GREATER) {
			return count > this.value;
		} else if (op == Operator.GREATER_EQUAL) {
			return count >= this.value;
		} else if (op == Operator.LESS) {
			return count < this.value;
		} else if (op == Operator.LESS_EQUAL) {
			return count <= this.value;
		}
		return false;
	}

	@Override
	protected void init(CoreConditionResource resource) {
		super.init(resource);
		this.op = resource.getOperatorEnum();
	}

	@Override
	public Class<?>[] getAchievementEvent() {
		return new Class<?>[] { TempleEvent.class };
	}
}
