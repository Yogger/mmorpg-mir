package com.mmorpg.mir.model.country.model.vo;

import java.util.HashMap;

import com.mmorpg.mir.model.country.model.ArmsFactory;
import com.mmorpg.mir.model.country.model.Tank;

public class ArmsFactoryVO {
	private String factoryId;
	private HashMap<Integer, TankVO> tankVOs = new HashMap<Integer, TankVO>();

	public static ArmsFactoryVO valueOf(ArmsFactory arms) {
		ArmsFactoryVO vo = new ArmsFactoryVO();
		vo.setFactoryId(arms.getFactoryId());
		if (!arms.getTanks().isEmpty()) {
			for (Tank tank : arms.getTanks().values()) {
				vo.getTankVOs().put(tank.getId(), tank.createVO());
			}
		}
		return vo;
	}

	public String getFactoryId() {
		return factoryId;
	}

	public void setFactoryId(String factoryId) {
		this.factoryId = factoryId;
	}

	public HashMap<Integer, TankVO> getTankVOs() {
		return tankVOs;
	}

	public void setTankVOs(HashMap<Integer, TankVO> tankVOs) {
		this.tankVOs = tankVOs;
	}

}
