package com.mmorpg.mir.model.express.packet;

import com.mmorpg.mir.model.express.model.LorryVO;
import com.mmorpg.mir.model.system.packet.SM_System_Sign;

public class SM_Express_Start extends SM_System_Sign {
	private LorryVO lorry;

	public static SM_Express_Start valueOf(int sign, LorryVO vo) {
		SM_Express_Start sm = new SM_Express_Start();
		sm.setSign(sign);
		sm.setLorry(vo);
		return sm;
	}
	
	public LorryVO getLorry() {
		return lorry;
	}

	public void setLorry(LorryVO lorry) {
		this.lorry = lorry;
	}
	
}
