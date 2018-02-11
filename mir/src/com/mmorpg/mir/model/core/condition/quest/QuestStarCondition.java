package com.mmorpg.mir.model.core.condition.quest;

import com.mmorpg.mir.model.quest.model.Quest;

/**
 * 任务星级
 * 
 * @author Kuang Hao
 * @since v1.0 2014-8-21
 * 
 */
public class QuestStarCondition extends AbstractQuestCoreCondition {

	@Override
	public boolean verify(Object object) {
		Quest quest = null;
		if (object instanceof Quest) {
			quest = (Quest) object;
		}
		if (quest == null) {
			this.errorObject(object);
		}
		if (quest.getStar() == (byte) (value - 1)) {
			return true;
		}
		return false;
	}
}
