package com.mmorpg.mir.model.warbook.packet;

public class SM_Warbook_Broadcast {
	private long objId;

	private int grade;

	public static SM_Warbook_Broadcast valueOf(long objId, int grade, boolean hided) {
		SM_Warbook_Broadcast result = new SM_Warbook_Broadcast();
		result.objId = objId;
		result.grade = hided ? -1 : grade;
		return result;
	}

	public long getObjId() {
		return objId;
	}

	public int getGrade() {
		return grade;
	}

	public void setObjId(long objId) {
		this.objId = objId;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

}
