package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.welfare.event.OnlineRewardEvent;

/**
 * 在线奖励已领取次数
 * 
 * @author 37.com
 * 
 */
public class OnlineRewardCountCondition extends AbstractCoreCondition implements GiftCollectCondition {

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
		int rewardCount = player.getWelfare().getOnlineReward().getRewardedList().size();
		if (op == Operator.EQUAL) {
			return rewardCount == this.value;
		} else if (op == Operator.GREATER_EQUAL) {
			return rewardCount >= this.value;
		} else if (op == Operator.LESS_EQUAL) {
			return rewardCount <= this.value;
		}
		return false;
	}

	@Override
	protected void init(CoreConditionResource resource) {
		super.init(resource);
		this.op = resource.getOperatorEnum();
	}

	@Override
	public Class<?>[] getGiftCollectEvent() {
		return new Class<?>[] { OnlineRewardEvent.class };
	}
}
