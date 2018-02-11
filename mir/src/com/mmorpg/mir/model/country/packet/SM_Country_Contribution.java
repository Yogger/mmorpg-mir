package com.mmorpg.mir.model.country.packet;

import com.mmorpg.mir.model.country.model.Coppers;
import com.mmorpg.mir.model.country.model.vo.CoppersVO;

public class SM_Country_Contribution {
	private CoppersVO vo;
	private int sign;
	private int addContribution;

	public static SM_Country_Contribution valueOf(Coppers coppers, int sign, int inc) {
		SM_Country_Contribution sm = new SM_Country_Contribution();
		sm.setVo(coppers.creatCoppersVO());
		sm.sign = sign;
		sm.addContribution = inc;
		return sm;
	}

	public CoppersVO getVo() {
		return vo;
	}

	public void setVo(CoppersVO vo) {
		this.vo = vo;
	}

	public int getSign() {
		return sign;
	}

	public void setSign(int sign) {
		this.sign = sign;
	}

	public int getAddContribution() {
    	return addContribution;
    }

	public void setAddContribution(int addContribution) {
    	this.addContribution = addContribution;
    }

}
