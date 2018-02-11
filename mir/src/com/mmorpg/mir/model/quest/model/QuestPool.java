package com.mmorpg.mir.model.quest.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Transient;

import org.apache.commons.lang.ArrayUtils;
import org.cliffc.high_scale_lib.NonBlockingHashMap;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.h2.util.New;

import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.action.CoreActionManager;
import com.mmorpg.mir.model.core.action.CoreActions;
import com.mmorpg.mir.model.core.condition.quest.TodayNoCompleteTypeQuestCondition;
import com.mmorpg.mir.model.gameobjects.Npc;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.quest.event.QuestCompletEvent;
import com.mmorpg.mir.model.quest.event.QuestCompletRewardEvent;
import com.mmorpg.mir.model.quest.manager.QuestManager;
import com.mmorpg.mir.model.quest.packet.SM_ClientQuest_Add;
import com.mmorpg.mir.model.quest.packet.SM_QuestUpdateVO;
import com.mmorpg.mir.model.quest.packet.SM_Quest_Complete;
import com.mmorpg.mir.model.quest.packet.SM_RandomQuest_CompleteTime;
import com.mmorpg.mir.model.quest.resource.QuestResource;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.trigger.manager.TriggerManager;
import com.mmorpg.mir.model.trigger.model.TriggerContextKey;
import com.mmorpg.mir.model.utils.MathUtil;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.windforce.common.event.core.EventBusManager;
import com.windforce.common.utility.DateUtils;

public class QuestPool {

	@Transient
	private Player owner;
	/** 当前的任务列表 */
	private NonBlockingHashMap<String, Quest> quests = new NonBlockingHashMap<String, Quest>();
	/** 帮助前端选取可以接取的任务 */
	private List<String> clientAccept = New.arrayList();
	/** 接取任务记录 */
	private NonBlockingHashMap<String, Integer> acceptHistory = new NonBlockingHashMap<String, Integer>();
	/** 完成任务记录 */
	private NonBlockingHashMap<String, Integer> completionHistory = new NonBlockingHashMap<String, Integer>();
	/** 今日接取任务记录 */
	private NonBlockingHashMap<String, Integer> todayAcceptHistory = new NonBlockingHashMap<String, Integer>();
	/** 今日完成任务记录 */
	private NonBlockingHashMap<String, Integer> todayCompletionHistory = new NonBlockingHashMap<String, Integer>();
	/** 最后完成一次完成游历任务的时间 */
	private long lastCompletedRandomQuestTime;
	/** 最后刷新时间 */
	private long lastRefreshTime;

	/**
	 * 尝试接取任务
	 * 
	 * @param ids
	 */
	@JsonIgnore
	public List<Quest> acceptQuests(QuestUpdate update, Player player, List<String> ids, long now) {
		List<String> dest = new ArrayList<String>(ids.size());
		dest.addAll(ids);
		Collections.shuffle(dest, MathUtil.getRandom());
		return this.acceptQuests(update, player, dest, now, false, false);
	}

	/**
	 * 
	 * @param update
	 * @param player
	 * @param ids
	 * @param now
	 * @param isThrowException
	 *            验证不通过是否抛出异常
	 * @return
	 */
	@JsonIgnore
	synchronized public List<Quest> acceptQuests(QuestUpdate update, Player player, List<String> ids, long now,
			boolean clientAccpet, boolean trigger) {
		List<Quest> newQuests = New.arrayList();
		for (String id : ids) {
			if (this.quests.containsKey(id)) {
				continue;
			}

			QuestResource resource = QuestManager.staticQuestResources.get(id, true);
			if (trigger != resource.isTriggerType()) {
				continue;
			}

			if (resource.getAcceptConditions().verify(owner, clientAccpet)) {
				if (resource.isAutoType() || clientAccpet || trigger) {
					Quest quest = QuestManager.getInstance().createQuest(resource, player, now);
					this.quests.put(quest.getId(), quest);
					this.removeClientAccept(id);
					this.addTodayAcceptCount(id);
					update.getQuests().put(id, quest);
					newQuests.add(quest);
					if (!ArrayUtils.isEmpty(resource.getAcceptTriggers())) {
						for (String triggerId : resource.getAcceptTriggers()) {
							TriggerManager.getInstance().tiggerQuest(owner, quest, triggerId);
						}
					}
				} else if (!resource.isTriggerType()) {
					addClientAccept(id);
				}
			}
		}
		return newQuests;
	}

	@JsonIgnore
	public boolean isContainsQuestType(QuestType type) {
		for (Quest quest : quests.values()) {
			if (quest.getResource().getType() == type) {
				return true;
			}
		}
		return false;
	}

	@JsonIgnore
	public void addAcceptCount(String id) {
		if (!acceptHistory.containsKey(id)) {
			acceptHistory.put(id, 1);
		} else {
			acceptHistory.put(id, acceptHistory.get(id) + 1);
		}
	}

	/**
	 * 添加前端可接取的任务
	 * 
	 * @param id
	 */
	@JsonIgnore
	public void addClientAccept(String id) {
		if (this.clientAccept.contains(id)) {
			return;
		}
		this.clientAccept.add(id);
		PacketSendUtility.sendPacket(owner,
				SM_ClientQuest_Add.valueOf(QuestManager.getInstance().convertTemplateId(id)));
	}

	@JsonIgnore
	public void addCompleteCount(String id, int count) {
		if (!completionHistory.containsKey(id)) {
			completionHistory.put(id, count);
		} else {
			completionHistory.put(id, completionHistory.get(id) + count);
		}
		PacketSendUtility.sendPacket(owner,
				SM_Quest_Complete.valueOf(QuestManager.getInstance().convertTemplateId(id), count));
	}

	@JsonIgnore
	public void addTodayAcceptCount(String id) {
		if (!todayAcceptHistory.containsKey(id)) {
			todayAcceptHistory.put(id, 1);
		} else {
			todayAcceptHistory.put(id, todayAcceptHistory.get(id) + 1);
		}
		this.addAcceptCount(id);
	}

	@JsonIgnore
	public void addTodayCompleteCount(String id, int count) {
		if (!todayCompletionHistory.containsKey(id)) {
			todayCompletionHistory.put(id, count);
		} else {
			todayCompletionHistory.put(id, todayCompletionHistory.get(id) + count);
		}
		this.addCompleteCount(id, count);
		if (QuestManager.staticQuestResources.get(id, true).getType() == QuestType.RANDOM) {
			lastCompletedRandomQuestTime = System.currentTimeMillis();
			PacketSendUtility.sendPacket(owner, SM_RandomQuest_CompleteTime.valueOf(lastCompletedRandomQuestTime));
		}
		EventBusManager.getInstance().submit(QuestCompletEvent.valueOf(owner.getObjectId(), id, count));
	}

	/**
	 * 清理今天的完成记录
	 */
	@JsonIgnore
	public void clearTodayQuest() {
		if (!DateUtils.isToday(new Date(lastRefreshTime))) {
			lastRefreshTime = System.currentTimeMillis();
			lastCompletedRandomQuestTime = 0;
			todayAcceptHistory.clear();
			todayCompletionHistory.clear();
		}
	}

	/**
	 * 客服端完成的任务
	 * 
	 * @param id
	 */
	@JsonIgnore
	public void clientComplete(String id) {
		if (!this.quests.containsKey(id) || this.quests.get(id).getPhase() != QuestPhase.INCOMPLETE) {
			throw new ManagedException(ManagedErrorCode.QUEST_NOT_FOUND);
		}
		Quest quest = this.quests.get(id);
		if (!quest.getResource().isClientComplete()) {
			throw new ManagedException(ManagedErrorCode.QUEST_COMPLETE_ERROR_REQUEST);
		}
		refreshQuest(Arrays.asList(quest), true, true, false);
		QuestManager.getInstance().doLoginRefresh(owner);

	}

	public SM_QuestUpdateVO createVO() {
		QuestUpdate update = new QuestUpdate();
		for (Quest quest : quests.values()) {
			update.getQuests().put(quest.getId(), quest);
		}
		SM_QuestUpdateVO vo = update.createVO();
		vo.setShortClientAccepts(QuestManager.getInstance().convertTemplateId(clientAccept));
		vo.setShortCompletionHistory(QuestManager.getInstance().convertTemplateId(completionHistory));
		vo.setShortTodayCompletionHistory(QuestManager.getInstance().convertTemplateId(todayCompletionHistory));
		return vo;
	}

	@JsonIgnore
	synchronized public void dayCompleteAll(String id) {
		if (!this.quests.containsKey(id)) {
			throw new ManagedException(ManagedErrorCode.QUEST_NOT_FOUND);
		}
		Quest quest = this.quests.get(id);
		if (quest.getResource().getAtOnceCompletAct() == null || quest.getResource().getType() != QuestType.DAY) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		TodayNoCompleteTypeQuestCondition cntqc = quest.getResource().getAcceptConditions()
				.findConditionType(TodayNoCompleteTypeQuestCondition.class);
		int need = cntqc.getValue() - getTodayTypeCompleteCount(QuestType.DAY);
		CoreActions actions = CoreActionManager.getInstance().getCoreActions(need,
				quest.getResource().getAtOnceCompletAct());
		actions.verify(owner, true);
		actions.act(owner, ModuleInfo.valueOf(ModuleType.QUEST, SubModuleType.DAILY_QUEST_ACT));
		quest.fullStar();
		if (need > 0) {
			List<String> allRewardIds = New.arrayList();
			List<String> rewardIds = null;
			for (int i = 0; i < need; i++) {
				addTodayCompleteCount(id, 1);
				if (quest.getResource().getRewardChooserGroupId() != null) {
					rewardIds = ChooserManager.getInstance().chooseValueByRequire(quest,
							quest.getResource().getRewardChooserGroupId());
				} else {
					rewardIds = New.arrayList();
				}
				allRewardIds.addAll(rewardIds);
			}
			Map<String, Object> params = RewardManager.getInstance().getRewardParams(owner);
			RewardManager.getInstance().grantReward(owner, allRewardIds,
					ModuleInfo.valueOf(ModuleType.QUEST, SubModuleType.DAILY_QUEST_REWARD), params);
			EventBusManager.getInstance().submit(QuestCompletEvent.valueOf(owner.getObjectId(), id, 1));
		}
		QuestUpdate update = new QuestUpdate();
		removeQuest(Arrays.asList(quest.getId()));
		update.getQuests().put(quest.getId(), null);
		// 推送更新
		PacketSendUtility.sendPacket(getOwner(), update.createVO());
		QuestManager.getInstance().doLoginRefresh(owner);
	}

	public Map<String, Integer> getAcceptHistory() {
		return acceptHistory;
	}

	public List<String> getClientAccept() {
		return clientAccept;
	}

	public Map<String, Integer> getCompletionHistory() {
		return completionHistory;
	}

	public long getLastRefreshTime() {
		return lastRefreshTime;
	}

	@JsonIgnore
	public Player getOwner() {
		return owner;
	}

	public Map<String, Quest> getQuests() {
		return quests;
	}

	public Map<String, Integer> getTodayAcceptHistory() {
		return todayAcceptHistory;
	}

	public Map<String, Integer> getTodayCompletionHistory() {
		return todayCompletionHistory;
	}

	@JsonIgnore
	public int getTodayTypeCompleteCount(QuestType type) {
		int sum = 0;
		for (Entry<String, Integer> entry : getTodayCompletionHistory().entrySet()) {
			QuestResource resource = QuestManager.staticQuestResources.get(entry.getKey(), true);
			if (resource.getType() == type) {
				sum += entry.getValue();
			}
		}
		return sum;
	}

	@JsonIgnore
	public int getTypeCompleteCount(QuestType type) {
		int sum = 0;
		for (Entry<String, Integer> entry : getCompletionHistory().entrySet()) {
			QuestResource resource = QuestManager.staticQuestResources.get(entry.getKey(), true);
			if (resource.getType() == type) {
				sum += entry.getValue();
			}
		}
		return sum;
	}

	@JsonIgnore
	synchronized public void giveUp(String id) {
		Quest quest = this.quests.get(id);
		if (quest == null) {
			return;
		}
		if (!quest.canGiveUp()) {
			return;
		}
		removeQuest(Arrays.asList(id));

		if (!ArrayUtils.isEmpty(quest.getResource().getGiveUpTriggers())) {
			for (String triggerId : quest.getResource().getGiveUpTriggers()) {
				TriggerManager.getInstance().tiggerQuest(owner, quest, triggerId);
			}
		}

		// 释放任务怪
		if (quest.getQuestMonsters() != null) {
			for (Npc npc : quest.getQuestMonsters()) {
				npc.getController().delete();
			}
			quest.getQuestMonsters().clear();
		}

		QuestUpdate update = new QuestUpdate();
		update.getQuests().put(id, null);
		PacketSendUtility.sendPacket(owner, update.createVO());
		// 子任务
		if (quest.getResource().getChildTasks() != null && quest.getResource().getChildTasks().length != 0) {
			List<String> childQuest = New.arrayList();
			childQuest.addAll(Arrays.asList(quest.getResource().getChildTasks()));
			List<Quest> newQuests = acceptQuests(update, owner, childQuest, System.currentTimeMillis());
			refreshQuest(newQuests);
		}
	}

	@JsonIgnore
	public void goldComplete(String id) {
		if (!this.quests.containsKey(id)) {
			throw new ManagedException(ManagedErrorCode.QUEST_NOT_FOUND);
		}
		if (this.quests.get(id).getPhase() != QuestPhase.INCOMPLETE) {
			if (quests.get(id).getResource().getType() == QuestType.RANDOM) {
				goldCompleteRandomQuest(id);
				return;
			} else {
				throw new ManagedException(ManagedErrorCode.QUEST_NOT_FOUND);
			}
		}
		Quest quest = this.quests.get(id);
		if (quest.getResource().getAtOnceCompletAct() == null) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		CoreActions actions = CoreActionManager.getInstance().getCoreActions(1,
				quest.getResource().getAtOnceCompletAct());
		actions.verify(owner, true);
		actions.act(owner, ModuleInfo.valueOf(ModuleType.QUEST, SubModuleType.QUEST_GOLD_COMPLETE));
		refreshQuest(Arrays.asList(quest), true, true, true);
		QuestManager.getInstance().doLoginRefresh(owner);
	}

	synchronized public void goldCompleteRandomQuest(String id) {
		Quest quest = this.quests.get(id);
		if (quest == null) {
			return;
		}
		if (quest.getPhase() == QuestPhase.INCOMPLETE) {
			throw new ManagedException(ManagedErrorCode.QUEST_INCOMPLETE);
		}

		if (quest.getResource().getAtOnceCompletAct() == null) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		CoreActions actions = CoreActionManager.getInstance().getCoreActions(1,
				quest.getResource().getAtOnceCompletAct());
		actions.verify(owner, true);
		actions.act(owner, ModuleInfo.valueOf(ModuleType.QUEST, SubModuleType.QUEST_GOLD_COMPLETE));

		reward(id);
	}

	@JsonIgnore
	public void levelUpStar(String id) {
		if (!this.quests.containsKey(id)) {
			throw new ManagedException(ManagedErrorCode.QUEST_NOT_FOUND);
		}
		Quest quest = this.quests.get(id);
		if (quest.getResource().getFullStarAct() == null || quest.getResource().getType() != QuestType.DAY) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		CoreActions actions = CoreActionManager.getInstance().getCoreActions(1, quest.getResource().getFullStarAct());
		actions.verify(owner, true);
		actions.act(owner, ModuleInfo.valueOf(ModuleType.QUEST, SubModuleType.QUEST_STAR_UPGRADE));
		quest.fullStar();
		refreshQuest(Arrays.asList(quest), true, false, false);
	}

	public void refreshQuest(Collection<Quest> quests) {
		this.refreshQuest(quests, true, false, false);
	}

	/**
	 * 刷新任务
	 * 
	 * @param player
	 * @param quests
	 * @param keyRefresh
	 *            一定要刷新到前端并且通知
	 * @param clientComplet
	 *            提供给客服端任务完成
	 * @param goldComplet
	 *            使用元宝完成
	 */
	synchronized public void refreshQuest(Collection<Quest> quests, boolean keyRefresh, boolean clientComplet,
			boolean goldComplet) {
		long now = System.currentTimeMillis();
		clearTodayQuest();
		List<String> childQuest = New.arrayList();
		List<String> completeRemoveIds = New.arrayList();
		QuestUpdate update = new QuestUpdate();
		for (Quest quest : quests) {
			if (keyRefresh) {
				update.getQuests().put(quest.getId(), quest);
			}
			QuestPhase oldPhase = quest.getPhase();
			boolean needCheckComplete = (oldPhase == QuestPhase.INCOMPLETE && !quest.getResource().isClientComplete());
			if (needCheckComplete || clientComplet) {
				// 达成完成条件
				if (goldComplet || quest.canCompletion()) {
					quest.setPhase(QuestPhase.COMPLETE);
					update.getQuests().put(quest.getId(), quest);
					oldPhase = QuestPhase.COMPLETE;
				}
			}

			if (oldPhase == QuestPhase.COMPLETE && quest.getResource().isAutoReward()) {
				if (quest.getResource().isAutoReward()) {
					completeRemoveIds.add(quest.getId());
				}
			}

			if (oldPhase == QuestPhase.INCOMPLETE) {
				if (quest.canFail()) {
					quest.setPhase(QuestPhase.FAIL);
					update.getQuests().put(quest.getId(), quest);
					oldPhase = QuestPhase.FAIL;
					// 失败触发器
					if (!ArrayUtils.isEmpty(quest.getResource().getFailTriggers())) {
						Map<String, Object> contexts = New.hashMap();
						contexts.put(TriggerContextKey.PLAYER, getOwner());
						for (String triggerId : quest.getResource().getFailTriggers()) {
							TriggerManager.getInstance().tiggerQuest(quest.getOwner(), quest, triggerId);
						}
					}
				}
			}

		}
		for (String id : completeRemoveIds) {
			addTodayCompleteCount(id, 1);
			Quest quest = removeQuest(Arrays.asList(id)).get(0);
			if (quest != null && quest.getResource().getChildTasks() != null) {
				childQuest.addAll(Arrays.asList(quest.getResource().getChildTasks()));
			}
			if (quest.getResource().getRewardChooserGroupId() != null) {
				if (quest.getResource().getType() == QuestType.DAY) {
					// 日常发奖
					EventBusManager.getInstance().submit(
							QuestCompletRewardEvent.valueOf(getOwner().getObjectId(), quest, quest.getResource()
									.getType(), quest.getResource().getRewardChooserGroupId()));
				} else {
					// 自动发奖
					List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(quest,
							quest.getResource().getRewardChooserGroupId());
					Map<String, Object> params = RewardManager.getInstance().getRewardParams(owner);
					RewardManager.getInstance().grantReward(getOwner(), rewardIds,
							ModuleInfo.valueOf(ModuleType.QUEST, SubModuleType.QUEST_REWARD), params);
				}
			}
			update.getQuests().put(quest.getId(), null);
			if (!ArrayUtils.isEmpty(quest.getResource().getCompleteTriggers())) {
				// 完成触发器
				Map<String, Object> contexts = New.hashMap();
				contexts.put(TriggerContextKey.PLAYER, getOwner());
				contexts.put(TriggerContextKey.QUEST, quest);
				for (String triggerId : quest.getResource().getCompleteTriggers()) {
					TriggerManager.getInstance().trigger(contexts, triggerId);
				}
			}
		}
		// 尝试接取子任务
		List<Quest> newQuests = acceptQuests(update, getOwner(), childQuest, now);

		if (!update.getQuests().isEmpty()) {
			// 推送更新
			PacketSendUtility.sendPacket(getOwner(), update.createVO());
		}
		// 检测接受的新任务是否能够完成
		if (!newQuests.isEmpty()) {
			refreshQuest(newQuests, false, false, false);
		}
	}

	/**
	 * 删除前端可接取的任务
	 * 
	 * @param id
	 */
	@JsonIgnore
	public void removeClientAccept(String id) {
		if (this.clientAccept.contains(id)) {
			this.clientAccept.remove(id);
			// PacketSendUtility.sendPacket(owner,
			// SM_ClientQuest_Remove.valueOf(id));
		}
	}

	@JsonIgnore
	private List<Quest> removeQuest(List<String> removeIds) {
		List<Quest> removes = new ArrayList<Quest>();
		for (String id : removeIds) {
			Quest quest = this.quests.remove(id);
			if (quest != null) {
				removes.add(quest);
			}
		}
		return removes;
	}

	@JsonIgnore
	synchronized public void removeQuests(QuestUpdate update, Player player, Collection<Quest> quests, long now) {
		List<String> removeIds = New.arrayList();
		for (Quest quest : quests) {
			if (quest.canRemove()) {
				removeIds.add(quest.getId());
				update.getQuests().put(quest.getId(), null);
			}
		}
		this.removeQuest(removeIds);
	}

	/**
	 * 领奖
	 * 
	 * @param id
	 * @return
	 */
	@JsonIgnore
	synchronized public void reward(String id) {
		Quest quest = this.quests.get(id);
		if (quest == null) {
			return;
		}
		if (quest.getPhase() == QuestPhase.INCOMPLETE) {
			throw new ManagedException(ManagedErrorCode.QUEST_INCOMPLETE);
		}
		List<String> rewardIds = null;
		if (quest.getResource().getRewardChooserGroupId() != null) {
			if (quest.getResource().getType() == QuestType.DAY) {
				// 日常任务发奖
				EventBusManager.getInstance().submit(
						QuestCompletRewardEvent.valueOf(owner.getObjectId(), quest, quest.getResource().getType(),
								quest.getResource().getRewardChooserGroupId()));
			} else {
				rewardIds = ChooserManager.getInstance().chooseValueByRequire(quest,
						quest.getResource().getRewardChooserGroupId());
			}
		} else {
			rewardIds = New.arrayList();
		}
		addTodayCompleteCount(id, 1);
		removeQuest(Arrays.asList(id));
		QuestUpdate update = new QuestUpdate();
		update.getQuests().put(id, null);
		PacketSendUtility.sendPacket(owner, update.createVO());
		if (!ArrayUtils.isEmpty(quest.getResource().getCompleteTriggers())) {
			// 完成触发器
			Map<String, Object> contexts = New.hashMap();
			contexts.put(TriggerContextKey.PLAYER, getOwner());
			contexts.put(TriggerContextKey.QUEST, quest);
			for (String triggerId : quest.getResource().getCompleteTriggers()) {
				TriggerManager.getInstance().trigger(contexts, triggerId);
			}
		}

		if (rewardIds != null && !rewardIds.isEmpty()) {
			Map<String, Object> params = RewardManager.getInstance().getRewardParams(owner);
			RewardManager.getInstance().grantReward(owner, rewardIds,
					ModuleInfo.valueOf(ModuleType.QUEST, SubModuleType.QUEST_REWARD), params);
		}

		// 子任务
		if (quest.getResource().getChildTasks() != null && quest.getResource().getChildTasks().length != 0) {
			List<String> childQuest = New.arrayList();
			childQuest.addAll(Arrays.asList(quest.getResource().getChildTasks()));
			List<Quest> newQuests = acceptQuests(update, owner, childQuest, System.currentTimeMillis());
			refreshQuest(newQuests);
		}

		// QuestManager.getInstance().doLoginRefresh(owner);

	}

	public void setAcceptHistory(NonBlockingHashMap<String, Integer> acceptHistory) {
		this.acceptHistory = acceptHistory;
	}

	public void setClientAccept(List<String> clientAccept) {
		this.clientAccept = clientAccept;
	}

	public void setCompletionHistory(NonBlockingHashMap<String, Integer> completionHistory) {
		this.completionHistory = completionHistory;
	}

	public void setLastRefreshTime(long lastRefreshTime) {
		this.lastRefreshTime = lastRefreshTime;
	}

	@JsonIgnore
	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public void setQuests(NonBlockingHashMap<String, Quest> quests) {
		this.quests = quests;
	}

	public void setTodayAcceptHistory(NonBlockingHashMap<String, Integer> todayAcceptHistory) {
		this.todayAcceptHistory = todayAcceptHistory;
	}

	public void setTodayCompletionHistory(NonBlockingHashMap<String, Integer> todayCompletionHistory) {
		this.todayCompletionHistory = todayCompletionHistory;
	}

	public long getLastCompletedRandomQuestTime() {
		return lastCompletedRandomQuestTime;
	}

	public void setLastCompletedRandomQuestTime(long lastCompletedRandomQuestTime) {
		this.lastCompletedRandomQuestTime = lastCompletedRandomQuestTime;
	}

}
