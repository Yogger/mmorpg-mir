package com.mmorpg.mir.model.exchange.packet;

public class SM_Exchange_RJ_Request {

	private String name;
	
	public static SM_Exchange_RJ_Request valueOf(String n) {
		SM_Exchange_RJ_Request sm = new SM_Exchange_RJ_Request();
		sm.name = n;
		return sm;
	}

	public String getName() {
    	return name;
    }

	public void setName(String name) {
    	this.name = name;
    }
}
