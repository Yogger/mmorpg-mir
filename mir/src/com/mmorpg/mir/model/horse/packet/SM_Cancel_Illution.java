package com.mmorpg.mir.model.horse.packet;

import com.mmorpg.mir.model.horse.model.HorseAppearance;
import com.mmorpg.mir.model.system.packet.SM_System_Sign;

public class SM_Cancel_Illution extends SM_System_Sign{
	private HorseAppearance appearance;
	
	public static SM_Cancel_Illution valueOf(int sign,HorseAppearance appearance){
		SM_Cancel_Illution result = new SM_Cancel_Illution();
		result.setSign(sign);
		result.appearance =appearance;
		return result;
	}

	public HorseAppearance getAppearance() {
		return appearance;
	}

	public void setAppearance(HorseAppearance appearance) {
		this.appearance = appearance;
	}
	
	
}
