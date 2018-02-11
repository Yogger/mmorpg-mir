package com.mmorpg.mir.model.skill.model;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleHandle;
import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.entity.PlayerEnt;
import com.windforce.common.utility.JsonUtils;

@Component
public class SkillListInit extends ModuleHandle {

	@Override
	public ModuleKey getModule() {
		return ModuleKey.SKILL;
	}

	@Override
	public void deserialize(PlayerEnt ent) {
		if (ent.getSkillJson() == null) {
			ent.setSkillJson(JsonUtils.object2String(new SkillList()));
		}

		Player player = ent.getPlayer();

		if (player.getSkillList() == null) {
			SkillList skillList = JsonUtils.string2Object(ent.getSkillJson(), SkillList.class);
			skillList.setOwner(player);
			player.setSkillList(skillList);
		}
	}

	@Override
	public void serialize(PlayerEnt ent) {
		Player player = ent.getPlayer();
		if (player.getSkillList() != null) {
			ent.setSkillJson(JsonUtils.object2String(player.getSkillList()));
		}
	}
}
