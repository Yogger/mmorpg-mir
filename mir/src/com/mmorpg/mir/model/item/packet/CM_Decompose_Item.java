package com.mmorpg.mir.model.item.packet;

import java.util.HashSet;

public class CM_Decompose_Item {
	private HashSet<Long> indexs;

	public HashSet<Long> getIndexs() {
		return indexs;
	}

	public void setIndexs(HashSet<Long> indexs) {
		this.indexs = indexs;
	}
}
