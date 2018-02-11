package com.mmorpg.mir.model.ministerfete.packet;

import com.mmorpg.mir.model.ministerfete.config.MinisterFeteConfig;

public class SM_Minister_Random {

	private long rewardTime;

	private int hpPercent;
	
	private int randPoints;

	public static SM_Minister_Random valueOf(int hpConfig, int randPoints) { 
		SM_Minister_Random sm = new SM_Minister_Random();
		sm.rewardTime = System.currentTimeMillis() + (1000L * MinisterFeteConfig.getInstance().ROLL_TIME.getValue());
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
