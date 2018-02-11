package com.mmorpg.mir.model.quest.keyhandle.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.h2.util.New;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.quest.event.QuestCompletEvent;
import com.mmorpg.mir.model.quest.keyhandle.QuestKeyHandle;
import com.mmorpg.mir.model.quest.model.KeyType;
import com.mmorpg.mir.model.quest.model.Quest;
import com.mmorpg.mir.model.quest.model.QuestKey;
import com.mmorpg.mir.model.quest.resource.QuestKeyResource;
import com.windforce.common.event.anno.ReceiverAnno;
import com.windforce.common.utility.JsonUtils;

@Component
public class QuestCompleteOr extends QuestKeyHandle {

	public static String QUESTIDS = "QUESTIDS";

	@Override
	public KeyType getType() {
		return KeyType.COMPLETE_QUEST_OR;
	}

	// 所接收的事件
	@ReceiverAnno
	public void completeEvent(QuestCompletEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		List<Quest> quests = this.getAllQuestKey(player);
		List<Quest> needUpdate = New.arrayList();
		for (Quest quest : quests) {
			for (QuestKey key : quest.getKeys()) {
				if (key.getType() == this.getType()) {
					if (key.getParms() == null || key.getParms().isEmpty()) {
						key.addValue();
						needUpdate.add(quest);
					} else {
						String[] questIds = JsonUtils.string2Array(key.getParms().get(QUESTIDS).toString(),
								String.class);
						if (Arrays.asList(questIds).contains(event.getQuestId())) {
							key.addValue();
							needUpdate.add(quest);
						}
					}
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
			key.getParms().put(QUESTIDS, resource.getParms().get(QUESTIDS));
		}
		return key;
	}

	public static void main(String[] args) {
		QuestKeyResource qr = new QuestKeyResource();
		qr.setParms(new HashMap<String, Object>());
		qr.getParms().put(QUESTIDS, JsonUtils.object2String(new String[] { "quest1", "quest2" }));
		System.out.println(JsonUtils.object2String(qr));
	}

}
