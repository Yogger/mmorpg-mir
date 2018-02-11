package com.mmorpg.mir.model.ministerfete.packet;

public class SM_Minister_Dice {
	private int randPoints;
	
	private int hpPercent;
	
	public static SM_Minister_Dice valueOf(int points, int hpPercent) {
		SM_Minister_Dice sm = new SM_Minister_Dice();
		sm.randPoints = points;
		sm.hpPercent = hpPercent;
		return sm;
	}

	public int getRandPoints() {
		return randPoints;
	}

	public void setRandPoints(int randPoints) {
		this.randPoints = randPoints;
	}

	public int getHpPercent() {
		return hpPercent;
	}

	public void setHpPercent(int hpPercent) {
		this.hpPercent = hpPercent;
	}

}
