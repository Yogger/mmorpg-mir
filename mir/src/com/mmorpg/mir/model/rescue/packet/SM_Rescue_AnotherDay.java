package com.mmorpg.mir.model.rescue.packet;

import com.mmorpg.mir.model.rescue.model.Rescue;

public class SM_Rescue_AnotherDay {
	private Rescue rescue;

	public static SM_Rescue_AnotherDay valueOf(Rescue rescue) {
		SM_Rescue_AnotherDay sm = new SM_Rescue_AnotherDay();
		sm.rescue = rescue;
		return sm;
	}

	public Rescue getRescue() {
		return rescue;
	}

	public void setRescue(Rescue rescue) {
		this.rescue = rescue;
	}

}
