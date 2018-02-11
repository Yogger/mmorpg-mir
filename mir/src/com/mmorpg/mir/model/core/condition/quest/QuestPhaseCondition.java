package com.mmorpg.mir.model.core.condition.quest;

import com.mmorpg.mir.model.quest.model.Quest;
import com.mmorpg.mir.model.quest.model.QuestPhase;

/**
 * 任务处于该状态
 * 
 * @author Kuang Hao
 * @since v1.0 2014-8-21
 * 
 */
public class QuestPhaseCondition extends AbstractQuestCoreCondition {

	@Override
	public boolean verify(Object object) {
		Quest quest = null;
		if (object instanceof Quest) {
			quest = (Quest) object;
		}
		if (quest == null) {
			this.errorObject(object);
		}
		if (quest.getPhase() == QuestPhase.typeOf(code)) {
			return true;
		}
		return false;
	}
}
