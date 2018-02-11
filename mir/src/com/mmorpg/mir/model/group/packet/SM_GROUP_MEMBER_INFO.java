package com.mmorpg.mir.model.group.packet;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.group.model.MemberVO;
import com.mmorpg.mir.model.group.model.PlayerGroup;

public class SM_GROUP_MEMBER_INFO {
	private MemberVO memberVO;

	public static SM_GROUP_MEMBER_INFO valueOf(PlayerGroup playerGroup, Player player, boolean online) {
		SM_GROUP_MEMBER_INFO sm = new SM_GROUP_MEMBER_INFO();
		sm.setMemberVO(MemberVO.valueOf(playerGroup, player, online));
		return sm;
	}

	public MemberVO getMemberVO() {
		return memberVO;
	}

	public void setMemberVO(MemberVO memberVO) {
		this.memberVO = memberVO;
	}

}
