package com.mmorpg.mir.model.country.packet;

import com.mmorpg.mir.model.country.model.Tank;
import com.mmorpg.mir.model.country.model.vo.TankVO;

public class SM_Country_DistributeTank {
	private TankVO tankVO;

	public static SM_Country_DistributeTank valueOf(Tank tank) {
		SM_Country_DistributeTank sm = new SM_Country_DistributeTank();
		sm.tankVO = tank.createVO();
		return sm;
	}

	public TankVO getTankVO() {
		return tankVO;
	}

	public void setTankVO(TankVO tankVO) {
		this.tankVO = tankVO;
	}

}
