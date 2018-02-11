package com.mmorpg.mir.model.country.packet;

import com.mmorpg.mir.model.country.model.Coppers;
import com.mmorpg.mir.model.country.model.vo.CoppersVO;

public class SM_Country_Coppers {
	private CoppersVO vo;
	private int sign;

	public static SM_Country_Coppers valueOf(Coppers coppers, int sign) {
		SM_Country_Coppers sm = new SM_Country_Coppers();
		sm.setVo(coppers.creatCoppersVO());
		sm.sign = sign;
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

}
