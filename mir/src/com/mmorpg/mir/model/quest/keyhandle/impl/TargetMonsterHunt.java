package com.mmorpg.mir.model.quest.keyhandle.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.h2.util.New;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.event.MonsterKillEvent;
import com.mmorpg.mir.model.quest.keyhandle.QuestKeyHandle;
import com.mmorpg.mir.model.quest.model.KeyType;
import com.mmorpg.mir.model.quest.model.Quest;
import com.mmorpg.mir.model.quest.model.QuestKey;
import com.mmorpg.mir.model.quest.resource.QuestKeyResource;
import com.windforce.common.event.anno.ReceiverAnno;

@Component
public class TargetMonsterHunt extends QuestKeyHandle {

	private final String MONSTERIDS = "MONSTERIDS";

	@Override
	public KeyType getType() {
		return KeyType.TARGET_MONSTER_HUNT;
	}

	@ReceiverAnno
	public void addMonsterHunt(MonsterKillEvent event) {
		if (!event.isKnowPlayer()) {
			return;
		}
		Player player = playerManager.getPlayer(event.getOwner());
		List<Quest> quests = this.getAllQuestKey(player);
		List<Quest> needUpdate = New.arrayList();
		for (Quest quest : quests) {
			for (QuestKey key : quest.getKeys()) {
				boolean success = false;
				if (key.getType() == this.getType()) {
					if (key.getParms() != null) {
						@SuppressWarnings("unchecked")
						HashSet<String> monsterIdSet = (HashSet<String>) key.getParms().get(MONSTERIDS);
						boolean checkMonster = monsterIdSet.contains(event.getKey());
						success = checkMonster;
					} else if (key.getParms() == null || key.getParms().isEmpty()) {
						success = true;
					}
				}
				if (success) {
					key.addValue();
					needUpdate.add(quest);
				}
			}
		}
		player.getQuestPool().refreshQuest(needUpdate);
	}

	@Override
	public QuestKey create(QuestKeyResource resource, long now, Player player) {
		QuestKey key = new QuestKey();
		key.setType(this.getType());
		key.setValue(0);
		if (resource.getKeyname() == null) {
			key.setKeyname(resource.getType().name());
		} else {
			key.setKeyname(resource.getKeyname());
		}
		if (resource.getParms() != null) {
			key.setParms(new HashMap<String, Object>());
			String[] monsterIds = StringUtils.split((String) resource.getParms().get(MONSTERIDS), ",");
			Set<String> monsterIdSet = new HashSet<String>(Arrays.asList(monsterIds));
			key.getParms().put(MONSTERIDS, monsterIdSet);
		}
		return key;
	}

}
