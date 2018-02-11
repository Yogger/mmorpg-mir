package com.mmorpg.mir.model.player.packet;

public class SM_RP_Update {
	private int rp;
	private int type;

	public static SM_RP_Update valueOf(int rp, int type) {
		SM_RP_Update update = new SM_RP_Update();
		update.rp = rp;
		update.type = type;
		return update;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getRp() {
		return rp;
	}

	public void setRp(int rp) {
		this.rp = rp;
	}
}
