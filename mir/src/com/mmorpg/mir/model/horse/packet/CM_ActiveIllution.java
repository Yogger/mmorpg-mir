package com.mmorpg.mir.model.horse.packet;

import com.mmorpg.mir.model.system.packet.CM_System_Sign;

public class CM_ActiveIllution extends CM_System_Sign{
	/** 是否永久激活 */
	private boolean foreverActive;

	public boolean isForeverActive() {
		return foreverActive;
	}

	public void setForeverActive(boolean foreverActive) {
		this.foreverActive = foreverActive;
	}

}
