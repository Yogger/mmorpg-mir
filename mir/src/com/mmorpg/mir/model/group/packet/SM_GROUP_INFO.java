package com.mmorpg.mir.model.group.packet;

import java.util.ArrayList;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.group.model.GroupApply;
import com.mmorpg.mir.model.group.model.GroupApplyVO;
import com.mmorpg.mir.model.group.model.MemberVO;
import com.mmorpg.mir.model.group.model.PlayerGroup;
import com.mmorpg.mir.model.session.SessionManager;

public class SM_GROUP_INFO {
	private long leader;
	private ArrayList<MemberVO> members;
	private ArrayList<GroupApplyVO> groupApplyeVos;
	
	public static SM_GROUP_INFO valueOf(PlayerGroup group) {
		SM_GROUP_INFO sm = new SM_GROUP_INFO();
		sm.leader = group.getGroupLeader().getObjectId();
		if (!group.getMembers().isEmpty()) {
			sm.setMembers(new ArrayList<MemberVO>());
			for (Player player : group.getGroupMembers().values()) {
				sm.getMembers().add(
						MemberVO.valueOf(group, player, SessionManager.getInstance().isOnline(player.getObjectId())));
			}
		}
		if (!group.getApplies().isEmpty()) {
			for (GroupApply groupApply : group.getApplies().values()) {
				sm.setGroupApplyeVos(new ArrayList<GroupApplyVO>());
				sm.getGroupApplyeVos().add(groupApply.createVO());
			}
		}
		return sm;
	}

	public ArrayList<MemberVO> getMembers() {
		return members;
	}

	public void setMembers(ArrayList<MemberVO> members) {
		this.members = members;
	}

	public ArrayList<GroupApplyVO> getGroupApplyeVos() {
		return groupApplyeVos;
	}

	public void setGroupApplyeVos(ArrayList<GroupApplyVO> groupApplyeVos) {
		this.groupApplyeVos = groupApplyeVos;
	}

	public long getLeader() {
		return leader;
	}

	public void setLeader(long leader) {
		this.leader = leader;
	}

}
