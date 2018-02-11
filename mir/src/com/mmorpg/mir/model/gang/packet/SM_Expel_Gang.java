package com.mmorpg.mir.model.gang.packet;

public class SM_Expel_Gang {
	private int code;
	private long targetId;

	public static SM_Expel_Gang valueOf(long targetId) {
		SM_Expel_Gang sm = new SM_Expel_Gang();
		sm.targetId = targetId;
		return sm;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public long getTargetId() {
		return targetId;
	}

	public void setTargetId(long targetId) {
		this.targetId = targetId;
	}

}
