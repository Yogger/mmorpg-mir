package com.mmorpg.mir.model.core.condition;

import java.util.Map;

import com.mmorpg.mir.model.achievement.model.AchievementCondition;
import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.nickname.model.NicknameCondition;
import com.mmorpg.mir.model.purse.event.RechargeRewardEvent;
import com.mmorpg.mir.model.trigger.model.TriggerContextKey;

public class TotalRechargeCondition extends AbstractCoreCondition implements NicknameCondition,AchievementCondition{

	private Operator op;
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean verify(Object object) {
		Player player = null;
		
		if (object instanceof Player) {
			player = (Player) object;
		}
		
		if (object instanceof Map) {
			player = (Player) ((Map<String, Object>) object).get(TriggerContextKey.PLAYER);
		}
		
		if (player == null) {
			this.errorObject(object);
		}
		
		long totalCharge = player.getVip().getTotalCharge();
		
		if (op == Operator.GREATER_EQUAL) {
			return totalCharge >= value;
		} else if (op == Operator.GREATER) {
			return totalCharge > value;
		} else if (op == Operator.EQUAL) {
			return totalCharge == value;
		} else if (op == Operator.LESS_EQUAL) {
			return totalCharge <= value;
		} else if (op == Operator.LESS) {
			return totalCharge < value;
		}
		
		return false;
	}
	
	@Override
	protected void init(CoreConditionResource resource) {
		super.init(resource);
		if (resource.getOperator() == null || resource.getOperator().isEmpty()) {
			this.op = Operator.GREATER_EQUAL;
		} else {
			this.op = resource.getOperatorEnum();
		}
	}

	@Override
	public Class<?>[] getNicknameEvent() {
		return new Class<?>[] {RechargeRewardEvent.class};
	}

	@Override
	public Class<?>[] getAchievementEvent() {
		return new Class<?>[] {RechargeRewardEvent.class};
	}
}
