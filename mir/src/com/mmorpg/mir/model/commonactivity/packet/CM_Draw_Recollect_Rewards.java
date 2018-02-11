package com.mmorpg.mir.model.commonactivity.packet;

import java.util.HashSet;

public class CM_Draw_Recollect_Rewards {
	
	private HashSet<String> ids;
	
	private boolean useGold;

	public HashSet<String> getIds() {
		return ids;
	}

	public void setIds(HashSet<String> ids) {
		this.ids = ids;
	}

	public boolean isUseGold() {
		return useGold;
	}

	public void setUseGold(boolean useGold) {
		this.useGold = useGold;
	}

}
