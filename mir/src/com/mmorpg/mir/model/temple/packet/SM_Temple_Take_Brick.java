package com.mmorpg.mir.model.temple.packet;

import com.mmorpg.mir.model.temple.model.Brick;

public class SM_Temple_Take_Brick {
	private String id;

	public static SM_Temple_Take_Brick valueOf(Brick brick) {
		SM_Temple_Take_Brick sm = new SM_Temple_Take_Brick();
		sm.id = brick.getId();
		return sm;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
