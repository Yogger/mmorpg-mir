package com.mmorpg.mir.model.player.packet;

import java.util.HashMap;

import com.mmorpg.mir.model.system.packet.CM_System_Sign;

public class CM_ComplexStat_Settings extends CM_System_Sign{

	private HashMap<Integer, Boolean> complexSettings;

	public HashMap<Integer, Boolean> getComplexSettings() {
    	return complexSettings;
    }

	public void setComplexSettings(HashMap<Integer, Boolean> complexSettings) {
    	this.complexSettings = complexSettings;
    }

}
