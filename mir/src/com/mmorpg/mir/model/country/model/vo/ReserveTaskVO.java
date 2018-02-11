package com.mmorpg.mir.model.country.model.vo;

import com.mmorpg.mir.model.country.model.ReserveTask;

public class ReserveTaskVO {
	/** 任务类型 */
	private int taskType;
	/** 完成次数 */
	private int finishCount;

	public static ReserveTaskVO valueOf(ReserveTask task) {
		ReserveTaskVO result = new ReserveTaskVO();
		result.taskType = task.getTaskType();
		result.finishCount = task.getFinishCount();
		return result;
	}

	public int getTaskType() {
		return taskType;
	}

	public void setTaskType(int taskType) {
		this.taskType = taskType;
	}

	public int getFinishCount() {
		return finishCount;
	}

	public void setFinishCount(int finishCount) {
		this.finishCount = finishCount;
	}

}
