package com.mmorpg.mir.model.openactive.packet;

import java.util.Map;

import com.mmorpg.mir.model.openactive.model.vo.OldCompeteVO;

public class SM_Query_OldCompeteStatus {
	
	private Map<Integer, OldCompeteVO> status;
	
	public static SM_Query_OldCompeteStatus valueOf(Map<Integer, OldCompeteVO> ret) {
		SM_Query_OldCompeteStatus sm = new SM_Query_OldCompeteStatus();
		sm.status = ret;
		return sm;
	}

	public Map<Integer, OldCompeteVO> getStatus() {
		return status;
	}

	public void setStatus(Map<Integer, OldCompeteVO> status) {
		this.status = status;
	}
}
