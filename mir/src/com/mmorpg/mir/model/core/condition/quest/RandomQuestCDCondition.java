package com.mmorpg.mir.model.core.condition.quest;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.quest.model.QuestType;
import com.windforce.common.utility.DateUtils;

/**
 * 游历任务的CD接取条件限制
 * 
 * @author Kuang Hao
 * @since v1.0 2014-8-21
 * 
 */
public class RandomQuestCDCondition extends AbstractQuestCoreCondition {

	@Override
	public boolean verify(Object object) {
		Player player = null;
		if (object instanceof Player) {
			player = (Player) object;
		}
		if (player == null) {
			this.errorObject(object);
		}
		// 每完成五个任务后添加CD时间10分钟，不在CD时间内方可接取
		int completeRandomCountToday = player.getQuestPool().getTodayTypeCompleteCount(QuestType.RANDOM);
		if (completeRandomCountToday != 0 && completeRandomCountToday % Integer.valueOf(code) == 0) {
			if ((player.getQuestPool().getLastCompletedRandomQuestTime() + value * DateUtils.MILLIS_PER_SECOND) > System
					.currentTimeMillis()) {
				if (isThrowException()) {
					throw new ManagedException(ManagedErrorCode.RANDOM_QUEST_CD);
				}
			}
		}

		return true;
	}

	public static void main(String[] args) {
		for (int i = 0; i < 12; i++) {
			System.out.println(i + ">>>" + (i % 5));
		}
	}

}
