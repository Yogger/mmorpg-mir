package com.mmorpg.mir.model.core.condition.quest;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.quest.model.Quest;
import com.mmorpg.mir.model.quest.model.QuestType;

/**
 * 当前没有接取该种类型的任务
 * 
 * @author Kuang Hao
 * @since v1.0 2014-8-21
 * 
 */
public class CurrentNoAcceptTypeQuestCondition extends AbstractQuestCoreCondition {

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
		for (Quest quest : player.getQuestPool().getQuests().values()) {
			if (quest.getResource().getType() == type) {
				if (isThrowException()) {
					throw new ManagedException(ManagedErrorCode.CURRENT_NOACCEPT_TYPE_QUEST);
				} else {
					return false;
				}
			}
		}
		return true;
	}
}
