package com.mmorpg.mir.model.country.packet;

import com.mmorpg.mir.model.country.model.vo.ArmsFactoryVO;

public class SM_Country_Factory {
	private ArmsFactoryVO vo;

	public static SM_Country_Factory valueOf(ArmsFactoryVO vo) {
		SM_Country_Factory sm = new SM_Country_Factory();
		sm.setVo(vo);
		return sm;
	}

	public ArmsFactoryVO getVo() {
		return vo;
	}

	public void setVo(ArmsFactoryVO vo) {
		this.vo = vo;
	}
}
