package com.mmorpg.mir.model.core.condition.quest;

import com.mmorpg.mir.model.core.condition.GiftCollectCondition;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.quest.event.QuestCompletEvent;
import com.mmorpg.mir.model.quest.model.Quest;
import com.mmorpg.mir.model.quest.model.QuestType;

/**
 * 今天完成该种类型的任务次数
 * 
 * @author Kuang Hao
 * @since v1.0 2014-8-21
 * 
 */
public class TodayCompleteTypeCountCondition extends AbstractQuestCoreCondition implements GiftCollectCondition {

	@Override
	public boolean verify(Object object) {
		Player player = null;
		if (object instanceof Player) {
			player = (Player) object;
		}
		if (object instanceof Quest) {
			player = ((Quest) object).getOwner();
		}
		if (player == null) {
			this.errorObject(object);
		}
		QuestType type = QuestType.valueOf(code);
		if (player.getQuestPool().getTodayTypeCompleteCount(type) >= value) {
			return true;
		}
		return false;
	}

	@Override
	public Class<?>[] getGiftCollectEvent() {
		return new Class<?>[] { QuestCompletEvent.class };
	}
}
