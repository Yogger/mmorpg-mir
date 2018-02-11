package com.mmorpg.mir.model.gang.packet;

import com.mmorpg.mir.model.gang.model.Gang;
import com.mmorpg.mir.model.gang.model.GangInfo;

public class SM_Self_Gang_Change {

	private long playerId;
	private GangInfo gangInfo;

	public static SM_Self_Gang_Change valueOf(long pid, Gang gang) {
		SM_Self_Gang_Change sm = new SM_Self_Gang_Change();
		sm.gangInfo = GangInfo.valueOf(gang);
		sm.playerId = pid;
		return sm;
	}
	
	public GangInfo getGangInfo() {
		return gangInfo;
	}

	public void setGangInfo(GangInfo gangInfo) {
		this.gangInfo = gangInfo;
	}

	public final long getPlayerId() {
		return playerId;
	}

	public final void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	
}
