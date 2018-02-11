package com.mmorpg.mir.model.core.condition.quest;

import java.util.Date;

import com.mmorpg.mir.model.core.condition.AbstractCoreCondition;
import com.mmorpg.mir.model.player.event.AnotherDayEvent;
import com.mmorpg.mir.model.quest.model.Quest;
import com.windforce.common.utility.DateUtils;

/**
 * 任务的创建时间不是今天
 * 
 * @author Kuang Hao
 * @since v1.0 2014-8-21
 * 
 */
public class QuestNotCreateTodayCondition extends AbstractQuestCoreCondition implements QuestCondition {

	@Override
	public boolean verify(Object object) {
		Quest quest = null;
		if (object instanceof Quest) {
			quest = (Quest) object;
		}
		if (quest == null) {
			this.errorObject(object);
		}
		if (!DateUtils.isToday(new Date(quest.getCreateTime()))) {
			return true;
		}
		return false;
	}

	@Override
	public Class<?>[] getEvent() {
		return new Class<?>[] { AnotherDayEvent.class };
	}

	@Override
	protected boolean check(AbstractCoreCondition condition) {
		return false;
	}

}
