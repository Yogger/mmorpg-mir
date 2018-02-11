package com.mmorpg.mir.model.welfare.packet;

public class SM_Active_Value {
	private byte id;
	private int addValue;

	public static SM_Active_Value valueOf(int id, int add) {
		SM_Active_Value sm = new SM_Active_Value();
		sm.id = (byte) id;
		sm.addValue = add;
		return sm;
	}
	
	public byte getId() {
		return id;
	}

	public void setId(byte id) {
		this.id = id;
	}

	public int getAddValue() {
		return addValue;
	}

	public void setAddValue(int addValue) {
		this.addValue = addValue;
	}

}
