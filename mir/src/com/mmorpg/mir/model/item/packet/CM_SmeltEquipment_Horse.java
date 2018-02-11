package com.mmorpg.mir.model.item.packet;

import java.util.HashSet;

public class CM_SmeltEquipment_Horse {

	private HashSet<Integer> indexs;
	private int isTreasureWare;

	public HashSet<Integer> getIndexs() {
		return indexs;
	}

	public void setIndexs(HashSet<Integer> indexs) {
		this.indexs = indexs;
	}

	public int getIsTreasureWare() {
		return isTreasureWare;
	}

	public void setIsTreasureWare(int isTreasureWare) {
		this.isTreasureWare = isTreasureWare;
	}

}
