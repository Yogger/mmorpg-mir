package com.mmorpg.mir.model.quest.manager;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.ResourceReload;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.quest.model.Quest;
import com.mmorpg.mir.model.quest.model.QuestPhase;
import com.mmorpg.mir.model.quest.model.QuestUpdate;
import com.mmorpg.mir.model.quest.resource.QuestResource;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.windforce.common.event.core.EventBusManager;
import com.windforce.common.event.event.IEvent;
import com.windforce.common.utility.JsonUtils;
import com.windforce.common.utility.New;

@Component
public class QuestEventManager implements ResourceReload, IQuestEventManager {
	private static final Logger logger = Logger.getLogger(QuestEventManager.class);
	private Map<Class<?>, List<String>> acceptEventQuests;

	private Map<Class<?>, List<String>> completeEventQuests;

	private Map<Class<?>, List<String>> failEventQuests;

	private Map<Class<?>, List<String>> removeEventQuests;

	@Autowired
	private PlayerManager playerManager;
	@Autowired
	private EventBusManager eventBusManager;

	@Autowired
	private QuestManager questManager;

	private static QuestEventManager self;

	public static QuestEventManager getInstance() {
		return self;
	}

	@PostConstruct
	public void init() {
		self = this;
		// reload();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void reload() {
		try {
			synchronized (this) {
				Map<Class<?>, List<String>> acceptEventQuestsTemp = New.hashMap();
				Map<Class<?>, List<String>> completeEventQuestsTemp = New.hashMap();
				Map<Class<?>, List<String>> failEventQuestsTemp = New.hashMap();
				Map<Class<?>, List<String>> removeEventQuestsTemp = New.hashMap();
				Set<Class<?>> clazzes = new HashSet<Class<?>>();
				for (QuestResource resource : questManager.getAllResources()) {
					for (Class<?> clazz : resource.getAllAcceptEvent()) {
						if (!acceptEventQuestsTemp.containsKey(clazz)) {
							acceptEventQuestsTemp.put(clazz, new ArrayList<String>());
						}
						if (acceptEventQuestsTemp.get(clazz).contains(resource.getId())) {
							logger.error(String.format("resourceId[%s] event[%s]重复！", resource.getId(), clazz));
							throw new RuntimeException(String.format("resourceId[%s] event[%s]重复", resource.getId(),
									clazz));
						} else {
							acceptEventQuestsTemp.get(clazz).add(resource.getId());
						}
						if (!clazzes.contains(clazz)) {
							clazzes.add(clazz);
						}
					}
					for (Class<?> clazz : resource.getAllCompleteEvent()) {
						if (!completeEventQuestsTemp.containsKey(clazz)) {
							completeEventQuestsTemp.put(clazz, new ArrayList<String>());
						}
						completeEventQuestsTemp.get(clazz).add(resource.getId());
						if (!clazzes.contains(clazz)) {
							clazzes.add(clazz);
						}
					}
					for (Class<?> clazz : resource.getAllFailEvent()) {
						if (!failEventQuestsTemp.containsKey(clazz)) {
							failEventQuestsTemp.put(clazz, new ArrayList<String>());
						}
						failEventQuestsTemp.get(clazz).add(resource.getId());
						if (!clazzes.contains(clazz)) {
							clazzes.add(clazz);
						}
					}
					for (Class<?> clazz : resource.getAllRemoveEvent()) {
						if (!removeEventQuestsTemp.containsKey(clazz)) {
							removeEventQuestsTemp.put(clazz, new ArrayList<String>());
						}
						removeEventQuestsTemp.get(clazz).add(resource.getId());
						if (!clazzes.contains(clazz)) {
							clazzes.add(clazz);
						}
					}
				}

				acceptEventQuests = acceptEventQuestsTemp;
				completeEventQuests = completeEventQuestsTemp;
				failEventQuests = failEventQuestsTemp;
				removeEventQuests = removeEventQuestsTemp;

				// logger.error(JsonUtils.object2String(acceptEventQuests.get(PromotionEvent.class)));
				// logger.error("size[" + getSize(acceptEventQuests) +
				// "]acceptEventQuests");
				// logger.error("size[" + getSize(completeEventQuests) +
				// "]completeEventQuests");
				// logger.error("size[" + getSize(failEventQuests) +
				// "]failEventQuests");
				// logger.error("size[" + getSize(removeEventQuests) +
				// "]removeEventQuests");

				Method method = QuestEventManager.class.getMethod("doEventRefreshQuest", IEvent.class);
				for (Class<?> clazz : clazzes) {
					eventBusManager.registReceiver(this, method, (Class<? extends IEvent>) clazz);
				}
			}
		} catch (Exception e) {
			logger.error("初始化任务事件接收器失败", e);
			throw new RuntimeException("初始化任务事件接收器失败");
		}

	}

	@SuppressWarnings("unused")
	private int getSize(Map<Class<?>, List<String>> map) {
		int size = 0;
		for (Entry<Class<?>, List<String>> entry : map.entrySet()) {
			size += entry.getValue().size();
		}
		return size;
	}

	public void doEventRefreshQuest(IEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		player.getQuestPool().clearTodayQuest();
		List<String> acceptIds = acceptEventQuests.get(event.getClass());
		long now = System.currentTimeMillis();
		List<Quest> quests = New.arrayList();
		QuestUpdate update = QuestUpdate.valueOf();

		// 删除任务
		List<Quest> removeQuests = New.arrayList();
		List<String> removeIds = removeEventQuests.get(event.getClass());
		if (removeIds != null && !removeIds.isEmpty()) {
			for (Quest quest : player.getQuestPool().getQuests().values()) {
				if (removeIds.contains(quest.getId())) {
					removeQuests.add(quest);
				}
			}
		}
		player.getQuestPool().removeQuests(update, player, removeQuests, now);

		// 接取
		if (acceptIds != null && !acceptIds.isEmpty()) {
			quests.addAll(player.getQuestPool().acceptQuests(update, player, acceptIds, now));
		}

		if (!update.getQuests().isEmpty()) {
			// 推送更新
			PacketSendUtility.sendPacket(player, update.createVO());
		}

		// 完成
		List<String> completeIds = completeEventQuests.get(event.getClass());
		if (completeIds != null && !completeIds.isEmpty()) {
			for (Quest quest : player.getQuestPool().getQuests().values()) {
				if (quest.getPhase() == QuestPhase.INCOMPLETE && completeIds.contains(quest.getId())
						&& !quests.contains(quest)) {
					quests.add(quest);
				}
			}
		}

		// 失败
		List<String> failIds = failEventQuests.get(event.getClass());
		if (failIds != null && !failIds.isEmpty()) {
			for (Quest quest : player.getQuestPool().getQuests().values()) {
				if (quest.getPhase() == QuestPhase.INCOMPLETE && failIds.contains(quest.getId())
						&& !quests.contains(quest)) {
					quests.add(quest);
				}
			}
		}

		// 刷新任务
		player.getQuestPool().refreshQuest(quests, false, false, false);
	}

	@Override
	public Class<?> getResourceClass() {
		return QuestResource.class;
	}

	public static void shuffleCopy(List<String> list) {
		try {
			List<String> dest = new ArrayList<String>();
			dest.addAll(list);
			// Collections.copy(dest, list);
			Collections.shuffle(dest, new Random());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	synchronized public static void shuffleFixSyn(List<String> list) {
		Collections.shuffle(list);
	}

	public static void shuffle(List<String> list) {
		Collections.shuffle(list, new Random());
	}

	public static void main(String[] args) {
		final List<String> ss = new LinkedList<String>();

		for (int i = 0; i < 100; i++) {
			ss.add("" + i);
		}
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		CompletionService<Integer> completionService = new ExecutorCompletionService<Integer>(executorService);
		int count = 2000000;
		long now = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			completionService.submit(new Callable<Integer>() {

				@Override
				public Integer call() throws Exception {
					new LinkedList<String>(ss);
					for (int i = 0; i < ss.size(); i++) {
						ss.get(i);
					}
					return 0;
				}
			});
		}

		for (int i = 0; i < count; i++) {
			try {
				completionService.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println(System.currentTimeMillis() - now);
		System.out.println(ss.size());
		System.out.println(JsonUtils.object2String(ss));
		System.exit(0);
	}
}
