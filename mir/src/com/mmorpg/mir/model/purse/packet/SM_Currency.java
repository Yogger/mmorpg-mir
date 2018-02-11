package com.mmorpg.mir.model.purse.packet;

import java.util.Map;

import org.h2.util.New;

import com.mmorpg.mir.model.gameobjects.Player;

public class SM_Currency {
	private Map<Integer, Long> updateMap = New.hashMap();
	private Map<Integer, Long> updateTotalMap = New.hashMap();

	public static SM_Currency valueOf(Player player) {
		SM_Currency smc = new SM_Currency();
		player.getPurse().collectUpdate(smc.updateMap, smc.updateTotalMap);
		return smc;
	}

	public Map<Integer, Long> getUpdateMap() {
		return updateMap;
	}

	public void setUpdateMap(Map<Integer, Long> updateMap) {
		this.updateMap = updateMap;
	}

	public Map<Integer, Long> getUpdateTotalMap() {
		return updateTotalMap;
	}

	public void setUpdateTotalMap(Map<Integer, Long> updateTotalMap) {
		this.updateTotalMap = updateTotalMap;
	}

}
