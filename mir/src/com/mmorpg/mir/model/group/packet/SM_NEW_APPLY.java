package com.mmorpg.mir.model.group.packet;

import com.mmorpg.mir.model.group.model.GroupApplyVO;

public class SM_NEW_APPLY {
	private GroupApplyVO vo;

	public static SM_NEW_APPLY valueOf(GroupApplyVO vo) {
		SM_NEW_APPLY sm = new SM_NEW_APPLY();
		sm.vo = vo;
		return sm;
	}

	public GroupApplyVO getVo() {
		return vo;
	}

	public void setVo(GroupApplyVO vo) {
		this.vo = vo;
	}

}
