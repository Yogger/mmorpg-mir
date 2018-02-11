package com.mmorpg.mir.model.rank.packet;

public class SM_World_Level {

	private int worldLevel;
	
	public static SM_World_Level valueOf(int level) {
		SM_World_Level sm = new SM_World_Level();
		sm.worldLevel = level;
		return sm;
	}

	public int getWorldLevel() {
    	return worldLevel;
    }

	public void setWorldLevel(int worldLevel) {
    	this.worldLevel = worldLevel;
    }
}
