package com.mmorpg.mir.model.quest.model;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleHandle;
import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.entity.PlayerEnt;
import com.windforce.common.utility.JsonUtils;

@Component
public class QuestPoolInit extends ModuleHandle {

	@Override
	public ModuleKey getModule() {
		return ModuleKey.QUEST;
	}

	@Override
	public void deserialize(PlayerEnt ent) {
		if (ent.getQuestPoolJson() == null) {
			ent.setQuestPoolJson(JsonUtils.object2String(new QuestPool()));
		}

		Player player = ent.getPlayer();

		if (player.getQuestPool() == null) {
			QuestPool questPool = JsonUtils.string2Object(ent.getQuestPoolJson(), QuestPool.class);
			player.setQuestPool(questPool);
			questPool.setOwner(player);
			for (Quest quest : questPool.getQuests().values()) {
				quest.setOwner(player);
			}
		}
	}

	@Override
	public void serialize(PlayerEnt ent) {
		Player player = ent.getPlayer();
		if (player.getQuestPool() != null) {
			ent.setQuestPoolJson(JsonUtils.object2String(player.getQuestPool()));
		}
	}
}
