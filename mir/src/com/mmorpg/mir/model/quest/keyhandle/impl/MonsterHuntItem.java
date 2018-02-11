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
import com.windforce.common.utility.RandomUtils;

@Component
public class MonsterHuntItem extends QuestKeyHandle {

	public static String MONSTERID = "MONSTERID";

	public static String ITEM_RATE = "ITEM_RATE";
	
	public static String OUT_COUNT = "OUT_COUNT";

	@Override
	public KeyType getType() {
		return KeyType.MONSTER_HUNT_ITEM;
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
				if (key.getType() == this.getType() && key.getParms().get(MONSTERID).equals(event.getKey())) {
					int rate = Integer.valueOf((String) key.getParms().get(ITEM_RATE));
					Integer count = 0, finished = 1;
					if (key.getParms().containsKey(OUT_COUNT)) {
						count = Integer.valueOf((String) key.getParms().get(OUT_COUNT));
					}
					if (key.getCtx().containsKey(OUT_COUNT)) {
						finished = (Integer) key.getCtx().get(OUT_COUNT);
					}
					if (RandomUtils.isHit(rate * 1.0 / 10000) || (count != 0 && (finished % count == 0))) {
						key.addValue();
						needUpdate.add(quest);
					}
					key.getCtx().put(OUT_COUNT, finished + 1);
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
		key.setParms(new HashMap<String, Object>());
		key.getParms().put(MONSTERID, resource.getParms().get(MONSTERID));
		key.getParms().put(ITEM_RATE, resource.getParms().get(ITEM_RATE));
		if(resource.getParms().containsKey(OUT_COUNT)) {
			key.getParms().put(OUT_COUNT, resource.getParms().get(OUT_COUNT));
		} else {
			key.getParms().put(OUT_COUNT, "0");
		}
		return key;
	}
}
