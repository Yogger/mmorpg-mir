package com.mmorpg.mir.model.quest.resource;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Transient;

import org.apache.commons.lang.ArrayUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.condition.AbstractCoreCondition;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditionType;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.core.condition.quest.QuestCondition;
import com.mmorpg.mir.model.quest.model.QuestType;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Index;
import com.windforce.common.resource.anno.Resource;
import com.windforce.common.utility.JsonUtils;
import com.windforce.common.utility.SelectRandom;

@Resource
public class QuestResource {
	@Id
	private String id;
	/** 短id */
	@Index(name = "templateId", unique = true)
	private short templateId;
	/** 任务完成接取后的触发器 */
	private String[] acceptTriggers;
	/** 接取条件 */
	private CoreConditionResource[] accept;
	/** 是接取方式 */
	private AccpetType accpetType;
	/** 任务的条件上下文 */
	private QuestKeyResource[] keys;
	/** 客服端请求完成 */
	private boolean clientComplete;
	/** 任务需要完成的条件 */
	private CoreConditionResource[] complete;
	/** 任务完成以后的触发器 */
	private String[] completeTriggers;
	/** 放弃任务的条件 */
	private CoreConditionResource[] giveUp;
	/** 放弃任务后的触发器 */
	private String[] giveUpTriggers;
	/** 删除任务后的触发器 */
	@Deprecated
	private String[] removeTriggers;
	/** 任务失败后的触发器 */
	private String[] failTriggers;
	/** 任务删除的条件 */
	private CoreConditionResource[] remove;
	/** 任务失败的条件 */
	private CoreConditionResource[] fail;
	/** 任务包含奖励id */
	private String rewardChooserGroupId;
	/** 是否自动发奖 */
	private boolean autoReward;
	/** 子类任务 */
	private String[] childTasks;
	/** 类型 */
	private QuestType type;
	/** 一键完成消耗 */
	private String[] atOnceCompletAct;
	/** 提升到5星消耗 */
	private String[] fullStarAct;
	/** 星级权重 */
	private int[] starWeight;
	/** 接取的最低等级要求 */
	private int needLevel;
	/** 转国处理已经完成的任务id */
	private String[] migrateCompleteQuestIds;
	/** 转国处理当前的任务id */
	private String[] migrateCurrentQuestIds;
	/** 副本id */
	private String copyId;

	public static void main(String[] args) {
		CoreConditionResource[] temp = new CoreConditionResource[0];
		CoreConditionResource r1 = CoreConditionResource.createCondition(CoreConditionType.ITEM, "item1", 2);
		CoreConditionResource r2 = CoreConditionResource.createCondition(CoreConditionType.FOREVERTRUE, null, 0);
		temp = (CoreConditionResource[]) ArrayUtils.add(temp, r1);
		temp = (CoreConditionResource[]) ArrayUtils.add(temp, r2);
		System.out.println(JsonUtils.object2String(temp));
	}

	@Transient
	private CoreConditions completeConditions;
	@Transient
	private CoreConditions acceptConditions;
	@Transient
	private CoreConditions giveUpConditions;
	@Transient
	private CoreConditions removeConditions;
	@Transient
	private CoreConditions failConditions;
	@Transient
	private List<Class<?>> acceptEventClass;
	@Transient
	private List<Class<?>> completeEventClass;
	@Transient
	private List<Class<?>> removeEventClass;
	@Transient
	private List<Class<?>> failEventClass;
	@Transient
	private SelectRandom<Byte> selector;

	@JsonIgnore
	public byte selectStar() {
		if (starWeight == null) {
			return 0;
		}
		if (selector == null) {
			SelectRandom<Byte> selectorTemp = new SelectRandom<Byte>();
			for (int i = 0; i < starWeight.length; i++) {
				selectorTemp.addElement((byte) i, starWeight[i]);
			}
			selector = selectorTemp;
		}
		return selector.run();
	}

	/**
	 * 获取所有接受条件响应的事件
	 * 
	 * @return
	 */
	@JsonIgnore
	public List<Class<?>> getAllAcceptEvent() {
		synchronized (this) {
			if (acceptEventClass == null) {
				List<Class<?>> eventClass = new ArrayList<Class<?>>();
				for (AbstractCoreCondition condition : getAcceptConditions().getConditionList()) {
					if (condition instanceof QuestCondition) {
						QuestCondition qc = (QuestCondition) condition;
						for (Class<?> clazz : qc.getEvent()) {
							if (!eventClass.contains(clazz)) {
								eventClass.add(clazz);
							}
						}
					}
				}
				acceptEventClass = eventClass;
			}
			return acceptEventClass;
		}
	}

	/**
	 * 获取完成接受条件响应的事件
	 * 
	 * @return
	 */
	@JsonIgnore
	public List<Class<?>> getAllFailEvent() {
		if (failEventClass == null) {
			List<Class<?>> eventClass = new ArrayList<Class<?>>();
			for (AbstractCoreCondition condition : getFailConditions().getConditionList()) {
				if (condition instanceof QuestCondition) {
					QuestCondition qc = (QuestCondition) condition;
					for (Class<?> clazz : qc.getEvent()) {
						if (!eventClass.contains(clazz)) {
							eventClass.add(clazz);
						}
					}
				}
			}
			failEventClass = eventClass;
		}

		return failEventClass;
	}

	/**
	 * 获取完成接受条件响应的事件
	 * 
	 * @return
	 */
	@JsonIgnore
	public List<Class<?>> getAllCompleteEvent() {
		if (completeEventClass == null) {
			List<Class<?>> eventClass = new ArrayList<Class<?>>();
			for (AbstractCoreCondition condition : getCompleteConditions().getConditionList()) {
				if (condition instanceof QuestCondition) {
					QuestCondition qc = (QuestCondition) condition;
					for (Class<?> clazz : qc.getEvent()) {
						if (!eventClass.contains(clazz)) {
							eventClass.add(clazz);
						}
					}
				}
			}
			completeEventClass = eventClass;
		}

		return completeEventClass;
	}

	@JsonIgnore
	public List<Class<?>> getAllRemoveEvent() {
		if (removeEventClass == null) {
			List<Class<?>> eventClass = new ArrayList<Class<?>>();
			for (AbstractCoreCondition condition : getRemoveConditions().getConditionList()) {
				if (condition instanceof QuestCondition) {
					QuestCondition qc = (QuestCondition) condition;
					for (Class<?> clazz : qc.getEvent()) {
						if (!eventClass.contains(clazz)) {
							eventClass.add(clazz);
						}
					}
				}
			}
			removeEventClass = eventClass;
		}

		return removeEventClass;
	}

	@JsonIgnore
	public CoreConditions getCompleteConditions() {
		if (completeConditions == null) {
			completeConditions = CoreConditionManager.getInstance().getCoreConditions(complete);
		}
		return completeConditions;
	}

	@JsonIgnore
	public CoreConditions getAcceptConditions() {
		if (acceptConditions == null) {
			acceptConditions = CoreConditionManager.getInstance().getCoreConditions(accept);
			for (AbstractCoreCondition acc : acceptConditions.getConditionList()) {
				acc.setThrowException(false);
			}
		}
		return acceptConditions;
	}

	@JsonIgnore
	public CoreConditions getGiveUpConditions() {
		if (giveUpConditions == null) {
			giveUpConditions = CoreConditionManager.getInstance().getCoreConditions(giveUp);
			for (AbstractCoreCondition acc : giveUpConditions.getConditionList()) {
				acc.setThrowException(false);
			}
		}
		return giveUpConditions;
	}

	@JsonIgnore
	public CoreConditions getFailConditions() {
		if (failConditions == null) {
			failConditions = CoreConditionManager.getInstance().getCoreConditions(fail);
			for (AbstractCoreCondition acc : failConditions.getConditionList()) {
				acc.setThrowException(false);
			}
		}
		return failConditions;
	}

	@JsonIgnore
	public CoreConditions getRemoveConditions() {
		if (removeConditions == null) {
			removeConditions = CoreConditionManager.getInstance().getCoreConditions(remove);
			for (AbstractCoreCondition acc : removeConditions.getConditionList()) {
				acc.setThrowException(false);
			}
		}
		return removeConditions;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public QuestKeyResource[] getKeys() {
		return keys;
	}

	public void setKeys(QuestKeyResource[] keys) {
		this.keys = keys;
	}

	public String[] getChildTasks() {
		return childTasks;
	}

	public void setChildTasks(String[] childTasks) {
		this.childTasks = childTasks;
	}

	public CoreConditionResource[] getAccept() {
		return accept;
	}

	public void setAccept(CoreConditionResource[] accept) {
		this.accept = accept;
	}

	public CoreConditionResource[] getComplete() {
		return complete;
	}

	public void setComplete(CoreConditionResource[] complete) {
		this.complete = complete;
	}

	public CoreConditionResource[] getGiveUp() {
		return giveUp;
	}

	public void setGiveUp(CoreConditionResource[] giveUp) {
		this.giveUp = giveUp;
	}

	public CoreConditionResource[] getRemove() {
		return remove;
	}

	public void setRemove(CoreConditionResource[] remove) {
		this.remove = remove;
	}

	public CoreConditionResource[] getFail() {
		return fail;
	}

	public void setFail(CoreConditionResource[] fail) {
		this.fail = fail;
	}

	public String getRewardChooserGroupId() {
		return rewardChooserGroupId;
	}

	public void setRewardChooserGroupId(String rewardChooserGroupId) {
		this.rewardChooserGroupId = rewardChooserGroupId;
	}

	public boolean isAutoReward() {
		return autoReward;
	}

	public void setAutoReward(boolean autoReward) {
		this.autoReward = autoReward;
	}

	public boolean isClientComplete() {
		return clientComplete;
	}

	public void setClientComplete(boolean clientComplete) {
		this.clientComplete = clientComplete;
	}

	public AccpetType getAccpetType() {
		return accpetType;
	}

	@JsonIgnore
	public boolean isTriggerType() {
		return AccpetType.TRIGGER == getAccpetType() ? true : false;
	}

	@JsonIgnore
	public boolean isAutoType() {
		return AccpetType.AUTO == getAccpetType() ? true : false;
	}

	@JsonIgnore
	public boolean isClientType() {
		return AccpetType.CLIENT == getAccpetType() ? true : false;
	}

	public void setAccpetType(AccpetType accpetType) {
		this.accpetType = accpetType;
	}

	public QuestType getType() {
		return type;
	}

	public void setType(QuestType type) {
		this.type = type;
	}

	public String[] getAtOnceCompletAct() {
		return atOnceCompletAct;
	}

	public void setAtOnceCompletAct(String[] atOnceCompletAct) {
		this.atOnceCompletAct = atOnceCompletAct;
	}

	public int[] getStarWeight() {
		return starWeight;
	}

	public void setStarWeight(int[] starWeight) {
		this.starWeight = starWeight;
	}

	public String[] getFullStarAct() {
		return fullStarAct;
	}

	public void setFullStarAct(String[] fullStarAct) {
		this.fullStarAct = fullStarAct;
	}

	public String[] getCompleteTriggers() {
		return completeTriggers;
	}

	public void setCompleteTriggers(String[] completeTriggers) {
		this.completeTriggers = completeTriggers;
	}

	public String[] getAcceptTriggers() {
		return acceptTriggers;
	}

	public void setAcceptTriggers(String[] acceptTriggers) {
		this.acceptTriggers = acceptTriggers;
	}

	public String[] getGiveUpTriggers() {
		return giveUpTriggers;
	}

	public void setGiveUpTriggers(String[] giveUpTriggers) {
		this.giveUpTriggers = giveUpTriggers;
	}

	@Deprecated
	public String[] getRemoveTriggers() {
		return removeTriggers;
	}

	public void setRemoveTriggers(String[] removeTriggers) {
		this.removeTriggers = removeTriggers;
	}

	public String[] getFailTriggers() {
		return failTriggers;
	}

	public void setFailTriggers(String[] failTriggers) {
		this.failTriggers = failTriggers;
	}

	public short getTemplateId() {
		return templateId;
	}

	public void setTemplateId(short templateId) {
		this.templateId = templateId;
	}

	public int getNeedLevel() {
		return needLevel;
	}

	public void setNeedLevel(int needLevel) {
		this.needLevel = needLevel;
	}

	public String[] getMigrateCompleteQuestIds() {
		return migrateCompleteQuestIds;
	}

	public void setMigrateCompleteQuestIds(String[] migrateCompleteQuestIds) {
		this.migrateCompleteQuestIds = migrateCompleteQuestIds;
	}

	public String[] getMigrateCurrentQuestIds() {
		return migrateCurrentQuestIds;
	}

	public void setMigrateCurrentQuestIds(String[] migrateCurrentQuestIds) {
		this.migrateCurrentQuestIds = migrateCurrentQuestIds;
	}

	public String getCopyId() {
		return copyId;
	}

	public void setCopyId(String copyId) {
		this.copyId = copyId;
	}

}
