package com.mmorpg.mir.model.gang.packet;

import com.mmorpg.mir.model.gang.model.PlayerInvite;
import com.mmorpg.mir.model.gang.model.PlayerInviteVO;

public class SM_GangInvite {
	private String code;
	private PlayerInviteVO vo;

	public static SM_GangInvite valueOf(PlayerInvite playerInvite) {
		SM_GangInvite in = new SM_GangInvite();
		in.setVo(playerInvite.createVO());
		return in;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public PlayerInviteVO getVo() {
		return vo;
	}

	public void setVo(PlayerInviteVO vo) {
		this.vo = vo;
	}

}
