package com.mmorpg.mir.model.vip.packet;

import com.mmorpg.mir.model.system.packet.CM_System_Sign;

public class CM_Vip_LevelReward extends CM_System_Sign {
	private int level;

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}
