package com.mmorpg.mir.model.kingofwar.packet.vo;

import java.util.HashMap;
import java.util.Map;

import com.mmorpg.mir.model.gameobjects.StatusNpc;

public class ReliveStatusVO {
	private Map<String, Integer> reliveStatus;

	public static ReliveStatusVO valueOf(Map<String, StatusNpc> statusNpcs) {
		ReliveStatusVO vo = new ReliveStatusVO();
		vo.setReliveStatus(new HashMap<String, Integer>());
		for (StatusNpc relive : statusNpcs.values()) {
			vo.getReliveStatus().put(relive.getSpawnKey(), relive.getStatus());
		}
		return vo;
	}

	public Map<String, Integer> getReliveStatus() {
		return reliveStatus;
	}

	public void setReliveStatus(Map<String, Integer> reliveStatus) {
		this.reliveStatus = reliveStatus;
	}

}
