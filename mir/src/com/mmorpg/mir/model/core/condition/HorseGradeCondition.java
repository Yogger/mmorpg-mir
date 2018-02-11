package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.achievement.model.AchievementCondition;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.condition.quest.QuestCondition;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.horse.event.HorseGradeUpEvent;
import com.mmorpg.mir.model.nickname.model.NicknameCondition;
import com.mmorpg.mir.model.quest.model.Quest;

/**
 * 玩家等级 高于等于 条件
 */
public class HorseGradeCondition extends AbstractCoreCondition implements QuestCondition, NicknameCondition,AchievementCondition{

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
		
		if (object instanceof Quest) {
			player = ((Quest) object).getOwner();
		}
		
		if(player == null){
			this.errorObject(object);
		}
		if (player.getHorse().getGrade() >= this.value) {
			return true;
		}
		throw new ManagedException(ManagedErrorCode.NOT_ENOUGH_HORSEGRADE);
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
    public Class<?>[] getEvent() {
		return new Class<?>[] {HorseGradeUpEvent.class};
    }

	@Override
	public Class<?>[] getNicknameEvent() {
		return new Class<?>[] {HorseGradeUpEvent.class};
	}

	@Override
	public Class<?>[] getAchievementEvent() {
		return new Class<?>[] {HorseGradeUpEvent.class};
	}
	
}
