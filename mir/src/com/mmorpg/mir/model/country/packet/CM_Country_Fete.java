package com.mmorpg.mir.model.country.packet;

import com.mmorpg.mir.model.system.packet.CM_System_Sign;

public class CM_Country_Fete extends CM_System_Sign{

	private int type;

	public int getType() {
    	return type;
    }

	public void setType(int type) {
    	this.type = type;
    }
	
}
