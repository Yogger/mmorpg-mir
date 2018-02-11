package com.mmorpg.mir.model.operator.packet;

public class SM_OpVip_Reward {
	private int code;
	private int level;

	public static SM_OpVip_Reward valueOf(int level, int code) {
		SM_OpVip_Reward sm = new SM_OpVip_Reward();
		sm.code = code;
		sm.level = level;
		return sm;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}
