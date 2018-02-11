package com.mmorpg.mir.model.copy.packet;

public class SM_Copy_First_Finish {
	private String copyId;
	private long time;

	public static SM_Copy_First_Finish valueOf(String id, long t) {
		SM_Copy_First_Finish sm = new SM_Copy_First_Finish();
		sm.copyId = id;
		sm.time = t;
		return sm;
	}
	
	public String getCopyId() {
		return copyId;
	}

	public void setCopyId(String copyId) {
		this.copyId = copyId;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

}
