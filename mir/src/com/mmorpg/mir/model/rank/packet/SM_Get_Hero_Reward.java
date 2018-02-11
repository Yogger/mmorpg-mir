package com.mmorpg.mir.model.rank.packet;

public class SM_Get_Hero_Reward {
	private int code;

	public static SM_Get_Hero_Reward valueOf(int c) {
		SM_Get_Hero_Reward s = new SM_Get_Hero_Reward();
		s.code = c;
		return s;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}
