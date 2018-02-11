package com.mmorpg.mir.model.country.packet;

import java.util.HashSet;

public class CM_Country_Store {
	private HashSet<Integer> indexs;
	private boolean inPack;

	public HashSet<Integer> getIndexs() {
		return indexs;
	}

	public void setIndexs(HashSet<Integer> indexs) {
		this.indexs = indexs;
	}

	public boolean isInPack() {
		return inPack;
	}

	public void setInPack(boolean inPack) {
		this.inPack = inPack;
	}

}
