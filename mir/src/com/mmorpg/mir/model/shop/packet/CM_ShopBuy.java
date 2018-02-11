package com.mmorpg.mir.model.shop.packet;

public class CM_ShopBuy {
	private String id;
	private int count;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
