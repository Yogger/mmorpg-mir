package com.mmorpg.mir.model.item.packet;

import com.mmorpg.mir.model.gameobjects.Player;

public class SM_CallBack_Pet {
	private int code;

	public static SM_CallBack_Pet valueOf(Player player) {
		SM_CallBack_Pet sm = new SM_CallBack_Pet();
		return sm;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}
