package com.mmorpg.mir.model.core.condition.quest;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.event.AnotherDayEvent;
import com.mmorpg.mir.model.quest.model.QuestType;

/**
 * 今天没有完成该种类型的任务次数
 * 
 * @author Kuang Hao
 * @since v1.0 2014-8-21
 * 
 */
public class TodayNoCompleteTypeQuestCondition extends AbstractQuestCoreCondition implements QuestCondition {

	@Override
	public boolean verify(Object object) {
		Player player = null;
		if (object instanceof Player) {
			player = (Player) object;
		}
		if (player == null) {
			this.errorObject(object);
		}
		QuestType type = QuestType.valueOf(code);
		int maxLimit = value;
		/*
		 * if (type == QuestType.RANDOM) { maxLimit +=
		 * player.getWelfare().getWelfareHistory
		 * ().getClawbackNum(ClawbackEnum.CLAWBACK_EVENT_RANDOM); }
		 */
		if (player.getQuestPool().getTodayTypeCompleteCount(type) < maxLimit) {
			return true;
		}
		if (isThrowException()) {
			throw new ManagedException(ManagedErrorCode.TYPE_OF_QUEST_OUTOF_COMPLETETODAY);
		} else {
			return false;
		}
	}

	@Override
	public Class<?>[] getEvent() {
		return new Class<?>[] { AnotherDayEvent.class };
	}
}
