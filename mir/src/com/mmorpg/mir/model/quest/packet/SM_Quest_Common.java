package com.mmorpg.mir.model.quest.packet;

public class SM_Quest_Common {
	private int code;

	public static SM_Quest_Common valueOf(int code) {
		SM_Quest_Common cr = new SM_Quest_Common();
		cr.code = code;
		return cr;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}
