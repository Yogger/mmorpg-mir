package com.mmorpg.mir.model.horse.packet;

import com.mmorpg.mir.model.horse.model.HorseAppearance;
import com.mmorpg.mir.model.system.packet.SM_System_Sign;

public class SM_Illution_Active extends SM_System_Sign {
	private HorseAppearance appearance;

	public static SM_Illution_Active valueOf(int sign, HorseAppearance appearance) {
		SM_Illution_Active result = new SM_Illution_Active();
		result.setSign(sign);
		result.appearance = appearance;
		return result;
	}

	public HorseAppearance getAppearance() {
		return appearance;
	}

	public void setAppearance(HorseAppearance appearance) {
		this.appearance = appearance;
	}

}
