package com.mmorpg.mir.model.commonactivity.packet;

import com.mmorpg.mir.model.gameobjects.Player;

public class SM_Can_Recollect_Count {

	private int recollectAllCount;

	public static SM_Can_Recollect_Count valueOf(Player player) {
		SM_Can_Recollect_Count sm = new SM_Can_Recollect_Count();
		sm.recollectAllCount = player.getCommonActivityPool().getCurrentRecollectActive().getAllCanClawBackCount(player);
		return sm;
	}

	public int getRecollectAllCount() {
		return recollectAllCount;
	}

	public void setRecollectAllCount(int recollectAllCount) {
		this.recollectAllCount = recollectAllCount;
	}
	
}
