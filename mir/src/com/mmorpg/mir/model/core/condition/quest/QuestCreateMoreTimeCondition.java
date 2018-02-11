package com.mmorpg.mir.model.core.condition.quest;

import com.mmorpg.mir.model.player.event.HeartbeatEvent;
import com.mmorpg.mir.model.quest.model.Quest;

/**
 * 任务的创建时间过期
 * 
 * @author Kuang Hao
 * @since v1.0 2014-8-21
 * 
 */
public class QuestCreateMoreTimeCondition extends AbstractQuestCoreCondition implements QuestCondition {

	@Override
	public boolean verify(Object object) {
		Quest quest = null;
		if (object instanceof Quest) {
			quest = (Quest) object;
		}
		if (quest == null) {
			this.errorObject(object);
		}
		if ((quest.getCreateTime() + value) < System.currentTimeMillis()) {
			return true;
		}
		return false;
	}

	@Override
	public Class<?>[] getEvent() {
		return new Class<?>[] { HeartbeatEvent.class };
	}
}
