package com.mmorpg.mir.model.fashion.packet;

public class SM_Fashion_Wear {

	private int fashionId;

	public static SM_Fashion_Wear valueOf(int fashionId) {
		SM_Fashion_Wear result = new SM_Fashion_Wear();
		result.fashionId = fashionId;
		return result;
	}

	public int getFashionId() {
		return fashionId;
	}

	public void setFashionId(int fashionId) {
		this.fashionId = fashionId;
	}

}
