package com.mmorpg.mir.model.copy.packet;

public class SM_Copy_Encourage {
	private int count;
	private int hpCount;
	private int damageCount;
	private boolean success;

	public static SM_Copy_Encourage valueOf(int count, int hpCount, int damageCount, boolean success) {
		SM_Copy_Encourage sm = new SM_Copy_Encourage();
		sm.count = count;
		sm.hpCount = hpCount;
		sm.damageCount = damageCount;
		sm.success = success;
		return sm;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getHpCount() {
		return hpCount;
	}

	public void setHpCount(int hpCount) {
		this.hpCount = hpCount;
	}

	public int getDamageCount() {
		return damageCount;
	}

	public void setDamageCount(int damageCount) {
		this.damageCount = damageCount;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

}
