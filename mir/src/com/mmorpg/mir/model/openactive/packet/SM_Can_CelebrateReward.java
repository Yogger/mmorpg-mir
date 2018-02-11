package com.mmorpg.mir.model.openactive.packet;

import com.mmorpg.mir.model.openactive.model.RechargeCelebrate;

public class SM_Can_CelebrateReward {

	private RechargeCelebrate rechargeCelebrate;
	
	public static SM_Can_CelebrateReward valueOf(RechargeCelebrate rc) {
		SM_Can_CelebrateReward sm = new SM_Can_CelebrateReward();
		sm.rechargeCelebrate = rc;
		return sm;
	}

	public RechargeCelebrate getRechargeCelebrate() {
		return rechargeCelebrate;
	}

	public void setRechargeCelebrate(RechargeCelebrate rechargeCelebrate) {
		this.rechargeCelebrate = rechargeCelebrate;
	}
	
}
