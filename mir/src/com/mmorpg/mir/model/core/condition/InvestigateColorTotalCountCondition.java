package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.achievement.model.AchievementCondition;
import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.nickname.model.NicknameCondition;
import com.mmorpg.mir.model.welfare.event.InvestigateEvent;

/**
 * 某种颜色情报历史获得次数
 * 
 * @author 37.com
 * 
 */
public class InvestigateColorTotalCountCondition extends AbstractCoreCondition implements NicknameCondition,AchievementCondition{

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
		Integer colorCount = player.getInvestigate().getHistoryColorCount().get(color);
		if (colorCount == null) {
			return false;
		}

		if (op == Operator.GREATER_EQUAL) {
			return colorCount >= this.value;
		} else if (op == Operator.GREATER) {
			return colorCount > this.value;
		} else if (op == Operator.EQUAL) {
			return colorCount == this.value;
		} else if (op == Operator.LESS_EQUAL) {
			return colorCount <= this.value;
		} else if (op == Operator.LESS) {
			return colorCount < this.value;
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
		return new Class<?>[] { InvestigateEvent.class };
	}

	@Override
	public Class<?>[] getAchievementEvent() {
		return new Class<?>[] { InvestigateEvent.class };
	}

}
