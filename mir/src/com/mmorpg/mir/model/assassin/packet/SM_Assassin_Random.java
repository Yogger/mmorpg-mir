package com.mmorpg.mir.model.assassin.packet;

import com.mmorpg.mir.model.assassin.config.AssassinConfig;

public class SM_Assassin_Random {

	private long rewardTime;

	private int hpPercent;
	
	private int randPoints;

	public static SM_Assassin_Random valueOf(int hpConfig, int randPoints) { 
		SM_Assassin_Random sm = new SM_Assassin_Random();
		sm.rewardTime = System.currentTimeMillis() + (1000L * AssassinConfig.getInstance().ROLL_TIME.getValue());
		sm.hpPercent = hpConfig;
		sm.randPoints = randPoints;
		return sm;
	}
	
	public long getRewardTime() {
		return rewardTime;
	}

	public void setRewardTime(long rewardTime) {
		this.rewardTime = rewardTime;
	}

	public int getHpPercent() {
		return hpPercent;
	}

	public void setHpPercent(int hpPercent) {
		this.hpPercent = hpPercent;
	}

	public int getRandPoints() {
		return randPoints;
	}

	public void setRandPoints(int randPoints) {
		this.randPoints = randPoints;
	}

}
