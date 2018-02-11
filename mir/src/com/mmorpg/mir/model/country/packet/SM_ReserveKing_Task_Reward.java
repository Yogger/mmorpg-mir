package com.mmorpg.mir.model.country.packet;

public class SM_ReserveKing_Task_Reward {
	private int taskType;
	private int finishCount;

	public static SM_ReserveKing_Task_Reward valueOf(int taskType, int finishCount) {
		SM_ReserveKing_Task_Reward result = new SM_ReserveKing_Task_Reward();
		result.taskType = taskType;
		result.finishCount = finishCount;
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
