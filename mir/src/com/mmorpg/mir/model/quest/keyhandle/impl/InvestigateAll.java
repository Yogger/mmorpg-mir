package com.mmorpg.mir.model.quest.keyhandle.impl;

import java.util.List;

import org.h2.util.New;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.quest.keyhandle.QuestKeyHandle;
import com.mmorpg.mir.model.quest.model.KeyType;
import com.mmorpg.mir.model.quest.model.Quest;
import com.mmorpg.mir.model.quest.model.QuestKey;
import com.mmorpg.mir.model.quest.resource.QuestKeyResource;
import com.mmorpg.mir.model.welfare.event.InvestigateEvent;
import com.windforce.common.event.anno.ReceiverAnno;

@Component
public class InvestigateAll extends QuestKeyHandle {

	@Override
	public KeyType getType() {
		return KeyType.INVESTIGATE_ALL;
	}

	// 所接收的事件
	@ReceiverAnno
	public void rescueEvent(InvestigateEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		List<Quest> quests = this.getAllQuestKey(player);
		List<Quest> needUpdate = New.arrayList();
		for (Quest quest : quests) {
			for (QuestKey key : quest.getKeys()) {
				if (key.getType() == this.getType()) {
					int count = player.getInvestigate().getCountAll();
					if (key.getValue() != count) {
						key.setValue(count);
						needUpdate.add(quest);
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
		key.setValue(player.getInvestigate().getCountAll());
		if (resource.getKeyname() == null) {
			key.setKeyname(resource.getType().name());
		} else {
			key.setKeyname(resource.getKeyname());
		}
		return key;
	}
}
