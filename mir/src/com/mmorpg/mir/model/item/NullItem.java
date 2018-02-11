package com.mmorpg.mir.model.item;

public class NullItem {

	public static NullItem getInstance() {
		return SingletonHolder.instance;
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder {
		protected static final NullItem instance = new NullItem();
	}

}
