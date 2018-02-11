package com.mmorpg.mir.model.copy.packet;

public class SM_Copy_BuyCount {
	private String id;
	private int count;

	public static SM_Copy_BuyCount valueOf(String id, int count) {
		SM_Copy_BuyCount sm = new SM_Copy_BuyCount();
		sm.count = count;
		sm.id = id;
		return sm;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
