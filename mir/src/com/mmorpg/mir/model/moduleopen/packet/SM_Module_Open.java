package com.mmorpg.mir.model.moduleopen.packet;

import java.util.ArrayList;

public class SM_Module_Open {
	
	private ArrayList<String> keys;

	public static SM_Module_Open valueOf(ArrayList<String> k) {
		SM_Module_Open sm = new SM_Module_Open();
		sm.keys = k;
		return sm;
	}

	public ArrayList<String> getKeys() {
    	return keys;
    }

	public void setKeys(ArrayList<String> keys) {
    	this.keys = keys;
    }
	
}
