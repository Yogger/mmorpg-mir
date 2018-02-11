package com.mmorpg.mir.model.contact.packet;

public class SM_Update_Enemy {

	private long enemyId;
	private int enmity;
	private int slaughtCount;

	public static SM_Update_Enemy valueOf(long enemyId, Integer enmity, Integer slaughtCount) {
		SM_Update_Enemy sm = new SM_Update_Enemy();
		sm.enemyId = enemyId;
		sm.enmity = (enmity == null? 0 : enmity);
		sm.slaughtCount = (slaughtCount == null? 0: slaughtCount);
		return sm;
	}
	
	public long getEnemyId() {
		return enemyId;
	}

	public void setEnemyId(long enemyId) {
		this.enemyId = enemyId;
	}

	public int getEnmity() {
		return enmity;
	}

	public void setEnmity(int enmity) {
		this.enmity = enmity;
	}

	public int getSlaughtCount() {
		return slaughtCount;
	}

	public void setSlaughtCount(int slaughtCount) {
		this.slaughtCount = slaughtCount;
	}

}
