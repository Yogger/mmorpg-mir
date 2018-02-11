package com.mmorpg.mir.model.quest.keyhandle;

import java.util.List;

import javax.annotation.PostConstruct;

import org.h2.util.New;
import org.springframework.beans.factory.annotation.Autowired;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.quest.manager.QuestManager;
import com.mmorpg.mir.model.quest.model.KeyType;
import com.mmorpg.mir.model.quest.model.Quest;
import com.mmorpg.mir.model.quest.model.QuestKey;
import com.mmorpg.mir.model.quest.model.QuestPhase;
import com.mmorpg.mir.model.quest.resource.QuestKeyResource;

/**
 * 活动条件处理
 * 
 * @author Kuang Hao
 * @since v1.0 2013-7-17
 * 
 */
public abstract class QuestKeyHandle {
	/**
	 * 条件
	 * 
	 * @return
	 */
	public abstract KeyType getType();

	@Autowired
	protected QuestManager questManager;
	@Autowired
	protected PlayerManager playerManager;

	@PostConstruct
	public void init() {
		questManager.registerActivityHandle(this);
	}

	/**
	 * 构建一个条件
	 * 
	 * @param sample
	 * @return
	 */
	public abstract QuestKey create(QuestKeyResource resource, long now, Player player);

	/**
	 * 获取所有包含了该条件的任务
	 * 
	 * @param playerId
	 * @return
	 */
	public List<Quest> getAllQuestKey(Player player) {
		List<Quest> quests = New.arrayList();
		for (Quest quest : player.getQuestPool().getQuests().values()) {
			if (quest.getPhase() == QuestPhase.COMPLETE || quest.getPhase() == QuestPhase.FAIL) {
				continue;
			}
			for (QuestKey key : quest.getKeys()) {
				if (key.getType() == this.getType()) {
					quests.add(quest);
					break;
				}
			}
		}
		return quests;
	}
}
