package com.mmorpg.mir.model.lifegrid.packet;

public class CM_LifeGrid_Drop {
	private int type;
	private int[] indexs;

	public int getType() {
		return type;
	}

	public int[] getIndexs() {
		return indexs;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setIndexs(int[] indexs) {
		this.indexs = indexs;
	}

}
