package com.mmorpg.mir.model.express.packet;

import com.mmorpg.mir.model.system.packet.CM_System_Sign;

public class CM_Express_Start extends CM_System_Sign {
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
