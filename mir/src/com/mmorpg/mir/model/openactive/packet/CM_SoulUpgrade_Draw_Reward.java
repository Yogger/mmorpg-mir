package com.mmorpg.mir.model.openactive.packet;

import com.mmorpg.mir.model.system.packet.CM_System_Sign;

public class CM_SoulUpgrade_Draw_Reward extends CM_System_Sign{
	private String code;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
