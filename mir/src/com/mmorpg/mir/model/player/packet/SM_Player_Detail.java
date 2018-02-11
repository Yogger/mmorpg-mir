package com.mmorpg.mir.model.player.packet;

import com.mmorpg.mir.model.player.entity.PlayerDetailVO;

public class SM_Player_Detail {

	private PlayerDetailVO vo;
	private String selfGangName;

	public static SM_Player_Detail valueOf(String gangName, PlayerDetailVO playerDetailVO) {
		SM_Player_Detail sm = new SM_Player_Detail();
		sm.vo = playerDetailVO;
		sm.selfGangName = gangName;
		return sm;
	}

	public PlayerDetailVO getVo() {
		return vo;
	}

	public void setVo(PlayerDetailVO vo) {
		this.vo = vo;
	}

	public String getSelfGangName() {
    	return selfGangName;
    }

	public void setSelfGangName(String selfGangName) {
    	this.selfGangName = selfGangName;
    }
}
