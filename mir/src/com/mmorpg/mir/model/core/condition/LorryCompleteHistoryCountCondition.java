package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.achievement.model.AchievementCondition;
import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.welfare.event.ExpressEvent;

public class LorryCompleteHistoryCountCondition extends AbstractCoreCondition implements AchievementCondition {

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

		int lorryCount = player.getExpress().getLorryCompleteHistoryCount();
		if (op == Operator.LESS) {
			return lorryCount < this.value;
		} else if (op == Operator.LESS_EQUAL) {
			return lorryCount <= this.value;
		} else if (op == Operator.GREATER) {
			return lorryCount > this.value;
		} else if (op == Operator.GREATER_EQUAL) {
			return lorryCount >= this.value;
		} else if (op == Operator.EQUAL) {
			return lorryCount == this.value;
		}
		return false;
	}
	
	@Override
	public Class<?>[] getAchievementEvent() {
		return new Class<?>[] { ExpressEvent.class };
	}

	@Override
	protected void init(CoreConditionResource resource) {
		super.init(resource);
		this.op = resource.getOperatorEnum();
	}
}
