package com.mmorpg.mir.model.express.packet;

public class CM_Express_Reset {
	private boolean gold;
	private boolean autoBuy;

	public boolean isGold() {
		return gold;
	}

	public void setGold(boolean gold) {
		this.gold = gold;
	}

	public boolean isAutoBuy() {
		return autoBuy;
	}

	public void setAutoBuy(boolean autoBuy) {
		this.autoBuy = autoBuy;
	}

}
