package com.mmorpg.mir.model.military.packet;

import java.util.Map;

public class SM_military_update {

	private int rank;
	private long updateTime;
	private Map<Integer, String> equipments;

	public static SM_military_update valueOf(int rank, long updateTime, Map<Integer, String> equipments) {
		SM_military_update update = new SM_military_update();
		update.rank = rank;
		update.updateTime = updateTime;
		update.equipments = equipments;
		return update;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public Map<Integer, String> getEquipments() {
		return equipments;
	}

	public void setEquipments(Map<Integer, String> equipments) {
		this.equipments = equipments;
	}

}
