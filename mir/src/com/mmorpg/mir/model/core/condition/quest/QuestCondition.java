package com.mmorpg.mir.model.core.condition.quest;

/**
 * 任务事件响应条件接口
 * 
 * @author Kuang Hao
 * @since v1.0 2014-8-21
 * 
 */
public interface QuestCondition {
	Class<?>[] getEvent();
}
