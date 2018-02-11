package com.mmorpg.mir.model.fashion.packet;

public class SM_Fashion_Operate_Hide {
	private boolean hided;

	public static SM_Fashion_Operate_Hide valueOf(boolean hided) {
		SM_Fashion_Operate_Hide result = new SM_Fashion_Operate_Hide();
		result.hided = hided;
		return result;
	}

	public boolean isHided() {
		return hided;
	}

	public void setHided(boolean hided) {
		this.hided = hided;
	}

}
