package com.mmorpg.mir.model.commonactivity.packet;

public class SM_WeekCri_Count_Change {
	private int openCount;

	public static SM_WeekCri_Count_Change valueOf(int openCount) {
		SM_WeekCri_Count_Change result = new SM_WeekCri_Count_Change();
		result.openCount = openCount;
		return result;
	}

	public int getOpenCount() {
		return openCount;
	}

	public void setOpenCount(int openCount) {
		this.openCount = openCount;
	}

}
