package com.mmorpg.mir.model.item;

import org.codehaus.jackson.annotate.JsonIgnore;

public class LifeGridItem extends AbstractItem {
	/** 经验值 */
	private int exp;

	public LifeGridItem() {
		this.exp = 0;
	}

	@JsonIgnore
	public int getLifeGridType() {
		return getResource().getLifeGridType();
	}

	@JsonIgnore
	public boolean isMaxLevel() {
		return getResource().getNeedExp() == 0;
	}

	@JsonIgnore
	public boolean isSpecialOne() {
		return getResource().getStats() == null || getResource().getStats().length == 0;
	}

	@JsonIgnore
	public boolean isLock() {
		return isState(ItemState.LOCK.getMark());
	}

	@JsonIgnore
	public void closeLock() {
		closeState(ItemState.LOCK.getMark());
	}

	@JsonIgnore
	public void openLock() {
		openState(ItemState.LOCK.getMark());
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

}
