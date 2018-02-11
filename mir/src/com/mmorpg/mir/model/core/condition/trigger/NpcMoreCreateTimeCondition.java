package com.mmorpg.mir.model.core.condition.trigger;

import java.util.Map;

import com.mmorpg.mir.model.core.condition.AbstractCoreCondition;
import com.mmorpg.mir.model.gameobjects.Npc;
import com.mmorpg.mir.model.trigger.model.TriggerContextKey;

/**
 * 当前生物出生时间大于多少毫秒
 * 
 * @author Kuang Hao
 * @since v1.0 2014-8-21
 * 
 */
public class NpcMoreCreateTimeCondition extends AbstractCoreCondition {

	@SuppressWarnings("unchecked")
	@Override
	public boolean verify(Object object) {
		Map<String, Object> contexts = (Map<String, Object>) object;
		Npc npc = (Npc) contexts.get(TriggerContextKey.NPC);
		if ((System.currentTimeMillis() - npc.getCreateTime()) >= value) {
			return true;
		}
		return false;
	}
}
