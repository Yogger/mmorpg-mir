package com.mmorpg.mir.model.openactive.packet;

import java.util.Map;

import com.mmorpg.mir.model.openactive.model.vo.CompeteVO;

public class SM_Query_CompeteStatus {
	private Map<Integer, CompeteVO> status;
	
	public static SM_Query_CompeteStatus valueOf(Map<Integer, CompeteVO> ret) {
		SM_Query_CompeteStatus sm = new SM_Query_CompeteStatus();
		sm.status = ret;
		return sm;
	}

	public Map<Integer, CompeteVO> getStatus() {
		return status;
	}

	public void setStatus(Map<Integer, CompeteVO> status) {
		this.status = status;
	}

}
