package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.achievement.model.AchievementCondition;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.welfare.event.InvestigateEvent;

/**
 * 刺探完成次数
 * 
 * @author 37.com
 * 
 */
public class InvestigateTotalCountCondition extends AbstractCoreCondition implements AchievementCondition {
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
		int tatalCount = player.getInvestigate().getCountAll();
		if (op == Operator.GREATER_EQUAL) {
			return tatalCount >= this.value;
		} else if (op == Operator.GREATER) {
			return tatalCount > this.value;
		} else if (op == Operator.EQUAL) {
			return tatalCount == this.value;
		} else if (op == Operator.LESS_EQUAL) {
			return tatalCount <= this.value;
		} else if (op == Operator.LESS) {
			return tatalCount < this.value;
		}

		return false;
	}

	@Override
	public Class<?>[] getAchievementEvent() {
		return new Class<?>[] { InvestigateEvent.class };
	}
}
