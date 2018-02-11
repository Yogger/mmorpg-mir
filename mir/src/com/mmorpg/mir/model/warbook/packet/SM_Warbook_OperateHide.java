package com.mmorpg.mir.model.warbook.packet;

public class SM_Warbook_OperateHide {
	private boolean hided;

	public static SM_Warbook_OperateHide valueOf(boolean hided) {
		SM_Warbook_OperateHide result = new SM_Warbook_OperateHide();
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
