package com.mmorpg.mir.model.openactive.model.vo;

import java.util.HashMap;

import com.mmorpg.mir.model.openactive.model.ActivityInfo;
import com.mmorpg.mir.model.openactive.model.RechargeCelebrate;
import com.mmorpg.mir.model.serverstate.ServerState;

public class CelebrateActivityVo {
	private HashMap<Integer, ActivityInfo> activityInfos;
	
	private RechargeCelebrate rechargeCelebrate;

	public static CelebrateActivityVo valueOf(RechargeCelebrate personnalRechargeInfo) {
		CelebrateActivityVo result = new CelebrateActivityVo();
		result.activityInfos = new HashMap<Integer, ActivityInfo>(ServerState.getInstance().getCelebrateActivityInfos());
		result.rechargeCelebrate = personnalRechargeInfo;
		return result;
	}

	public HashMap<Integer, ActivityInfo> getActivityInfos() {
		return activityInfos;
	}

	public void setActivityInfos(HashMap<Integer, ActivityInfo> activityInfos) {
		this.activityInfos = activityInfos;
	}

	public RechargeCelebrate getRechargeCelebrate() {
		return rechargeCelebrate;
	}

	public void setRechargeCelebrate(RechargeCelebrate rechargeCelebrate) {
		this.rechargeCelebrate = rechargeCelebrate;
	}
	
}
