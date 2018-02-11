package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.achievement.model.AchievementCondition;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.core.condition.quest.QuestCondition;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.nickname.model.NicknameCondition;
import com.mmorpg.mir.model.quest.model.Quest;
import com.mmorpg.mir.model.vip.event.VipEvent;

public class VipLevelCondition extends AbstractCoreCondition implements NicknameCondition, AchievementCondition,
		QuestCondition {

	private int low;
	private int high;

	@Override
	public boolean verify(Object object) {
		Player player = null;

		if (object instanceof Player) {
			player = (Player) object;
		}

		if (object instanceof Quest) {
			Quest quest = (Quest) object;
			player = quest.getOwner();
		}

		if (player == null) {
			errorObject(object);
		}

		boolean ret = true;
		if (this.high != 0 && this.low != 0) {
			ret = player.getVip().getLevel() >= this.low && player.getVip().getLevel() <= this.high;
		} else if (code != null) {
			if (code.trim().equals("<=")) {
				ret = player.getVip().getLevel() <= value;
			} else if (code.trim().equals(">=")) {
				ret = player.getVip().getLevel() >= value;
			} else if (code.trim().equals("=")) {
				ret = player.getVip().getLevel() == value;
			}
		}
		if (!ret) {
			throw new ManagedException(ManagedErrorCode.VIP_CONDITION_NOT_SATISFY);
		}

		return ret;

	}

	@Override
	protected void init(CoreConditionResource resource) {
		super.init(resource);
		this.low = resource.getLow();
		this.high = resource.getHigh();
	}

	@Override
	public Class<?>[] getNicknameEvent() {
		return new Class<?>[] { VipEvent.class };
	}

	@Override
	public Class<?>[] getAchievementEvent() {
		return new Class<?>[] { VipEvent.class };
	}

	@Override
	public Class<?>[] getEvent() {
		return new Class<?>[] { VipEvent.class };
	}

}
