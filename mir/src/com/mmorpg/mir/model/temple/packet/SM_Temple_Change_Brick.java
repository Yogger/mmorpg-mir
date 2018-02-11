package com.mmorpg.mir.model.temple.packet;

import com.mmorpg.mir.model.temple.model.Brick;

public class SM_Temple_Change_Brick {
	private String id;
	private boolean useGold;
	
	public static SM_Temple_Change_Brick valueOf(Brick brick, boolean useGold) {
		SM_Temple_Change_Brick sm = new SM_Temple_Change_Brick();
		sm.id = brick.getId();
		sm.useGold = useGold;
		return sm;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isUseGold() {
    	return useGold;
    }

	public void setUseGold(boolean useGold) {
    	this.useGold = useGold;
    }
	
}
