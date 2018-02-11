package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.achievement.model.AchievementCondition;
import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.nickname.model.NicknameCondition;
import com.mmorpg.mir.model.welfare.event.TempleEvent;

public class BrickColorTotalCountCondition extends AbstractCoreCondition implements NicknameCondition,AchievementCondition{

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
		int color = Integer.parseInt(this.code);
		int count = this.value;

		Integer currentCount = player.getTempleHistory().getHistoryColorCount().get(color);
		if (currentCount == null) {
			return false;
		}
		if (op == Operator.GREATER_EQUAL) {
			return currentCount >= count;
		} else if (op == Operator.GREATER) {
			return currentCount > count;
		} else if (op == Operator.EQUAL) {
			return currentCount == count;
		} else if (op == Operator.LESS_EQUAL) {
			return currentCount <= count;
		} else if (op == Operator.LESS) {
			return currentCount < count;
		}
		return false;
	}

	@Override
	protected void init(CoreConditionResource resource) {
		super.init(resource);
		this.op = resource.getOperatorEnum();
	}

	@Override
	public Class<?>[] getNicknameEvent() {
		return new Class<?>[] { TempleEvent.class };
	}

	@Override
	public Class<?>[] getAchievementEvent() {
		return new Class<?>[] { TempleEvent.class };
	}
}
