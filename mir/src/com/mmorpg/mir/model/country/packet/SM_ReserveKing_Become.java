package com.mmorpg.mir.model.country.packet;

import java.util.HashMap;
import java.util.Map;

import com.mmorpg.mir.model.country.model.ReserveKing;
import com.mmorpg.mir.model.country.model.ReserveTask;

public class SM_ReserveKing_Become {
	private HashMap<Integer, Integer> taskInfos;

	public static SM_ReserveKing_Become valueOf(ReserveKing reserveKing) {
		SM_ReserveKing_Become result = new SM_ReserveKing_Become();
		result.taskInfos = new HashMap<Integer, Integer>();
		for (Map.Entry<Integer, ReserveTask> entry : reserveKing.getTasks().entrySet()) {
			result.taskInfos.put(entry.getKey(), entry.getValue().getFinishCount());
		}
		return result;
	}

	public HashMap<Integer, Integer> getTaskInfos() {
		return taskInfos;
	}

	public void setTaskInfos(HashMap<Integer, Integer> taskInfos) {
		this.taskInfos = taskInfos;
	}

}
