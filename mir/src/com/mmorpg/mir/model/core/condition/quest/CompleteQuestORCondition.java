package com.mmorpg.mir.model.core.condition.quest;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.quest.event.QuestCompletEvent;
import com.mmorpg.mir.model.skill.model.SkillCondition;
import com.windforce.common.utility.JsonUtils;

/**
 * 完成任务的次数大于value
 * 
 * @author Kuang Hao
 * @since v1.0 2014-8-21
 * 
 */
public class CompleteQuestORCondition extends AbstractQuestCoreCondition implements SkillCondition {

	private String[] questIds;
	
	@Override
	public boolean verify(Object object) {
		Player player = null;
		if (object instanceof Player) {
			player = (Player) object;
		}

		if (player == null) {
			this.errorObject(object);
		}
		for (String questId : questIds) {
			if (player.getQuestPool().getCompletionHistory().containsKey(questId)) {
				if (player.getQuestPool().getCompletionHistory().get(questId) >= 1) {
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	protected void init() {
		super.init();
		questIds = JsonUtils.string2Array(code, String.class);
	}

	@Override
	public Class<?>[] getSkillEvent() {
		return new Class<?>[] { QuestCompletEvent.class };
	}
}
