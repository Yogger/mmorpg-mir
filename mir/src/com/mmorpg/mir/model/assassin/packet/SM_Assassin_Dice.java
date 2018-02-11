package com.mmorpg.mir.model.assassin.packet;

public class SM_Assassin_Dice {
	private int randPoints;
	
	private int hpPercent;
	
	public static SM_Assassin_Dice valueOf(int points, int hpPercent) {
		SM_Assassin_Dice sm = new SM_Assassin_Dice();
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
