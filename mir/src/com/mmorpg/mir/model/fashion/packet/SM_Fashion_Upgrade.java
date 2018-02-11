package com.mmorpg.mir.model.fashion.packet;

import com.mmorpg.mir.model.fashion.model.FashionPool;

public class SM_Fashion_Upgrade {

	private int level;

	private int exp;

	public static SM_Fashion_Upgrade valueOf(FashionPool pool) {
		SM_Fashion_Upgrade result = new SM_Fashion_Upgrade();
		result.level = pool.getLevel();
		result.exp = pool.getExp();
		return result;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

}
