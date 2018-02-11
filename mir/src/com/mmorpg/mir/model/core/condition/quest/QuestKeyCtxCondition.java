package com.mmorpg.mir.model.core.condition.quest;

import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.quest.model.Quest;

/**
 * 任务的上下文参数验证
 * 
 * @author Kuang Hao
 * @since v1.0 2014-8-21
 * 
 */
public class QuestKeyCtxCondition extends AbstractQuestCoreCondition {

	private String taskCtxKey;
	private String taskCtxValue;

	@Override
	public boolean verify(Object object) {
		Quest quest = null;
		if (object instanceof Quest) {
			quest = (Quest) object;
		}
		if (quest == null) {
			this.errorObject(object);
		}

		if (taskCtxValue.equals(quest.findKey(code).getCtx().get(taskCtxKey))) {
			return true;
		}
		return false;
	}

	@Override
	protected void init(CoreConditionResource resource) {
		super.init(resource);
		taskCtxKey = resource.getTaskCtxKey();
		taskCtxValue = resource.getTaskCtxValue();
	}

	public String getTaskCtxKey() {
		return taskCtxKey;
	}

	public void setTaskCtxKey(String taskCtxKey) {
		this.taskCtxKey = taskCtxKey;
	}

	public String getTaskCtxValue() {
		return taskCtxValue;
	}

	public void setTaskCtxValue(String taskCtxValue) {
		this.taskCtxValue = taskCtxValue;
	}

}
