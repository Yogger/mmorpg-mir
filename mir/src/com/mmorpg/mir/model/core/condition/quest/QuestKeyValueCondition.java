package com.mmorpg.mir.model.core.condition.quest;

import com.mmorpg.mir.model.quest.model.Quest;

/**
 * 任务的value值验证
 * 
 * @author Kuang Hao
 * @since v1.0 2014-8-21
 * 
 */
public class QuestKeyValueCondition extends AbstractQuestCoreCondition {

	@Override
	public boolean verify(Object object) {
		Quest quest = null;
		if (object instanceof Quest) {
			quest = (Quest) object;
		}
		if (quest == null) {
			this.errorObject(object);
		}

		if (quest.findKey(code) == null) {
			throw new RuntimeException(String.format("玩家[%s]任务[%s]code[%s]上下文找不到!", quest.getOwner().getObjectId(),
					quest.getId(), code));
		}
		if (quest.findKey(code).getValue() >= value) {
			return true;
		}
		return false;
	}
}
