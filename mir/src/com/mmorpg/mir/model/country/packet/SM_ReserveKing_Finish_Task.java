package com.mmorpg.mir.model.country.packet;

public class SM_ReserveKing_Finish_Task {
	private int taskType;
	private int count;

	public static SM_ReserveKing_Finish_Task valueOf(int taskType, int count) {
		SM_ReserveKing_Finish_Task result = new SM_ReserveKing_Finish_Task();
		result.taskType = taskType;
		result.count = count;
		return result;
	}

	public int getTaskType() {
		return taskType;
	}

	public void setTaskType(int taskType) {
		this.taskType = taskType;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
