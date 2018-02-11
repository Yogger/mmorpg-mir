package com.mmorpg.mir.model.quest.keyhandle.impl;

import java.util.List;

import org.h2.util.New;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.item.Equipment;
import com.mmorpg.mir.model.item.event.EquipEquipmentEvent;
import com.mmorpg.mir.model.item.event.EquipmentReElementEvent;
import com.mmorpg.mir.model.quest.keyhandle.QuestKeyHandle;
import com.mmorpg.mir.model.quest.model.KeyType;
import com.mmorpg.mir.model.quest.model.Quest;
import com.mmorpg.mir.model.quest.model.QuestKey;
import com.mmorpg.mir.model.quest.resource.QuestKeyResource;
import com.windforce.common.event.anno.ReceiverAnno;

@Component
public class EquipSouledEquipment extends QuestKeyHandle {

	@Override
	public KeyType getType() {
		return KeyType.SOULED_EQUIPMENT;
	}

	// 所接收的事件
	@ReceiverAnno
	public void equipSouled(EquipEquipmentEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		List<Quest> quests = this.getAllQuestKey(player);
		List<Quest> needUpdate = New.arrayList();
		for (Quest quest : quests) {
			for (QuestKey key : quest.getKeys()) {
				if (key.getType() == this.getType()) {
					int count = 0;
					for (Equipment e: player.getEquipmentStorage().getEquipments()) {
						if (e != null && e.getSoulType() != 0) {
							count++;
						}
					}
					if (key.getValue() < count) {
						key.setValue(count);
						needUpdate.add(quest);
					}
				}
			}
		}
		player.getQuestPool().refreshQuest(needUpdate);
	}
	
	@ReceiverAnno
	public void equipReElement(EquipmentReElementEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		List<Quest> quests = this.getAllQuestKey(player);
		List<Quest> needUpdate = New.arrayList();
		for (Quest quest : quests) {
			for (QuestKey key : quest.getKeys()) {
				if (key.getType() == this.getType()) {
					int count = 0;
					for (Equipment e: player.getEquipmentStorage().getEquipments()) {
						if (e != null && e.getSoulType() != 0) {
							count++;
						}
					}
					if (key.getValue() < count) {
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
		key.setValue(0);
		if (resource.getKeyname() == null) {
			key.setKeyname(getType().name());
		} else {
			key.setKeyname(resource.getKeyname());
		}
		int count = 0;
		for (Equipment e: player.getEquipmentStorage().getEquipments()) {
			if (e != null && e.getSoulType() != 0) {
				count++;
			}
		}
		key.setValue(count);
		return key;
	}

}
