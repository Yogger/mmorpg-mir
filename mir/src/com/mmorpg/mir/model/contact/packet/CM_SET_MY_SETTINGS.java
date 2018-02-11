package com.mmorpg.mir.model.contact.packet;

import com.mmorpg.mir.model.system.packet.CM_System_Sign;


public class CM_SET_MY_SETTINGS extends CM_System_Sign{

	private boolean displayOffline;
	private boolean displayHead;
	private boolean publicMapInfo;
	
	public boolean isPublicMapInfo() {
		return publicMapInfo;
	}

	public void setPublicMapInfo(boolean publicMapInfo) {
		this.publicMapInfo = publicMapInfo;
	}

	public boolean isDisplayOffline() {
		return displayOffline;
	}

	public void setDisplayOffline(boolean displayOffline) {
		this.displayOffline = displayOffline;
	}

	public boolean isDisplayHead() {
		return displayHead;
	}

	public void setDisplayHead(boolean displayHead) {
		this.displayHead = displayHead;
	}
	
}
