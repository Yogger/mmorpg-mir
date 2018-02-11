package com.mmorpg.mir.model.resourcecheck.handle;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.h2.util.New;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chooser.model.sample.Chooser;
import com.mmorpg.mir.model.chooser.model.sample.ChooserGroup;
import com.mmorpg.mir.model.chooser.model.sample.Item;
import com.mmorpg.mir.model.chooser.model.sample.ItemGroup;
import com.mmorpg.mir.model.core.condition.AbstractCoreCondition;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditionType;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.core.condition.LevelCondition;
import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.core.condition.quest.CompleteQuestCondition;
import com.mmorpg.mir.model.core.condition.quest.QuestKeyValueCondition;
import com.mmorpg.mir.model.quest.resource.AccpetType;
import com.mmorpg.mir.model.quest.resource.QuestKeyResource;
import com.mmorpg.mir.model.quest.resource.QuestResource;
import com.mmorpg.mir.model.resourcecheck.ResourceCheckHandle;
import com.mmorpg.mir.model.reward.model.sample.RewardSample;
import com.mmorpg.mir.model.trigger.resource.TriggerResource;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;
import com.windforce.common.utility.JsonUtils;

@Component
public class QuestResourceCheck extends ResourceCheckHandle {
	@Static
	private Storage<String, QuestResource> questResource;

	@Static
	private Storage<String, TriggerResource> triggerResources;

	@Static
	private Storage<String, ChooserGroup> chooserGroups;

	@Static
	private Storage<String, Chooser> choosers;

	@Static
	private Storage<String, RewardSample> rewardSamples;

	@Override
	public Class<?> getResourceClass() {
		return QuestResource.class;
	}

	@Override
	public void check() {
		List<String> errorMessage = New.arrayList();
		for (QuestResource rs : questResource.getAll()) {

			if (rs.getChildTasks() != null) {
				for (String id : rs.getChildTasks()) {
					QuestResource nullQuest = questResource.get(id, false);
					if (nullQuest == null) {
						String log = String.format("QuestResource id[%s] 包含子任务 getChildTasks[%s] id[%s]不存在！",
								rs.getId(), JsonUtils.object2String(rs.getChildTasks()), id);
						errorMessage.add(log);
						// throw new RuntimeException(log);
					}
				}
			}
			if (rs.getAcceptTriggers() != null) {
				for (String id : rs.getAcceptTriggers()) {
					triggerResources.get(id, true);
				}
			}
			if (rs.getCompleteTriggers() != null) {
				for (String id : rs.getCompleteTriggers()) {
					triggerResources.get(id, true);
				}
			}
			if (rs.getRewardChooserGroupId() != null) {
				ChooserGroup chooserGroup = chooserGroups.get(rs.getRewardChooserGroupId(), true);
				for (String chooserId : chooserGroup.getValueChoosers()) {
					Chooser chooser = choosers.get(chooserId, true);
					for (ItemGroup itemGroup : chooser.getItemGroups()) {
						for (Item item : itemGroup.getItems()) {
							RewardSample rewardSample = rewardSamples.get(item.getValue(), false);
							if (rewardSample == null) {
								String log = String
										.format("QuestResource id[%s],rewardChooserGroupId[%s] chooser[%s] 包含奖励不存在。rewardId[%s]",
												rs.getId(), rs.getRewardChooserGroupId(), chooserId, item.getValue());
								errorMessage.add(log);
								// throw new RuntimeException();
							}
						}
					}
				}
			}
			CoreConditions completeConditions = CoreConditionManager.getCoreConditionResources(1, rs.getComplete());
			for (AbstractCoreCondition acc : completeConditions.getConditionList()) {
				if (acc instanceof QuestKeyValueCondition) {
					if (rs.getKeys() == null || rs.getKeys().length == 0) {
						String log = String.format(
								"QuestResource id[%s] 包含QuestKeyValueCondition的条件，但是任务上下文却什么都没有。keys[%s]", rs.getId(),
								rs.getKeys());
						errorMessage.add(log);
						// throw new RuntimeException();
					}
				}
			}
			if (rs.getKeys() != null) {
				boolean qkvc = false;
				for (AbstractCoreCondition acc : completeConditions.getConditionList()) {
					if (acc instanceof QuestKeyValueCondition) {
						boolean keyName = false;
						for (QuestKeyResource qkr : rs.getKeys()) {
							if (qkr.getKeyname() != null && qkr.getKeyname().equals(acc.getCode())) {
								keyName = true;
							}
							if (qkr.getType().name().equals(acc.getCode())) {
								keyName = true;
							}

							if (qkr.getParms() != null && qkr.getParms().containsKey("LOWLEVEL")
									&& !(qkr.getParms().get("LOWLEVEL") instanceof Integer)) {
								String log = String.format("QuestResource id[%s] 包含任务上下文keys[%s] LOWLEVEL配置错误",
										rs.getId(), JsonUtils.object2String(rs.getKeys()),
										JsonUtils.object2String(rs.getCompleteConditions().getConditionList()));
								errorMessage.add(log);
							}

							if (qkr.getParms() != null && qkr.getParms().containsKey("UPLEVEL")
									&& !(qkr.getParms().get("UPLEVEL") instanceof Integer)) {
								String log = String.format("QuestResource id[%s] 包含任务上下文keys[%s] UPLEVEL配置错误",
										rs.getId(), JsonUtils.object2String(rs.getKeys()),
										JsonUtils.object2String(rs.getCompleteConditions().getConditionList()));
								errorMessage.add(log);
							}

							if (qkr.getParms() != null && qkr.getParms().containsKey("LEVELMIN")
									&& !(qkr.getParms().get("LEVELMIN") instanceof Integer)) {
								String log = String.format("QuestResource id[%s] 包含任务上下文keys[%s] LEVELMIN配置错误",
										rs.getId(), JsonUtils.object2String(rs.getKeys()),
										JsonUtils.object2String(rs.getCompleteConditions().getConditionList()));
								errorMessage.add(log);
							}

							if (qkr.getParms() != null && qkr.getParms().containsKey("LEVELMAX")
									&& !(qkr.getParms().get("LEVELMAX") instanceof Integer)) {
								String log = String.format("QuestResource id[%s] 包含任务上下文keys[%s] LEVELMAX配置错误",
										rs.getId(), JsonUtils.object2String(rs.getKeys()),
										JsonUtils.object2String(rs.getCompleteConditions().getConditionList()));
								errorMessage.add(log);
							}

						}
						if (!keyName) {
							String log = String
									.format("QuestResource id[%s] 包含任务上下文keys[%s] 但却找不到对应keyname的QuestKeyValueCondition的条件！QuestKeyValueCondition[%s]",
											rs.getId(), JsonUtils.object2String(rs.getKeys()),
											JsonUtils.object2String(rs.getCompleteConditions().getConditionList()));
							errorMessage.add(log);
							// throw new RuntimeException();
						}
						qkvc = true;
					}
				}
				if (!qkvc) {
					String log = String.format("QuestResource id[%s] 包含任务上下文keys[%s] 但却没有QuestKeyValueCondition的条件！",
							rs.getId(), JsonUtils.object2String(rs.getKeys()));
					errorMessage.add(log);
					// throw new RuntimeException();
				}

			}
			
			
			CoreConditions acceptConditions = CoreConditionManager.getCoreConditionResources(1, rs.getAccept());
			for (AbstractCoreCondition acc : acceptConditions.getConditionList()) {
				if (acc instanceof CompleteQuestCondition) {
					QuestResource parentQuest = questResource.get(acc.getCode(), false);
					if (parentQuest == null) {
						String log = String.format("QuestResource id[%s] 包含的接取条件的任务 [%s]不存在！", rs.getId(),
								acc.getCode());
						// throw new RuntimeException();
						errorMessage.add(log);
						continue;
					}
					if (parentQuest.getChildTasks() == null
							|| !Arrays.asList(parentQuest.getChildTasks()).contains(rs.getId())) {
						String log = String.format("QuestResource id[%s]的接取条件需要完成[%s] 但是任务[%s]的子任务配置为[%s]", rs.getId(),
								acc.getCode(), parentQuest.getId(),
								JsonUtils.object2String(parentQuest.getChildTasks()));
						errorMessage.add(log);
						// throw new RuntimeException(String.format(
						// "QuestResource id[%s]的接取条件需要完成[%s] 但是任务[%s]的子任务配置为[%s]",
						// rs.getId(), acc.getCode(),
						// parentQuest.getId(),
						// JsonUtils.object2String(parentQuest.getChildTasks())));
					}
				}
			}

			if (ArrayUtils.isEmpty(rs.getAccept()) && rs.getAccpetType() != AccpetType.TRIGGER) {
				throw new RuntimeException(String.format("QuestResource id[%s] Accept[%s]为null", rs.getId(),
						rs.getAccept()));
			}
			if (ArrayUtils.isEmpty(rs.getGiveUp())) {
				throw new RuntimeException(String.format("QuestResource id[%s] getGiveUp[%s]为null", rs.getId(),
						rs.getAccept()));
			}
			if (ArrayUtils.isEmpty(rs.getRemove())) {
				throw new RuntimeException(String.format("QuestResource id[%s] getRemove[%s]为null", rs.getId(),
						rs.getAccept()));
			}
			if (ArrayUtils.isEmpty(rs.getFail())) {
				throw new RuntimeException(String.format("QuestResource id[%s] getFail[%s]为null", rs.getId(),
						rs.getAccept()));
			}

			if (acceptConditions.findConditionType(LevelCondition.class) != null) {
				LevelCondition cond = acceptConditions.findConditionType(LevelCondition.class);
				if (cond.getLow() != rs.getNeedLevel()) {
					String log = String.format("QuestResource id[%s]的接取条件里的最低等级是[%d] 但是任务needLevel配的是[%d]", rs.getId(),
							cond.getLow(), rs.getNeedLevel());
					errorMessage.add(log);
				}
			}

			QuestKeyResource[] keys = rs.getKeys();
			CoreConditionResource[] completeResoures = rs.getComplete();
			Set<String> keyTypes = New.hashSet();
			if (keys == null) {
				continue;
			}
			for (QuestKeyResource key : keys) {
				if (key.getKeyname() != null) {
					keyTypes.add(key.getKeyname());
				} else {
					keyTypes.add(key.getType().name());
				}
			}
			for (CoreConditionResource completeResource : completeResoures) {
				if (completeResource.getType() == CoreConditionType.QUEST_KEYVALUE) {
					if (!keyTypes.contains(completeResource.getCode())) {
						throw new RuntimeException(String.format(
								"QuestResource id[%s] 上下文[%s]没有完成条件[%s]QUEST_KEYVALUE对应的code类型[%s]", rs.getId(),
								rs.getKeys(), rs.getComplete(), completeResource.getCode()));
					}
				}
			}
		}

		for (String s : errorMessage) {
			System.err.println(s);
		}
		if (!errorMessage.isEmpty()) {
			throw new RuntimeException();
		}
	}
}
