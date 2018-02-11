package com.mmorpg.mir.model.openactive.packet;

import com.mmorpg.mir.model.openactive.model.ActivityEnum;
import com.mmorpg.mir.model.openactive.model.ActivityInfo;

public class SM_Celebrate_Activity_Change {
	private int type;
	private long beginTime;
	private long endTime;

	public static SM_Celebrate_Activity_Change valueOf(ActivityEnum type, ActivityInfo activityInfo) {
		SM_Celebrate_Activity_Change result = new SM_Celebrate_Activity_Change();
		result.type = type.getValue();
		result.beginTime = activityInfo.getBeginTime();
		result.endTime = activityInfo.getEndTime();
		return result;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(long beginTime) {
		this.beginTime = beginTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

}
