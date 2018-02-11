package com.mmorpg.mir.model.commonactivity.model.vo;

import com.mmorpg.mir.model.commonactivity.model.CommonIdentifyTreasure;
import com.mmorpg.mir.model.commonactivity.model.CommonIdentifyTreasureServer;

public class CommonIdentifyTreasureVo {
	private int luckValue;
	
	private long startTime;
	
	private long endTime;

	public static CommonIdentifyTreasureVo valueOf(CommonIdentifyTreasureServer treasureServer, CommonIdentifyTreasure treasure){
		CommonIdentifyTreasureVo vo = new CommonIdentifyTreasureVo();
		if(treasureServer.getVersion() != treasure.getVersion()){
			treasure.reset(treasureServer);
		}
		vo.luckValue = treasure.getLuckValue();
		vo.startTime = treasureServer.getStartTime();
		vo.endTime = treasureServer.getEndTime();
		return vo;
	}

	public int getLuckValue() {
		return luckValue;
	}

	public void setLuckValue(int luckValue) {
		this.luckValue = luckValue;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
}
