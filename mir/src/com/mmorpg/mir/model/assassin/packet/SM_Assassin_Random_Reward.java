package com.mmorpg.mir.model.assassin.packet;

import java.util.ArrayList;

import com.mmorpg.mir.model.assassin.model.AssassinRandVO;

public class SM_Assassin_Random_Reward {
	private int hpPercent;

	private ArrayList<AssassinRandVO> randVOs;
	
	private int randPoints;

	public static SM_Assassin_Random_Reward valueOf(int hpPercentConfig, ArrayList<AssassinRandVO> vos, int randPoints) {
		SM_Assassin_Random_Reward sm = new SM_Assassin_Random_Reward();
		sm.hpPercent = hpPercentConfig;
		sm.randVOs = vos;
		sm.randPoints = randPoints;
		return sm;
	}
	
	public int getHpPercent() {
		return hpPercent;
	}

	public void setHpPercent(int hpPercent) {
		this.hpPercent = hpPercent;
	}

	public ArrayList<AssassinRandVO> getRandVOs() {
		return randVOs;
	}

	public void setRandVOs(ArrayList<AssassinRandVO> randVOs) {
		this.randVOs = randVOs;
	}

	public int getRandPoints() {
		return randPoints;
	}

	public void setRandPoints(int randPoints) {
		this.randPoints = randPoints;
	}

}
