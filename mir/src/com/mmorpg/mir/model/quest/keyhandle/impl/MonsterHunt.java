package com.mmorpg.mir.model.quest.keyhandle.impl;

import java.util.HashMap;
import java.util.List;

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
public class MonsterHunt extends QuestKeyHandle {

	public static final String MONSTERID = "MONSTERID";
	public static final String UPLEVEL = "UPLEVEL";
	public static final String LOWLEVEL = "LOWLEVEL";

	@Override
	public KeyType getType() {
		return KeyType.MONSTER_HUNT;
	}

	// 所接收的事件
	@ReceiverAnno
	public void addMonsterKill(MonsterKillEvent event) {
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
						String monsterId = (String) key.getParms().get(MONSTERID);
						Integer lowLevel = (Integer) key.getParms().get(LOWLEVEL);
						Integer upLevel = (Integer) key.getParms().get(UPLEVEL);
						boolean checkMonster = (monsterId != null ? monsterId.equals(event.getKey()) : true);
						boolean checkUpLevel = (upLevel != null ? event.getLevel() - player.getLevel() <= upLevel
								: true);
						boolean checkLowLevel = (lowLevel != null ? player.getLevel() - event.getLevel() <= lowLevel
								: true);
						success = (checkMonster && checkLowLevel & checkUpLevel);
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
			key.getParms().put(MONSTERID, resource.getParms().get(MONSTERID));
			key.getParms().put(LOWLEVEL, resource.getParms().get(LOWLEVEL));
			key.getParms().put(UPLEVEL, resource.getParms().get(UPLEVEL));
		}
		return key;
	}

}
