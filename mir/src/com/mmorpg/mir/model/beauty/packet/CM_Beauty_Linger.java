package com.mmorpg.mir.model.beauty.packet;

public class CM_Beauty_Linger {

	private String girlId;
	private boolean autoBuy;

	public boolean isAutoBuy() {
		return autoBuy;
	}

	public void setAutoBuy(boolean autoBuy) {
		this.autoBuy = autoBuy;
	}

	public String getGirlId() {
		return girlId;
	}

	public void setGirlId(String girlId) {
		this.girlId = girlId;
	}

}
