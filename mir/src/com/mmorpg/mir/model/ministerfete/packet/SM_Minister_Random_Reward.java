package com.mmorpg.mir.model.ministerfete.packet;

import java.util.ArrayList;

import com.mmorpg.mir.model.ministerfete.model.MinisterRandVO;

public class SM_Minister_Random_Reward {
	private int hpPercent;

	private ArrayList<MinisterRandVO> randVOs;
	
	private int randPoints;

	public static SM_Minister_Random_Reward valueOf(int hpPercentConfig, ArrayList<MinisterRandVO> vos, int randPoints) {
		SM_Minister_Random_Reward sm = new SM_Minister_Random_Reward();
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

	public ArrayList<MinisterRandVO> getRandVOs() {
		return randVOs;
	}

	public void setRandVOs(ArrayList<MinisterRandVO> randVOs) {
		this.randVOs = randVOs;
	}

	public int getRandPoints() {
		return randPoints;
	}

	public void setRandPoints(int randPoints) {
		this.randPoints = randPoints;
	}

}
