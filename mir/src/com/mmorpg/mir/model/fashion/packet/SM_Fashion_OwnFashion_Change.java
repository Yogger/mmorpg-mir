package com.mmorpg.mir.model.fashion.packet;

import java.util.HashSet;

import com.mmorpg.mir.model.fashion.model.FashionPool;

public class SM_Fashion_OwnFashion_Change {

	private int currentFashionId;

	private int exp;

	private HashSet<Integer> ownFashion;

	public static SM_Fashion_OwnFashion_Change valueOf(FashionPool fashionPool) {
		SM_Fashion_OwnFashion_Change result = new SM_Fashion_OwnFashion_Change();
		result.currentFashionId = fashionPool.getCurrentFashionId();
		result.exp = fashionPool.getExp();
		result.ownFashion = new HashSet<Integer>(fashionPool.getOwnFashions());
		return result;
	}

	public int getCurrentFashionId() {
		return currentFashionId;
	}

	public void setCurrentFashionId(int currentFashionId) {
		this.currentFashionId = currentFashionId;
	}

	public HashSet<Integer> getOwnFashion() {
		return ownFashion;
	}

	public void setOwnFashion(HashSet<Integer> ownFashion) {
		this.ownFashion = ownFashion;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

}
