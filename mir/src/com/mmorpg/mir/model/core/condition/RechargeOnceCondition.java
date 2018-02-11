package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.achievement.model.AchievementCondition;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.nickname.model.NicknameCondition;
import com.mmorpg.mir.model.purse.event.RechargeRewardEvent;

public class RechargeOnceCondition extends AbstractCoreCondition implements NicknameCondition,AchievementCondition{

	@Override
	public boolean verify(Object object) {
		Player player = null;
		
		if (object instanceof Player) {
			player = (Player) object;
		}
		
		if (player == null) {
			this.errorObject(object);
		}
		
		return player.getVip().getMaxCharge() >= value;
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
