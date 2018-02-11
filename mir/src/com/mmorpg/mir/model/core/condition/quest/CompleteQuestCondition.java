package com.mmorpg.mir.model.core.condition.quest;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.quest.event.QuestCompletEvent;
import com.mmorpg.mir.model.quest.model.Quest;
import com.mmorpg.mir.model.skill.model.SkillCondition;

/**
 * 完成任务的次数大于value
 * 
 * @author Kuang Hao
 * @since v1.0 2014-8-21
 * 
 */
public class CompleteQuestCondition extends AbstractQuestCoreCondition implements SkillCondition {

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

		if (!player.getQuestPool().getCompletionHistory().containsKey(code)) {
			return false;
		}
		if (player.getQuestPool().getCompletionHistory().get(code) >= value) {
			return true;
		}
		return false;
	}

	@Override
	public Class<?>[] getSkillEvent() {
		return new Class<?>[] { QuestCompletEvent.class };
	}
}
