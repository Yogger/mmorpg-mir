package com.mmorpg.mir.model.trigger.model.handle;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.skill.SkillEngine;
import com.mmorpg.mir.model.skill.model.Skill;
import com.mmorpg.mir.model.trigger.model.AbstractTrigger;
import com.mmorpg.mir.model.trigger.model.TriggerContextKey;
import com.mmorpg.mir.model.trigger.model.TriggerType;
import com.mmorpg.mir.model.trigger.resource.TriggerResource;

@Component
public class SkillTrigger extends AbstractTrigger {

	@Override
	public TriggerType getType() {
		return TriggerType.SKILL;
	}

	@Override
	public void handle(Map<String, Object> contexts, final TriggerResource resource) {
		final Player player = (Player) contexts.get(TriggerContextKey.PLAYER);
		int skillId = Integer.valueOf(resource.getKeys().get(TriggerContextKey.SKILLID));
		Skill skill = SkillEngine.getInstance().getSkill(player, skillId, player.getObjectId(), 0, 0, player, null);
		skill.noEffectorUseSkill();
	}
}
