package com.mmorpg.mir.model.invest.packet;

import com.mmorpg.mir.model.system.packet.CM_System_Sign;

public class CM_Draw_Reward extends CM_System_Sign {
	private int investType;

	private String resouceId;

	public int getInvestType() {
		return investType;
	}

	public void setInvestType(int investType) {
		this.investType = investType;
	}

	public String getResouceId() {
		return resouceId;
	}

	public void setResouceId(String resouceId) {
		this.resouceId = resouceId;
	}

}
