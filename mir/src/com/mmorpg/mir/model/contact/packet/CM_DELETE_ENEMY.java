package com.mmorpg.mir.model.contact.packet;

import com.mmorpg.mir.model.system.packet.CM_System_Sign;

public class CM_DELETE_ENEMY extends CM_System_Sign{

	private long enemyId;

	public long getEnemyId() {
		return enemyId;
	}

	public void setEnemyId(long enemyId) {
		this.enemyId = enemyId;
	}
}
