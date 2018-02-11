package com.mmorpg.mir.model.drop.packet;

public class SM_Boss_First_Blood {

	private String bosskey;

	public static SM_Boss_First_Blood valueOf(String key) {
		SM_Boss_First_Blood sm = new SM_Boss_First_Blood();
		sm.bosskey = key;
		return sm;
	}
	
	public String getBosskey() {
		return bosskey;
	}

	public void setBosskey(String bosskey) {
		this.bosskey = bosskey;
	}
}
