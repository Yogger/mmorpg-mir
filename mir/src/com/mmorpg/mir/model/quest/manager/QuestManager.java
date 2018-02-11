package com.mmorpg.mir.model.quest.manager;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.Debug;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.event.LoginEvent;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.quest.keyhandle.QuestKeyHandle;
import com.mmorpg.mir.model.quest.model.KeyType;
import com.mmorpg.mir.model.quest.model.Quest;
import com.mmorpg.mir.model.quest.model.QuestKey;
import com.mmorpg.mir.model.quest.model.QuestPhase;
import com.mmorpg.mir.model.quest.model.QuestUpdate;
import com.mmorpg.mir.model.quest.resource.QuestKeyResource;
import com.mmorpg.mir.model.quest.resource.QuestResource;
import com.mmorpg.mir.model.trigger.manager.TriggerManager;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;
import com.windforce.common.utility.New;

@Component
public class QuestManager implements IQuestManager {
	private static final Logger logger = Logger.getLogger(QuestManager.class);

	@Static
	public Storage<String, QuestResource> questResources;

	public static Storage<String, QuestResource> staticQuestResources;

	@Autowired
	public TriggerManager triggerManager;

	private static QuestManager self;

	public static QuestManager getInstance() {
		return self;
	}

	@PostConstruct
	public void init() {
		staticQuestResources = questResources;
		self = this;
	}

	/** 活动处理器集合 */
	private Map<KeyType, QuestKeyHandle> handles = New.hashMap();;

	public void registerActivityHandle(QuestKeyHandle questKeyHandle) {
		handles.put(questKeyHandle.getType(), questKeyHandle);
	}

	/**
	 * 创建任务对象
	 * 
	 * @param resource
	 * @param player
	 * @param now
	 * @return
	 */
	public Quest createQuest(QuestResource resource, Player player, long now) {
		Quest quest = Quest.valueOf(player, resource.getId(), now);
		if (resource.getKeys() != null) {
			for (QuestKeyResource keyResource : resource.getKeys()) {
				QuestKey key = handles.get(keyResource.getType()).create(keyResource, now, player);
				quest.getKeys().add(key);
			}
		}
		quest.setStar(resource.selectStar());
		quest.setPhase(QuestPhase.INCOMPLETE);
		return quest;
	}

	public short[] convertTemplateId(List<String> ids) {
		if (ids == null) {
			return null;
		}
		short[] intIds = new short[ids.size()];
		int i = 0;
		for (String id : ids) {
			intIds[i] = questResources.get(id, true).getTemplateId();
			i++;
		}
		return intIds;
	}

	public short convertTemplateId(String id) {
		return questResources.get(id, true).getTemplateId();
	}

	public HashMap<Short, Integer> convertTemplateId(Map<String, Integer> ids) {
		if (ids == null) {
			return null;
		}
		HashMap<Short, Integer> intIds = new HashMap<Short, Integer>();
		for (Entry<String, Integer> entry : ids.entrySet()) {
			intIds.put(questResources.get(entry.getKey(), true).getTemplateId(), entry.getValue());
		}
		return intIds;
	}

	@Autowired
	private PlayerManager playerManager;

	private void fix(Player player) {
		List<String> fixRemoves = New.arrayList();
		for (Quest quest : player.getQuestPool().getQuests().values()) {
			QuestResource resource = questResources.get(quest.getId(), false);
			if (resource == null) {
				fixRemoves.add(quest.getId());
				logger.error(String.format("任务配置表，任务[%s]丢失，自动删除！", quest.getId()));
			}
		}
		for (String removeId : fixRemoves) {
			player.getQuestPool().getQuests().remove(removeId);
		}

		fixRemoves.clear();
		for (String id : player.getQuestPool().getAcceptHistory().keySet()) {
			QuestResource resource = questResources.get(id, false);
			if (resource == null) {
				fixRemoves.add(id);
				logger.error(String.format("任务配置表，任务[%s]丢失，自动删除！", id));
			}
		}
		for (String removeId : fixRemoves) {
			player.getQuestPool().getAcceptHistory().remove(removeId);
		}

		fixRemoves.clear();
		for (String id : player.getQuestPool().getTodayCompletionHistory().keySet()) {
			QuestResource resource = questResources.get(id, false);
			if (resource == null) {
				fixRemoves.add(id);
				logger.error(String.format("任务配置表，任务[%s]丢失，自动删除！", id));
			}
		}
		for (String removeId : fixRemoves) {
			player.getQuestPool().getTodayCompletionHistory().remove(removeId);
		}

		fixRemoves.clear();
		for (String id : player.getQuestPool().getCompletionHistory().keySet()) {
			QuestResource resource = questResources.get(id, false);
			if (resource == null) {
				fixRemoves.add(id);
				logger.error(String.format("任务配置表，任务[%s]丢失，自动删除！", id));
			}
		}
		for (String removeId : fixRemoves) {
			player.getQuestPool().getCompletionHistory().remove(removeId);
		}

		fixRemoves.clear();
		for (String id : player.getQuestPool().getTodayAcceptHistory().keySet()) {
			QuestResource resource = questResources.get(id, false);
			if (resource == null) {
				fixRemoves.add(id);
				logger.error(String.format("任务配置表，任务[%s]丢失，自动删除！", id));
			}
		}
		for (String removeId : fixRemoves) {
			player.getQuestPool().getTodayAcceptHistory().remove(removeId);
		}

	}

	public void doLoginRefresh(Player player) {
		doLoginRefresh(player, true);
	}

	public void doLoginRefresh(Player player, boolean send) {
		long now = System.currentTimeMillis();
		QuestUpdate update = QuestUpdate.valueOf();
		if (Debug.debug) {
			fix(player);
		}
		// 首先删除过期的任务
		player.getQuestPool().removeQuests(update, player, player.getQuestPool().getQuests().values(), now);
		// 刷新自己所有任务的状态
		player.getQuestPool().refreshQuest(player.getQuestPool().getQuests().values(), false, false, false);
		List<String> ids = New.arrayList();
		for (QuestResource resource : questResources.getAll()) {
			if (!player.getQuestPool().getClientAccept().contains(resource.getId())) {
				ids.add(resource.getId());
			}
		}

		if (send && !update.getQuests().isEmpty()) {
			PacketSendUtility.sendPacket(player, update.createVO());
		}
		// 尝试接取所有的任务
		List<Quest> newQuests = player.getQuestPool().acceptQuests(update, player, ids, now);
		if (!newQuests.isEmpty()) {
			player.getQuestPool().refreshQuest(newQuests);
		}
	}

	public void loginRefresh(LoginEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		doLoginRefresh(player, false);
	}

	public Collection<QuestResource> getAllResources() {
		return questResources.getAll();
	}

	public Map<KeyType, QuestKeyHandle> getHandles() {
		return handles;
	}

}
