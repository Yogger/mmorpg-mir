package com.mmorpg.mir.model.country.packet;

import com.mmorpg.mir.model.country.model.Country;
import com.mmorpg.mir.model.country.model.vo.CourtVO;

public class SM_Country_Offical {
	private CourtVO vo;

	public static SM_Country_Offical valueOf(Country country) {
		SM_Country_Offical sm = new SM_Country_Offical();
		sm.vo = country.getCourt().creatVO();
		return sm;
	}

	public CourtVO getVo() {
		return vo;
	}

	public void setVo(CourtVO vo) {
		this.vo = vo;
	}
}
