package com.mmorpg.mir.model.country.packet;

import java.util.ArrayList;

import com.mmorpg.mir.model.country.model.log.FeteLogValue;

public class SM_Country_Fete_Log {

	private ArrayList<FeteLogValue> log;
	
	public static SM_Country_Fete_Log valueOf(ArrayList<FeteLogValue> list) {
		SM_Country_Fete_Log sm = new SM_Country_Fete_Log();
		sm.log = list;
		return sm;
	}

	public ArrayList<FeteLogValue> getLog() {
    	return log;
    }

	public void setLog(ArrayList<FeteLogValue> log) {
    	this.log = log;
    }

}
