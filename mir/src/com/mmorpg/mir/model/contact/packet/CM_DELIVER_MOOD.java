package com.mmorpg.mir.model.contact.packet;

import com.mmorpg.mir.model.system.packet.CM_System_Sign;

public class CM_DELIVER_MOOD extends CM_System_Sign{

	private String phrase;

	public String getPhrase() {
		return phrase;
	}

	public void setPhrase(String phrase) {
		this.phrase = phrase;
	}

}
