package com.mmorpg.mir.model.relive.packet;

public class CM_Respawn {
	private boolean buy;
	
	private boolean useItem;

	public boolean isBuy() {
		return buy;
	}

	public void setBuy(boolean buy) {
		this.buy = buy;
	}

	public boolean isUseItem() {
		return useItem;
	}

	public void setUseItem(boolean useItem) {
		this.useItem = useItem;
	}

}
