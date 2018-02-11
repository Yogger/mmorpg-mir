package com.mmorpg.mir.model.gang.packet;

public class SM_Gang_Login {
	private String name;

	public static SM_Gang_Login valueOf(String name) {
		SM_Gang_Login sm = new SM_Gang_Login();
		sm.name = name;
		return sm;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
