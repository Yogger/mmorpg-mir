package com.mmorpg.mir.model.country.packet;

import java.util.HashSet;

public class CM_Country_Take {
	private HashSet<Integer> indexs;
	private boolean pack;

	public HashSet<Integer> getIndexs() {
		return indexs;
	}

	public void setIndexs(HashSet<Integer> indexs) {
		this.indexs = indexs;
	}

	public boolean isPack() {
		return pack;
	}

	public void setPack(boolean pack) {
		this.pack = pack;
	}
}
