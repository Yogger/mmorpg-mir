package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.achievement.model.AchievementCondition;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.condition.quest.QuestCondition;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.nickname.model.NicknameCondition;
import com.mmorpg.mir.model.quest.model.Quest;
import com.mmorpg.mir.model.soul.event.SoulUpgradeEvent;

public class SoulGradeCondition extends AbstractCoreCondition implements QuestCondition, NicknameCondition,AchievementCondition{

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
			this.errorObject(object);
		}
		
		if (player.getSoul().getLevel() >= value) {
			return true;
		}
		
		throw new ManagedException(ManagedErrorCode.SOUL_GRADE_NOT_ENOUGH);
	}

	@Override
    public Class<?>[] getEvent() {
		return new Class<?>[] {SoulUpgradeEvent.class};
    }

	@Override
	public Class<?>[] getNicknameEvent() {
		return new Class<?>[] {SoulUpgradeEvent.class};
	}

	@Override
	public Class<?>[] getAchievementEvent() {
		return new Class<?>[] {SoulUpgradeEvent.class};
	}
	
}
