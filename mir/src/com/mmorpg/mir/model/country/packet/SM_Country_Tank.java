package com.mmorpg.mir.model.country.packet;

import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.h2.util.New;

import com.mmorpg.mir.model.country.model.Tank;
import com.mmorpg.mir.model.country.model.vo.TankVO;

public class SM_Country_Tank {
	private Map<Integer, TankVO> tanks = New.hashMap();

	public static SM_Country_Tank valueOf(Tank... tanks) {
		SM_Country_Tank vos = new SM_Country_Tank();
		if (!ArrayUtils.isEmpty(tanks)) {
			for (Tank tank : tanks) {
				vos.getTanks().put(tank.getId(), tank.createVO());
			}
		}
		return vos;
	}

	public Map<Integer, TankVO> getTanks() {
		return tanks;
	}

	public void setTanks(Map<Integer, TankVO> tanks) {
		this.tanks = tanks;
	}

}
