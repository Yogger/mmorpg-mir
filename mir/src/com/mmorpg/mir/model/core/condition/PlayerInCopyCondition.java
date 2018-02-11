package com.mmorpg.mir.model.core.condition;

import java.util.Map;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.quest.model.Quest;
import com.mmorpg.mir.model.skill.model.Skill;
import com.mmorpg.mir.model.trigger.model.TriggerContextKey;

/**
 * 玩家有在副本
 * 
 * @author Kuang Hao
 * @since v1.0 2014-8-21
 * 
 */
public class PlayerInCopyCondition extends AbstractCoreCondition {

	@SuppressWarnings("unchecked")
	@Override
	public boolean verify(Object object) {
		Player player = null;
		if (object instanceof Player) {
			player = (Player) object;
		}
		if (object instanceof Quest) {
			player = ((Quest) object).getOwner();
		}
		if (object instanceof Skill) {
			player = (Player) ((Skill) object).getEffector();
		}
		if (object instanceof Map) {
			player = (Player) ((Map<String, Object>) object).get(TriggerContextKey.PLAYER);
		}
		if (player == null) {
			this.errorObject(object);
		}
		if (code == null) {
			if (player.isInCopy()) {
				return true;
			}
		} else {
			if (code.equals(player.getCopyHistory().getCurrentCopyResourceId())) {
				return true;
			}
		}
		throw new ManagedException(ManagedErrorCode.PLAYER_IN_COPY);
	}
}
