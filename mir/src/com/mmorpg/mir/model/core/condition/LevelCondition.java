package com.mmorpg.mir.model.core.condition;


import java.util.Map;

import com.mmorpg.mir.model.achievement.model.AchievementCondition;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.core.condition.quest.QuestCondition;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.event.LevelUpEvent;
import com.mmorpg.mir.model.quest.model.Quest;
import com.mmorpg.mir.model.skill.model.SkillCondition;
import com.mmorpg.mir.model.trigger.model.TriggerContextKey;

/**
 * 玩家等级 区间 条件
 */
public class LevelCondition extends AbstractCoreCondition implements QuestCondition, SkillCondition,AchievementCondition {

	private int low;
	private int high;

	@SuppressWarnings("unchecked")
	@Override
	public boolean verify(Object object) {
		Player player = null;
		if (object instanceof Player) {
			player = (Player) object;
		} else if (object instanceof Quest) {
			player = ((Quest) object).getOwner();
		} else if (object instanceof Map) {
			Map<String, Object> map = (Map<String, Object>) object;
			player = (Player) map.get(TriggerContextKey.PLAYER);
		}
		if (player == null) {
			this.errorObject(object);
		}
		if (player.getPlayerEnt().getLevel() >= this.low && player.getPlayerEnt().getLevel() <= high) {
			return true;
		}
		if (isThrowException()) {
			throw new ManagedException(ManagedErrorCode.LEVEL_NOT_ENOUGH);
		} else {
			return false;
		}
	}

	@Override
	protected AbstractCoreCondition doAdd(AbstractCoreCondition condition) {
		if (this.value < condition.getValue()) {
			this.value = condition.getValue();
		}
		return null;
	}

	@Override
	protected void calculate(int num) {
		// nothing todo
	}

	@Override
	protected void init(CoreConditionResource resource) {
		super.init(resource);
		this.low = resource.getLow();
		this.high = resource.getHigh();
	}

	@Override
	public Class<?>[] getEvent() {
		return new Class<?>[] { LevelUpEvent.class };
	}

	public int getLow() {
		return low;
	}

	public void setLow(int low) {
		this.low = low;
	}

	public int getHigh() {
		return high;
	}

	public void setHigh(int high) {
		this.high = high;
	}

	@Override
	public Class<?>[] getSkillEvent() {
		return new Class<?>[] { LevelUpEvent.class };
	}

	@Override
	public Class<?>[] getAchievementEvent() {
		return new Class<?>[]{LevelUpEvent.class};
	}

}
