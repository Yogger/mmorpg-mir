package com.mmorpg.mir.model.quest.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.quest.manager.QuestManager;
import com.mmorpg.mir.model.quest.model.Quest;
import com.mmorpg.mir.model.quest.model.QuestUpdate;
import com.mmorpg.mir.model.quest.resource.QuestResource;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.windforce.common.resource.anno.Static;

@Component
public class QuestService {

	/** 领取奖励条件 */
	@Static("QUEST_REWARD_CONDITIONS")
	private ConfigValue<String[]> QUEST_REWARD_CONDITIONS;

	public void acceptQuest(Player player, String id) {
		if (player.getQuestPool().getQuests().containsKey(id)) {
			throw new ManagedException(ManagedErrorCode.HAVE_QUEST);
		}
		QuestUpdate update = new QuestUpdate();
		long now = System.currentTimeMillis();
		QuestResource resource = QuestManager.staticQuestResources.get(id, true);
		if (!resource.isClientType()) {
			throw new ManagedException(ManagedErrorCode.QUEST_NOT_CLIENTACCPET);
		}
		List<Quest> newQuests = player.getQuestPool().acceptQuests(update, player, Arrays.asList(id), now, true, false);
		if (!update.getQuests().isEmpty()) {
			PacketSendUtility.sendPacket(player, update.createVO());
		}
		if (!newQuests.isEmpty()) {
			player.getQuestPool().refreshQuest(newQuests);
		}
	}

	public void reward(Player player, String id) {
		if (!CoreConditionManager.getInstance().getCoreConditions(1, QUEST_REWARD_CONDITIONS.getValue())
				.verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		player.getQuestPool().reward(id);
	}

	public void clientComplete(Player player, String id) {
		player.getQuestPool().clientComplete(id);
	}

	public void giveUp(Player player, String id) {
		player.getQuestPool().giveUp(id);
	}

	public void atOnceComplete(Player player, String id) {
		player.getQuestPool().goldComplete(id);
	}

	public void levelUpStar(Player player, String id) {
		player.getQuestPool().levelUpStar(id);
	}

	public void dayQuestAllComplete(Player player, String id) {
		player.getQuestPool().dayCompleteAll(id);
	}
}
