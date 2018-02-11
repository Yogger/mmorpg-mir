package com.mmorpg.mir.model.item.event;

import com.windforce.common.event.event.IEvent;

public class TreasureEvent implements IEvent {

	private long owner;
	// 探宝次数
	private int count;

	public static TreasureEvent valueOf(long owner, int count) {
		TreasureEvent event = new TreasureEvent();
		event.owner = owner;
		event.count = count;
		return event;
	}

	@Override
	public long getOwner() {
		return owner;
	}

	public int getCount() {
		return count;
	}

	public void setOwner(long owner) {
		this.owner = owner;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
