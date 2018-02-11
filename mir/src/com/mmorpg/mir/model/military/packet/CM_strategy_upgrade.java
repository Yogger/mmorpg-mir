package com.mmorpg.mir.model.military.packet;

public class CM_strategy_upgrade {
	private int code;
	private int section;
	
	public static CM_strategy_upgrade valueOf(int code, int step) {
		CM_strategy_upgrade upgrade = new CM_strategy_upgrade();
		upgrade.code = code;
		upgrade.section = step;
		return upgrade;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getSection() {
		return section;
	}

	public void setSection(int section) {
		this.section = section;
	}
	
}
