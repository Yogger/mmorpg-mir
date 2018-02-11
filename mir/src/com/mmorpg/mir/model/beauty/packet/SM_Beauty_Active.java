package com.mmorpg.mir.model.beauty.packet;

import com.mmorpg.mir.model.beauty.model.BeautyGirl;

public class SM_Beauty_Active {
	private BeautyGirl girl;

	public BeautyGirl getGirl() {
		return girl;
	}

	public void setGirl(BeautyGirl girl) {
		this.girl = girl;
	}

	public static SM_Beauty_Active valueOf(BeautyGirl beautyGirl) {
		SM_Beauty_Active result = new SM_Beauty_Active();
		result.girl = beautyGirl;
		return result;
	}

}
