package com.mmorpg.mir.model.express.packet;

public class SM_Lorry_AttackTime {

	private long lastAttackTime;

	public static SM_Lorry_AttackTime valueOf(long time) {
		SM_Lorry_AttackTime sm = new SM_Lorry_AttackTime();
		sm.lastAttackTime = time;
		return sm;
	}
	
	public long getLastAttackTime() {
		return lastAttackTime;
	}

	public void setLastAttackTime(long lastAttackTime) {
		this.lastAttackTime = lastAttackTime;
	}
	
}
