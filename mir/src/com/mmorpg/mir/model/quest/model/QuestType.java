package com.mmorpg.mir.model.quest.model;

public enum QuestType {
	/** 主线 */
	TRUNK(1),
	/** 日常 */
	DAY(2),
	/** 支线 */
	BRANCH(3),
	/** 副本 */
	COPY(4),
	/** 运镖 */
	EXPRESS(6),
	/** 荣誉 */
	HONOR(7),
	/** 转职任务 */
	PROMOTE(8),
	/** 游历任务 */
	RANDOM(9),
	/** 卧薪尝胆任务 */
	STRENUOUS(10);

	private int value;

	private QuestType(int state) {
		this.value = state;
	}

	public int getValue() {
		return this.value;
	}
}
