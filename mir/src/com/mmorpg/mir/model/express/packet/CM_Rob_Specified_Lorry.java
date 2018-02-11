package com.mmorpg.mir.model.express.packet;

import com.mmorpg.mir.model.system.packet.CM_System_Sign;

public class CM_Rob_Specified_Lorry extends CM_System_Sign{

	private long targetPlayerId;

	public long getTargetPlayerId() {
    	return targetPlayerId;
    }

	public void setTargetPlayerId(long targetPlayerId) {
    	this.targetPlayerId = targetPlayerId;
    }
	
}
