package com.mmorpg.mir.model.beauty.packet;

import com.mmorpg.mir.model.beauty.model.BeautyGirl;

public class SM_Beauty_Linger {

	private BeautyGirl beautyGirl;

	public static SM_Beauty_Linger valueOf(BeautyGirl girl) {
		SM_Beauty_Linger result = new SM_Beauty_Linger();
		result.beautyGirl = girl;
		return result;
	}

	public BeautyGirl getBeautyGirl() {
		return beautyGirl;
	}

	public void setBeautyGirl(BeautyGirl beautyGirl) {
		this.beautyGirl = beautyGirl;
	}

}
