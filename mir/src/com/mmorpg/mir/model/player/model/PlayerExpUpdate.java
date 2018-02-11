package com.mmorpg.mir.model.player.model;

public class PlayerExpUpdate {
	private int level;
	private long addExp;
	private long exp;

	public static PlayerExpUpdate valueOf(int level, long addExp, long exp) {
		PlayerExpUpdate update = new PlayerExpUpdate();
		update.setAddExp(addExp);
		update.setExp(exp);
		update.setLevel(level);
		return update;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setExp(long exp) {
		this.exp = exp;
	}

	public long getExp() {
		return exp;
	}

	public long getAddExp() {
		return addExp;
	}

	public void setAddExp(long addExp) {
		this.addExp = addExp;
	}

}
