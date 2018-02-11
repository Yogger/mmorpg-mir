package com.mmorpg.mir.model.quest.keyhandle.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.h2.util.New;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.express.event.ExpressBeenRobEvent;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.group.model.PlayerGroup;
import com.mmorpg.mir.model.quest.keyhandle.QuestKeyHandle;
import com.mmorpg.mir.model.quest.model.KeyType;
import com.mmorpg.mir.model.quest.model.Quest;
import com.mmorpg.mir.model.quest.model.QuestKey;
import com.mmorpg.mir.model.quest.resource.QuestKeyResource;
import com.windforce.common.event.anno.ReceiverAnno;

@Component
public class ExpressRob extends QuestKeyHandle {

	@Override
	public KeyType getType() {
		return KeyType.EXPRESS_ROB;
	}

	// 所接收的事件
	@ReceiverAnno
	public void robEvent(ExpressBeenRobEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		// TODO: according the key parameters to recognize the personal mission or team mission?
		PlayerGroup playerGroup = player.getPlayerGroup();
		Collection<Player> members = (playerGroup == null ? Arrays.asList(player) : playerGroup.getMembers()); 
		for (Player member : members) {
			List<Quest> quests = this.getAllQuestKey(member);
			List<Quest> needUpdate = New.arrayList();
			for (Quest quest : quests) {
				for (QuestKey key : quest.getKeys()) {
					if (key.getType() == this.getType()) {
						key.addValue();
						needUpdate.add(quest);
					}
				}
			}
			member.getQuestPool().refreshQuest(needUpdate);
		}
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
