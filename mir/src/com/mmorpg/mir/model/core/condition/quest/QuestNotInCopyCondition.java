package com.mmorpg.mir.model.core.condition.quest;

import com.mmorpg.mir.model.copy.event.LeaveCopyEvent;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.quest.model.Quest;
import com.mmorpg.mir.model.skill.model.Skill;

/**
 * 没有在副本
 * 
 * @author Kuang Hao
 * @since v1.0 2014-8-21
 * 
 */
public class QuestNotInCopyCondition extends AbstractQuestCoreCondition implements QuestCondition {

	@Override
	public boolean verify(Object object) {
		Player player = null;
		if (object instanceof Player) {
			player = (Player) object;
		}
		if (object instanceof Skill) {
			player = (Player) ((Skill) object).getEffector();
		}
		if (object instanceof Quest) {
			player = ((Quest) object).getOwner();
		}
		if (player == null) {
			this.errorObject(object);
		}
		player.getCopyHistory().getCurrentMapInstance();
		if (code == null) {
			if (player.getCopyHistory().getCurrentCopyResourceId() == null) {
				return true;
			}
		} else {
			if (!code.equals(player.getCopyHistory().getCurrentCopyResourceId())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Class<?>[] getEvent() {
		return new Class<?>[] { LeaveCopyEvent.class };
	}
}
