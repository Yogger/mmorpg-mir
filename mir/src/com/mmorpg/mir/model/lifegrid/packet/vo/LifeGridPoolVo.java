package com.mmorpg.mir.model.lifegrid.packet.vo;

import com.mmorpg.mir.model.lifegrid.model.LifeGridPool;
import com.mmorpg.mir.model.lifegrid.model.LifeGridStorage;

public class LifeGridPoolVo {
	private int point;
	private LifeGridStorage equipStorage;
	private LifeGridStorage packStorage;
	private LifeGridStorage houseStorage;

	public static LifeGridPoolVo valueOf(LifeGridPool pool) {
		LifeGridPoolVo result = new LifeGridPoolVo();
		result.point = pool.getPoint();
		result.equipStorage = pool.getEquipStorage();
		result.packStorage = pool.getPackStorage();
		result.houseStorage = pool.getHouseStorage();
		return result;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public LifeGridStorage getEquipStorage() {
		return equipStorage;
	}

	public void setEquipStorage(LifeGridStorage equipStorage) {
		this.equipStorage = equipStorage;
	}

	public LifeGridStorage getPackStorage() {
		return packStorage;
	}

	public void setPackStorage(LifeGridStorage packStorage) {
		this.packStorage = packStorage;
	}

	public LifeGridStorage getHouseStorage() {
		return houseStorage;
	}

	public void setHouseStorage(LifeGridStorage houseStorage) {
		this.houseStorage = houseStorage;
	}

}
