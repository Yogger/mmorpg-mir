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
import com.mmorpg.mir.model.welfare.event.ExpressEvent;
import com.windforce.common.event.anno.ReceiverAnno;

@Component
public class Express extends QuestKeyHandle {

	public static String EXPRESSID = "EXPRESSID";

	@Override
	public KeyType getType() {
		return KeyType.EXPRESS;
	}

	// 所接收的事件
	@ReceiverAnno
	public void express(ExpressEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		List<Quest> quests = this.getAllQuestKey(player);
		List<Quest> needUpdate = New.arrayList();
		for (Quest quest : quests) {
			for (QuestKey key : quest.getKeys()) {
				if (key.getParms() == null || key.getParms().isEmpty()) {
					key.addValue();
					needUpdate.add(quest);
				} else if (key.getParms().get(EXPRESSID).equals(event.getExprssId())) {
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
		return key;
	}

}
