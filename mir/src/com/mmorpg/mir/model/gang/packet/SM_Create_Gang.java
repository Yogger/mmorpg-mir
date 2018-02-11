package com.mmorpg.mir.model.gang.packet;

import com.mmorpg.mir.model.gang.model.GangVO;

public class SM_Create_Gang {
	private int code;
	private GangVO gangVO;

	public GangVO getGangVO() {
		return gangVO;
	}

	public void setGangVO(GangVO gangVO) {
		this.gangVO = gangVO;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}
