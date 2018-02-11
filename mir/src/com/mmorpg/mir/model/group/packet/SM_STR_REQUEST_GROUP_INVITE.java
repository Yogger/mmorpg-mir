package com.mmorpg.mir.model.group.packet;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.group.model.GroupInviterVO;

public class SM_STR_REQUEST_GROUP_INVITE {

	private GroupInviterVO vo;
	
	public static SM_STR_REQUEST_GROUP_INVITE valueOf(Player player) {
		SM_STR_REQUEST_GROUP_INVITE sm = new SM_STR_REQUEST_GROUP_INVITE();
		sm.vo = GroupInviterVO.valueOf(player);
		return sm;
	}

	public GroupInviterVO getVo() {
    	return vo;
    }

	public void setVo(GroupInviterVO vo) {
    	this.vo = vo;
    }

}
