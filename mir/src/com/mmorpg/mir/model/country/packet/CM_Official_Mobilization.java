package com.mmorpg.mir.model.country.packet;

import com.mmorpg.mir.model.system.packet.CM_System_Sign;

public class CM_Official_Mobilization extends CM_System_Sign{

	private String phrase;

	public String getPhrase() {
    	return phrase;
    }

	public void setPhrase(String phrase) {
    	this.phrase = phrase;
    }

}
