package com.mmorpg.mir.model.item.packet;

public class SM_Combining {
	private int code;
	
	public static SM_Combining valueOf(int c) {
		SM_Combining cc = new SM_Combining();
		cc.code = c;
		return cc;
	}

	public int getCode() {
    	return code;
    }

	public void setCode(int code) {
    	this.code = code;
    }
	
}
