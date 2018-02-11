package com.mmorpg.mir.admin.packet;

import com.mmorpg.mir.model.gang.model.GangVO;

public class SGM_Gang_SetInfo {
	private GangVO gangVO;

	public GangVO getGangVO() {
		return gangVO;
	}

	public void setGangVO(GangVO gangVO) {
		this.gangVO = gangVO;
	}

}
