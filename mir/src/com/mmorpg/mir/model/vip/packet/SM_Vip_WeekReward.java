package com.mmorpg.mir.model.vip.packet;

public class SM_Vip_WeekReward {
	private long nextRecivedWeekTime;

	public static SM_Vip_WeekReward valueOf(long nextRecivedWeekTime) {
		SM_Vip_WeekReward sm = new SM_Vip_WeekReward();
		sm.nextRecivedWeekTime = nextRecivedWeekTime;
		return sm;
	}

	public long getNextRecivedWeekTime() {
		return nextRecivedWeekTime;
	}

	public void setNextRecivedWeekTime(long nextRecivedWeekTime) {
		this.nextRecivedWeekTime = nextRecivedWeekTime;
	}
}
