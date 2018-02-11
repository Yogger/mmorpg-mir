package com.mmorpg.mir.model.group.packet;

import java.util.ArrayList;

import com.mmorpg.mir.model.group.model.GroupApply;
import com.mmorpg.mir.model.group.model.GroupApplyVO;
import com.mmorpg.mir.model.group.model.PlayerGroup;

public class SM_APPLY_LIST_PLAYER {

	public static SM_APPLY_LIST_PLAYER valueOf(PlayerGroup playerGroup) {
		SM_APPLY_LIST_PLAYER sm = new SM_APPLY_LIST_PLAYER();
		if (!playerGroup.getApplies().isEmpty()) {
			sm.setVos(new ArrayList<GroupApplyVO>());
			for (GroupApply app : playerGroup.getApplies().values()) {
				sm.getVos().add(app.createVO());
			}
		}
		return sm;
	}

	private ArrayList<GroupApplyVO> vos = new ArrayList<GroupApplyVO>();

	public ArrayList<GroupApplyVO> getVos() {
		return vos;
	}

	public void setVos(ArrayList<GroupApplyVO> vos) {
		this.vos = vos;
	}

}
