package com.mmorpg.mir.model.item.packet;

import org.apache.commons.lang.StringUtils;

public class CM_Change_Equipment_Suit {

	private int index;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public static void main(String[] args) {
		String[] ids = new String[] { "d", "b", "s" };

		String result = StringUtils.join(ids, "|");
		System.err.println(result);
	}
}
